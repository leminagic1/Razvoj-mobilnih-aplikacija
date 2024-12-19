package com.example.s1rma

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

import android.util.Base64

class Konvertori {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(encodedString: String?): Bitmap? {
        if (encodedString == null) return null
        val decodedBytes = android.util.Base64.decode(encodedString, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromMedicinskaKoristList(value: List<MedicinskaKorist>): String {
        return value.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toMedicinskaKoristList(value: String): List<MedicinskaKorist> {
        return value.split(",").map { MedicinskaKorist.valueOf(it) }
    }

    @TypeConverter
    fun fromProfilOkusaBiljke(value: ProfilOkusaBiljke): String {
        return value.name
    }

    @TypeConverter
    fun toProfilOkusaBiljke(value: String): ProfilOkusaBiljke {
        return ProfilOkusaBiljke.valueOf(value)
    }

    @TypeConverter
    fun fromKlimatskiTipList(value: List<KlimatskiTip>): String {
        return value.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toKlimatskiTipList(value: String): List<KlimatskiTip> {
        return value.split(",").map { KlimatskiTip.valueOf(it) }
    }

    @TypeConverter
    fun fromZemljisteList(value: List<Zemljiste>): String {
        return value.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toZemljisteList(zemljisteString: String): List<Zemljiste> {
        return zemljisteString.split(",").mapNotNull {
            try {
                Zemljiste.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null  // Ili možete prijaviti grešku ili izbaciti Exception
            }
        }
    }
}