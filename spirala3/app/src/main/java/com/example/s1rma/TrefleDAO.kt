package com.example.s1rma


import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.IOException

import retrofit2.converter.gson.GsonConverterFactory

import java.net.URL

import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
class TrefleDAO() {
    val defaultBitmap: Bitmap = BitmapFactory.decodeResource(
        Resources.getSystem(),
        android.R.drawable.ic_menu_report_image
    )

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://trefle.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiInterface::class.java)

    private fun extractScientificName(fullName: String): String {
        val regex = Regex("\\(([^)]+)\\)")
        val matchResult = regex.find(fullName)
        return matchResult?.groupValues?.get(1) ?: fullName
    }


    suspend fun getImage(biljka: Biljka): Bitmap {
        return try {
            val scientificName = extractScientificName(biljka.naziv)
            val response = service.searchPlantsByName(scientific_name = scientificName)
            val imageUrl = response.plants.firstOrNull()?.image_url
            if (imageUrl != null) {
                val url = URL(imageUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } else {
                defaultBitmap
            }
        } catch (e: Exception) {
            defaultBitmap
        }
    }


    suspend fun fixData(biljka: Biljka): Biljka {
        return try {
            val scientificName = extractScientificName(biljka.naziv)
            val searchResponse = service.searchPlantsByName(scientific_name = scientificName)
            val treflePlant = searchResponse.plants.firstOrNull() ?: return biljka

            val plantDetailResponse = service.getPlantById(treflePlant.id)
            val plantDetail = plantDetailResponse.plantDetail


            var updatedPorodica = biljka.porodica
            if (plantDetail.main_species.family != null && plantDetail.main_species.family != biljka.porodica) {
                updatedPorodica = plantDetail.main_species.family
            }

            var updatedJela = biljka.jela
            var updatedMedicinskoUpozorenje = biljka.medicinskoUpozorenje

            if (plantDetail.main_species.edible == false) {
                updatedJela = emptyList()
                if (!updatedMedicinskoUpozorenje.contains("NIJE JESTIVO")) {
                    updatedMedicinskoUpozorenje += " NIJE JESTIVO "
                }
            }

            if (plantDetail.main_species.specifications.toxicity != "none" &&
                !updatedMedicinskoUpozorenje.contains("TOKSIČNO")
            ) {
                updatedMedicinskoUpozorenje += " TOKSIČNO "
            }

            val soilTextures = plantDetail.main_species.growth.soil_texture ?: emptyList()
            val updatedZemljiste = mutableListOf<Zemljiste>()

            if (soilTextures.contains(9)) updatedZemljiste.add(Zemljiste.SLJUNKOVITO)
            if (soilTextures.contains(10)) updatedZemljiste.add(Zemljiste.KRECNJACKO)
            if (soilTextures.contains(1) || soilTextures.contains(2)) updatedZemljiste.add(Zemljiste.GLINENO)
            if (soilTextures.contains(3) || soilTextures.contains(4)) updatedZemljiste.add(Zemljiste.PJESKOVITO)
            if (soilTextures.contains(5) || soilTextures.contains(6)) updatedZemljiste.add(Zemljiste.ILOVACA)
            if (soilTextures.contains(7) || soilTextures.contains(8)) updatedZemljiste.add(Zemljiste.CRNICA)


            val light = plantDetail.main_species.growth.light ?: 0
            val humidity = plantDetail.main_species.growth.atmospheric_humidity ?: 0
            val updatedKlima = mutableListOf<KlimatskiTip>()




            if (light in 6..9 && humidity in 1..5) updatedKlima.add(KlimatskiTip.SREDOZEMNA)
            if (light in 8..10 && humidity in 7..10) updatedKlima.add(KlimatskiTip.TROPSKA)
            if (light in 6..9 && humidity in 5..8) updatedKlima.add(KlimatskiTip.SUBTROPSKA)
            if (light in 4..7 && humidity in 3..7) updatedKlima.add(KlimatskiTip.UMJERENA)
            if (light in 7..9 && humidity in 1..2) updatedKlima.add(KlimatskiTip.SUHA)
            if (light in 0..5 && humidity in 3..7) updatedKlima.add(KlimatskiTip.PLANINSKA)



            biljka.copy(
                porodica = updatedPorodica,
                jela = updatedJela,
                medicinskoUpozorenje = updatedMedicinskoUpozorenje,
                zemljisniTipovi = updatedZemljiste,
                klimatskiTipovi = updatedKlima
            )
        } catch (e: Exception) {
            e.printStackTrace()
            biljka
        }
    }

    suspend fun getPlantsWithFlowerColor(flower_color: String, substr: String): List<Biljka> {
        return try {
            val response = service.searchPlantsByName(scientific_name = substr)
            val plants = response.plants.filter { treflePlant ->
                val plantDetailResponse = service.getPlantById(treflePlant.id)
                val plantDetail = plantDetailResponse.plantDetail

                // Provjera boje cvijeta
                val flowerColorExists = plantDetail.main_species.flower?.color?.any {
                    it.toLowerCase() == flower_color.toLowerCase()
                } ?: false

                // Provjera naziva
                val matchesCommonName =
                    plantDetail.commonName?.contains(substr, ignoreCase = true) ?: false
                val matchesScientificName =
                    plantDetail.scientificName?.contains(substr, ignoreCase = true) ?: false

                flowerColorExists && (matchesCommonName || matchesScientificName)
            }.map { treflePlant ->
                val plantDetailResponse = service.getPlantById(treflePlant.id)
                val plantDetail = plantDetailResponse.plantDetail

                val biljka = Biljka(
                    naziv = plantDetail.scientificName ?: "",
                    porodica = plantDetail.familyCommonName ?: "",
                    medicinskoUpozorenje = plantDetail.main_species.specifications.toxicity ?: "",
                    medicinskeKoristi = emptyList(),
                    profilOkusa = ProfilOkusaBiljke.GORKO,
                    jela = if (plantDetail.main_species.edible == true) listOf("Jestivo") else emptyList(),
                    klimatskiTipovi = emptyList(),
                    zemljisniTipovi = emptyList()
                )
                fixData(biljka)
            }
            plants
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}





