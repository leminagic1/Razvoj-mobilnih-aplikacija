package com.example.s1rma

import android.util.Log

object BiljkaManager {
    private var listaBiljaka = mutableListOf<Biljka>()

    fun dodajBiljku(biljka: Biljka) {
        listaBiljaka.add(biljka)
        Log.d("ThreadCheck", "JEL DODANA BILJKA $biljka")
    }

    fun getListaBiljaka(): List<Biljka> {
        return listaBiljaka
    }

    fun clearList() {
        listaBiljaka.clear()
    }
}
