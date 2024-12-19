package com.example.s1rma

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: Button

    private var trenutniMode: Mode = Mode.MEDICINSKI


    private lateinit var currentReferencePlant: Biljka

    private val biljke = listOf(
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
            jela = listOf("Tom Yum supa", "Marinada od limun trave za piletinu","Sos"),
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


    private val biljkaAdapter = BiljkaAdapter(trenutniMode)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.modSpinner)
        recyclerView = findViewById(R.id.biljkeRV)
        button = findViewById(R.id.resetBtn)
        currentReferencePlant = biljke.firstOrNull() ?: return



        biljkaAdapter.setOnItemClickListener { biljka ->
            // currentReferencePlant = biljka


            biljkaAdapter.setCurrentReferencePlant(biljka)
            updateRecyclerView(biljka, trenutniMode)
            // updateRecyclerView(biljka, trenutniMode)
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


        button.setOnClickListener {


            updateRecyclerView(null, trenutniMode)



        }

        // updateRecyclerView(null, trenutniMode)
    }


    private fun updateRecyclerView(referencePlant: Biljka?, mode: Mode) {


        Log.d("ThreadCheck", "STA SE OVDJE DESAVA ${mode}")



        currentReferencePlant = referencePlant?: currentReferencePlant




        val filteredPlants: List<Biljka> = when (mode) {
            Mode.MEDICINSKI -> {
                if (referencePlant == null) {

               biljke
                }else {

                    filterByMedicinalUse(referencePlant, biljke)
                }

            }

            Mode.KUHARSKI -> {
                if (referencePlant == null) {
biljke


                } else {
                    filterByTasteOrDish(referencePlant, biljke)
                }
            }

            Mode.BOTANICKI -> {
                if (referencePlant == null) {
                  biljke



                } else {
                    filterByClimateAndSoil(referencePlant, biljke)

                }
            }

        }
        trenutniMode=mode

        biljkaAdapter.updateMode(mode)
        biljkaAdapter.submitList(filteredPlants)


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
        val hasCommonTasteOrDish = haveCommonTasteOrDish(referencePlant, plant) || referencePlant.jela.any { it in plant.jela }
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


enum class Mode {
    MEDICINSKI,
    KUHARSKI,
    BOTANICKI
}