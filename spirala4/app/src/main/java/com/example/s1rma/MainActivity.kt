package com.example.s1rma

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {


    companion object {
        const val REQUEST_CODE_NOVA_BILJKA = 1
        lateinit var appContext: Context
    }

    private lateinit var database: BiljkaDatabase

    private lateinit var biljkaDAO: BiljkaDAO

    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: Button

    private lateinit var pretragaET: EditText
    private lateinit var bojaSPIN: Spinner
    private lateinit var brzaPretragaBtn: Button

    private var trenutniMode: Mode = Mode.MEDICINSKI


    private lateinit var currentReferencePlant: Biljka



    val biljke = listOf(
        Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE,
                MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Nana (Mentha spicata)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
            profilOkusa = ProfilOkusaBiljke.MENTA,
            jela = listOf("Jogurt sa voćem", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae (glavočike)",
            medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od kamilice"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Ružmarin (Rosmarinus officinalis)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO,
                MedicinskaKorist.REGULACIJAPRITISKA
            ),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pečeno pile", "Grah", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Lavanda (Lavandula angustifolia)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE,
                MedicinskaKorist.PODRSKAIMUNITETU
            ),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Jogurt sa voćem"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Majčina dušica (Thymus vulgaris)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Može izazvati alergijske reakcije kod nekih osoba.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO,
                MedicinskaKorist.REGULACIJAPRITISKA
            ),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Piletina s majčinom dušicom", "Rižoto s majčinom dušicom"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Peršin (Petroselinum crispum)",
            porodica = "Apiaceae (štitarka)",
            medicinskoUpozorenje = " Osobe s bubrežnim kamencima trebaju izbjegavati prekomjernu konzumaciju peršina.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO,
                MedicinskaKorist.PODRSKAIMUNITETU
            ),
            profilOkusa = ProfilOkusaBiljke.KORIJENASTO,
            jela = listOf(
                "Pire krumpir s peršinom",
                "Salata s peršinom i limunom",
                "Supa sa persinom"
            ),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Đumbir (Zingiber officinale)",
            porodica = "Zingiberaceae (đumbirovke)",
            medicinskoUpozorenje = "Može izazvati iritaciju želuca u prevelikim količinama.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO,
                MedicinskaKorist.PODRSKAIMUNITETU
            ),
            profilOkusa = ProfilOkusaBiljke.LJUTO,
            jela = listOf("Kari s piletinom i đumbirom", "Čaj od đumbira", "Suhi slatki dumbir"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.TROPSKA),
            zemljisniTipovi = listOf(Zemljiste.CRNICA, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Limun trava (Cymbopogon citratus)",
            porodica = "Poaceae (trave)",
            medicinskoUpozorenje = "Osobe s bolestima jetre trebaju koristiti limun travu s oprezom.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.REGULACIJAPRITISKA,
                MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.CITRUSNI,
            jela = listOf("Tom Yum supa", "Marinada od limun trave za piletinu", "Sos"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.TROPSKA),
            zemljisniTipovi = listOf(Zemljiste.CRNICA, Zemljiste.PJESKOVITO)
        ),
        Biljka(
            naziv = "Kopriva (Urtica dioica)",
            porodica = "Urticaceae (koprive)",
            medicinskoUpozorenje = "Osobe koje uzimaju lijekove za razrjeđivanje krvi trebaju izbjegavati koprivu.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.REGULACIJAPRITISKA,
                MedicinskaKorist.PODRSKAIMUNITETU
            ),
            profilOkusa = ProfilOkusaBiljke.GORKO,
            jela = listOf("Juha od koprive", "Pesto od koprive"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.CRNICA, Zemljiste.ILOVACA)
        )

    )

    private val flowerColors = listOf(
        "red", "blue", "yellow", "orange", "purple", "brown", "green"
    )

    private val biljkaAdapter = BiljkaAdapter(trenutniMode)

    private var dodaneBiljke: MutableList<Biljka> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        appContext = applicationContext
        database=BiljkaDatabase.getInstance(this)
        biljkaDAO = database.biljkaDao()


        spinner = findViewById(R.id.modSpinner)
        recyclerView = findViewById(R.id.biljkeRV)
        button = findViewById(R.id.resetBtn)
        currentReferencePlant = biljke.firstOrNull() ?: return

        pretragaET = findViewById(R.id.pretragaET)
        bojaSPIN = findViewById(R.id.bojaSPIN)
        brzaPretragaBtn = findViewById(R.id.brzaPretraga)

        val novaBiljkaButton = findViewById<Button>(R.id.novaBiljkaBtn)



        val adapterColors = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            flowerColors
        )
        adapterColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bojaSPIN.adapter = adapterColors


        novaBiljkaButton.setOnClickListener {
            val intent = Intent(this, NovaBiljkaActivity::class.java)

            startActivityForResult(intent, REQUEST_CODE_NOVA_BILJKA)
        }



        biljkaAdapter.setOnItemClickListener { biljka ->

            biljkaAdapter.setCurrentReferencePlant(biljka)
            updateRecyclerView(biljka, trenutniMode)

        }

        val adapterMode = ArrayAdapter.createFromResource(
            this,
            R.array.modes_array,
            android.R.layout.simple_spinner_item
        )
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterMode

        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = biljkaAdapter



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(
                    "--------------------------------------------------Suzuki",
                    " ${parent?.getItemAtPosition(position)} hOJHA"
                )
                val anesa = parent?.getItemAtPosition(position).toString()
                Log.d(
                    "--------------------------------------------------Suzuki",
                    " ${anesa} hehehehehe"
                )

                val newMode = when (anesa) {
                    "Medicinski" -> Mode.MEDICINSKI
                    "Kuharski" -> Mode.KUHARSKI
                    "Botanicki" -> Mode.BOTANICKI
                    else -> Mode.MEDICINSKI
                }
                Log.d(
                    "--------------------------------------------------Suzuki",
                    " ${anesa} omojbopze"
                )
                biljkaAdapter.updateMode(newMode)
                updateRecyclerView(null, newMode)


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


         fun getPlantsWithFlowerColor( flowerColor: String, substr: String) {
            CoroutineScope(Dispatchers.Main).launch {
                val plants = withContext(Dispatchers.IO) {
                    TrefleDAO().getPlantsWithFlowerColor(flowerColor, substr)
                }
                biljkaAdapter.submitList(plants)
            }
        }


        button.setOnClickListener {
            updateRecyclerView(null, trenutniMode)
        }



        brzaPretragaBtn.setOnClickListener {
            val pretraga = pretragaET.text.toString().trim()
            if (pretraga.isNotEmpty()) {
                val selectedColor = bojaSPIN.selectedItem.toString()
                getPlantsWithFlowerColor(selectedColor, pretraga)
            }
        }
    }



    private fun updateRecyclerView(referencePlant: Biljka?, mode: Mode) {

        Log.d("ThreadCheck", "STA SE OVDJE DESAVA ${mode}")

        currentReferencePlant = referencePlant ?: currentReferencePlant

        val sveBiljke = biljke + dodaneBiljke

        pretragaET.visibility = View.GONE
        bojaSPIN.visibility = View.GONE
        brzaPretragaBtn.visibility = View.GONE

        if (mode == Mode.BOTANICKI) {
            pretragaET.visibility = View.VISIBLE
            bojaSPIN.visibility = View.VISIBLE
            brzaPretragaBtn.visibility = View.VISIBLE
        }


        val filteredPlants: List<Biljka> = when (mode) {
            Mode.MEDICINSKI -> {
                if (referencePlant == null) {

                  sveBiljke
                } else {

                    filterByMedicinalUse(referencePlant, sveBiljke)
                }

            }

            Mode.KUHARSKI -> {
                if (referencePlant == null) {
                    sveBiljke
                } else {
                    filterByTasteOrDish(referencePlant, sveBiljke)
                }
            }

            Mode.BOTANICKI -> {
                if (referencePlant == null) {
                    sveBiljke
                } else {
                    filterByClimateAndSoil(referencePlant, sveBiljke)

                }
            }

        }

        trenutniMode = mode
        biljkaAdapter.updateMode(mode)
        biljkaAdapter.submitList(filteredPlants)




    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NOVA_BILJKA && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("nova_biljka")) {
                val novaBiljka = data.getParcelableExtra<Biljka>("nova_biljka")
                novaBiljka?.let {
                    // Save the new biljka to database

                    CoroutineScope(Dispatchers.Main).launch {

                        val fixedBiljka = withContext(Dispatchers.IO) {
                            TrefleDAO().fixData(it)
                        }
                        biljkaDAO.saveBiljka(fixedBiljka)

                        dodaneBiljke.add(fixedBiljka)
                        biljkaAdapter.submitList(biljke + dodaneBiljke)

                    }

                }
            } else {
                Log.e("NovaBiljkaActivity", "Nedostajući podaci u Intent objektu.")
            }
        }


    }


    private fun filterByMedicinalUse(referencePlant: Biljka, plants: List<Biljka>): List<Biljka> {

        val filteredPlants = plants.filter { plant ->
            val hasCommonMedicinalUse = haveCommonMedicinalUse(referencePlant, plant)
            println("Plant: ${plant.naziv}, Common Medicinal Use: $hasCommonMedicinalUse")
            hasCommonMedicinalUse
        }
        println("Filtered Plants: $filteredPlants")
        return filteredPlants
    }


    private fun haveCommonMedicinalUse(plant1: Biljka, plant2: Biljka): Boolean {
        val referenceMedicinalUses = plant1.medicinskeKoristi

        val plantMedicinalUses = plant2.medicinskeKoristi

        return referenceMedicinalUses.intersect(plantMedicinalUses).isNotEmpty()

    }


    private fun filterByTasteOrDish(referencePlant: Biljka, plants: List<Biljka>): List<Biljka> {

        val filteredPlants = plants.filter { plant ->
            val hasCommonTasteOrDish = haveCommonTasteOrDish(
                referencePlant,
                plant
            ) || referencePlant.jela.any { it in plant.jela }
            println("Plant: ${plant.naziv}, Common Taste or Dish: $hasCommonTasteOrDish")
            hasCommonTasteOrDish
        }
        println("Filtered Plants: $filteredPlants")
        return filteredPlants
    }

    private fun filterByClimateAndSoil(referencePlant: Biljka, plants: List<Biljka>): List<Biljka> {

        val filteredPlants = plants.filter { plant ->
            val hasCommonClimateAndSoil = haveCommonClimateAndSoil(referencePlant, plant)
            println("Plant: ${plant.naziv}, Common Climate and Soil: $hasCommonClimateAndSoil")
            hasCommonClimateAndSoil
        }
        println("Filtered Plants: $filteredPlants")
        return filteredPlants
    }


    private fun haveCommonTasteOrDish(plant1: Biljka, plant2: Biljka): Boolean {
        val commonTaste = plant1.profilOkusa == plant2.profilOkusa

        val commonDish = plant1.jela.intersect(plant2.jela).isNotEmpty()

        return commonTaste || commonDish


    }

    private fun haveCommonClimateAndSoil(plant1: Biljka, plant2: Biljka): Boolean {
        val sameFamily = plant1.porodica == plant2.porodica
        val commonSoilType = plant1.zemljisniTipovi.intersect(plant2.zemljisniTipovi).isNotEmpty()
        val commonClimateType =
            plant1.klimatskiTipovi.intersect(plant2.klimatskiTipovi).isNotEmpty()
        return sameFamily && commonSoilType && commonClimateType
    }
}

enum class Mode {
    MEDICINSKI,
    KUHARSKI,
    BOTANICKI
}