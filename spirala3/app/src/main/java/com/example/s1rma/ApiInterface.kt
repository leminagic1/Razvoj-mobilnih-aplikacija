package com.example.s1rma

import android.telecom.Call
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query



interface ApiInterface {

        @GET("plants/search")

        suspend fun searchPlantsByName(
                @Query("q") scientific_name: String,
                @Query("token") token: String = "sjvKdCuX6R4Z_H-tU4s_HuUlRxTLdBQhOBVZ2-721Cw"
        ): SearchResponse

        @GET("plants/{id}")
        suspend fun getPlantById(
                @Path("id") id: Long,
                @Query("token") token: String = "sjvKdCuX6R4Z_H-tU4s_HuUlRxTLdBQhOBVZ2-721Cw"
        ): PlantDetailResponse
}

data class SearchResponse(
        @SerializedName("data")
        val plants: List<TreflePlant>
)

data class TreflePlant(
        val id: Long,
        @SerializedName("common_name")
        val commonName: String?,
        @SerializedName("scientific_name")
        val scientificName: String?,
        @SerializedName("image_url")
        val image_url: String?,
        @SerializedName("family_common_name")
        val familyCommonName: String?,
        val family: String?,
        val genus: String?,
        val mainSpeciesId: Long
)


data class PlantDetailResponse(
        @SerializedName("data") val plantDetail: PlantDetail
)
data class PlantDetail(
        val id: Long,

        @SerializedName("common_name")

        val commonName: String?,
        @SerializedName("slug")
        val slug: String?,
        @SerializedName("scientific_name")
        val scientificName: String?,
        @SerializedName("main_species_id")
        val mainSpeciesId: Long,
        @SerializedName("image_url")
        val imageUrl: String?,
        val year: Int,
        val bibliography: String,
        val author: String,
        @SerializedName("family_common_name")
        val familyCommonName: String?,
        @SerializedName("genus_id")
        val genusId: Long,
        val observations: String,
        val vegetable: Boolean,

        @SerializedName("main_species")
        val main_species: MainSpecies
)
data class MainSpecies(
        val id: Long,
        val flower: Flower,
        val specifications: Specifications,
        val growth: Growth,

        @SerializedName("common_name")
        val commonName: String?,
        val slug: String?,
        @SerializedName("scientific_name")
        val scientificName: String?,
        val year: Int,
        val bibliography: String,
        val author: String,
        val status: String,
        val rank: String,
        @SerializedName("family_common_name")
        val familyCommonName: String?,
        @SerializedName("genus_id")
        val genusId: Long,
        val observations: String,
        val vegetable: Boolean,
        @SerializedName("image_url")
        val imageUrl: String?,
        val genus: String?,
        val family: String?,
        val duration: String?,
        @SerializedName("edible_part")
        val ediblePart: String?,
        val edible: Boolean,

)
data class Flower(
        val color: List<String>
)
data class Specifications(
        val toxicity: String?,
      //  val growth: Growth
)

data class Growth(
        @SerializedName("soil_texture")
        val soil_texture: List<Int>?,
        @SerializedName("light")
        val light: Int?,
        @SerializedName("atmospheric_humidity")
        val atmospheric_humidity: Int?
)

