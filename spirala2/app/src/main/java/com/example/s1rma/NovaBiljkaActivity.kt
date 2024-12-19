package com.example.s1rma

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NovaBiljkaActivity : AppCompatActivity() {

    private var our_request_code: Int = 123

    private lateinit var nazivET: EditText
    private lateinit var porodicaET: EditText
    private lateinit var medicinskoUpozorenjeET: EditText
    private lateinit var medicinskaKoristLV: ListView
    private lateinit var klimatskiTipLV: ListView
    private lateinit var zemljisniTipLV: ListView
    private lateinit var profilOkusaLV: ListView
    private lateinit var jeloET: EditText
    private lateinit var jelaLV: ListView
    private lateinit var dodajJeloBtn: Button
    private lateinit var dodajBiljkuBtn: Button
    private lateinit var uslikajBiljkuBtn: Button
    private lateinit var slikaIV: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_biljka)






        nazivET = findViewById(R.id.nazivET)
        porodicaET = findViewById(R.id.porodicaET)
        medicinskoUpozorenjeET = findViewById(R.id.medicinskoUpozorenjeET)
        jeloET = findViewById(R.id.jeloET)
        medicinskaKoristLV = findViewById(R.id.medicinskaKoristLV)
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)
        profilOkusaLV = findViewById(R.id.profilOkusaLV)
        jelaLV = findViewById(R.id.jelaLV)
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)
        dodajBiljkuBtn = findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)
        slikaIV = findViewById(R.id.slikaIV)


        val medicinskaKoristAdapter =
            MedicinskaKoristAdapter(this, MedicinskaKorist.values().toList())
        medicinskaKoristLV.adapter = medicinskaKoristAdapter

        val klimatskiTipAdapter = KlimatskiTipAdapter(this, KlimatskiTip.values().toList())
        klimatskiTipLV.adapter = klimatskiTipAdapter


        val zemljisniTipAdapter = ZemljisteAdapter(this, Zemljiste.values().toList())
        zemljisniTipLV.adapter = zemljisniTipAdapter

        val profilOkusaAdapter = ProfilOkusaAdapter(this, ProfilOkusaBiljke.values())
        profilOkusaLV.adapter = profilOkusaAdapter


        val jelaAdapter = JelaAdapter(this)
        jelaLV.adapter = jelaAdapter


        dodajJeloBtn.setOnClickListener {
            val novoJelo = jeloET.text.toString()
            if (novoJelo.isNotEmpty()) {
                val odabranoJelo = dodajJeloBtn.tag as? String
                if (odabranoJelo != null) {
                    jelaAdapter.izmijeniJelo(odabranoJelo, novoJelo)
                    jeloET.text.clear()
                    dodajJeloBtn.text = "Dodaj jelo"
                    dodajJeloBtn.tag = null
                } else {
                    if (validateJelo(jelaAdapter, novoJelo)) {
                        jelaAdapter.dodajJelo(novoJelo)
                        jeloET.text.clear()
                    }
                }
            } else {
                val odabranoJelo = dodajJeloBtn.tag as? String
                if (odabranoJelo != null) {
                    jelaAdapter.ukloniJelo(odabranoJelo)
                }
                jeloET.text.clear()
                dodajJeloBtn.text = "Dodaj jelo"
                dodajJeloBtn.tag = null
            }
        }


        jelaLV.setOnItemClickListener { parent, view, position, id ->
            val odabranoJelo = jelaAdapter.getItem(position) as String
            jeloET.setText(odabranoJelo)
            dodajJeloBtn.text = "Izmijeni jelo"

            dodajJeloBtn.tag = odabranoJelo
        }

        fun validateInput(
            naziv: String,
            porodica: String,
            medicinskoUpozorenje: String,
            medicinskaKorist: List<MedicinskaKorist>,
            profilOkusa: ProfilOkusaBiljke?,
            jela: List<String>,
            klimatskiTipovi: List<KlimatskiTip>,
            zemljisniTipovi: List<Zemljiste>
        ): Boolean {
            val isNazivValid = naziv.length in 2..20
            val isPorodicaValid = porodica.length in 2..20
            val isMedicinskoUpozorenjeValid = medicinskoUpozorenje.length in 2..20
            val isJelaValid = jela.isNotEmpty() && jela.map { it.toLowerCase() }
                .distinct().size == jela.size && jela.all { it.length in 2..20 }
            val isProfilOkusaValid = profilOkusa != null
            val isMedicinskaKoristValid = medicinskaKorist.isNotEmpty()
            val isKlimatskiTipValid = klimatskiTipovi.isNotEmpty()
            val isZemljisniTipValid = zemljisniTipovi.isNotEmpty()


           if (!isNazivValid) nazivET.setError("Naziv mora imati između 2 i 20 znakova.")
           if (!isPorodicaValid) porodicaET.setError("Porodica mora imati između 2 i 20 znakova.")
            if (!isJelaValid) jeloET.setError("Svako jelo mora biti jedinstveno i imati između 2 i 20 znakova.")
            if (!isMedicinskoUpozorenjeValid) medicinskoUpozorenjeET.setError("Medicinsko upozorenje mora imati između 3 i 19 znakova.")
            if (!isProfilOkusaValid) Toast.makeText(
                this,
                "Morate odabrati profil okusa.",
                Toast.LENGTH_SHORT
            ).show()
            if (!isMedicinskaKoristValid) Toast.makeText(
                this,
                "Morate odabrati medicinsku korist.",
                Toast.LENGTH_SHORT
            ).show()
            if (!isKlimatskiTipValid) Toast.makeText(
                this,
                "Morate odabrati barem jedan klimatski tip.",
                Toast.LENGTH_SHORT
            ).show()
            if (!isZemljisniTipValid) Toast.makeText(
                this,
                "Morate odabrati barem jedan zemljisni tip.",
                Toast.LENGTH_SHORT
            ).show()

            return isNazivValid && isPorodicaValid && isMedicinskoUpozorenjeValid && isJelaValid && isProfilOkusaValid && isMedicinskaKoristValid && isKlimatskiTipValid && isZemljisniTipValid
        }


        dodajBiljkuBtn.setOnClickListener {

            val naziv = nazivET.text.toString()
            val porodica = porodicaET.text.toString()
            val medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString()

            val medicinskaKoristSet =
                (medicinskaKoristLV.adapter as MedicinskaKoristAdapter).getSelectedItems()
            val klimatskiTipSet = (klimatskiTipLV.adapter as KlimatskiTipAdapter).getSelectedItems()
            val zemljisniTipSet = (zemljisniTipLV.adapter as ZemljisteAdapter).getSelectedItems()
            val profilOkusa = (profilOkusaLV.adapter as ProfilOkusaAdapter).getSelectedOption()
            val jela = (jelaLV.adapter as JelaAdapter).getAllJela()

            val medicinskaKorist = medicinskaKoristSet.toList()
            val klimatskiTip = klimatskiTipSet.toList()
            val zemljisniTip = zemljisniTipSet.toList()


            if (validateInput(
                    naziv,
                    porodica,
                    medicinskoUpozorenje,
                    medicinskaKorist,
                    profilOkusa,
                    jela,
                    klimatskiTip,
                    zemljisniTip
                )
            ) {
                val novaBiljka = Biljka(
                    naziv, porodica, medicinskoUpozorenje, medicinskaKorist.toList(),
                    profilOkusa!!, jela, klimatskiTip.toList(), zemljisniTip.toList()
                )
                val intent = Intent()
                intent.putExtra("nova_biljka", novaBiljka)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Niste ispravno popunili sva polja.", Toast.LENGTH_SHORT)
                    .show()
            }
        }





        uslikajBiljkuBtn.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                startActivityForResult(takePictureIntent, our_request_code)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "Nije pronadjena kamera" + e.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }



    private fun validateJelo(jelaAdapter: JelaAdapter, novoJelo: String): Boolean {
        val normaliziranoJelo = novoJelo.toLowerCase()

        return if (normaliziranoJelo.length in 2..20) {
            val svaJela = jelaAdapter.getAllJela()
                .map { it.toLowerCase() }

            if (!svaJela.contains(normaliziranoJelo)) {
                true
            } else {
                jeloET.setError("Jelo već postoji u listi.")
                false
            }
        } else {
            jeloET.setError("Svako jelo mora biti između 2 i 20 znakova.")
            false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == our_request_code && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
          slikaIV.setImageBitmap(imageBitmap)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
    fun getJelaListView(): ListView {
        return jelaLV
    }
}
