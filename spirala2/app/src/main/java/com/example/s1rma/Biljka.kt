package com.example.s1rma

import android.os.Parcel
import android.os.Parcelable

data class Biljka (
    val naziv : String,
    val porodica : String,
    val medicinskoUpozorenje : String,
    val medicinskeKoristi : List<MedicinskaKorist>,
    val profilOkusa : ProfilOkusaBiljke,
    val jela : List<String>,
    val  klimatskiTipovi : List<KlimatskiTip>,
    val  zemljisniTipovi : List<Zemljiste>

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        mutableListOf<MedicinskaKorist>().apply {
            parcel.readList(this, MedicinskaKorist::class.java.classLoader)
        },
        parcel.readParcelable(ProfilOkusaBiljke::class.java.classLoader)!!,
        mutableListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        },
        mutableListOf<KlimatskiTip>().apply {
            parcel.readList(this, KlimatskiTip::class.java.classLoader)
        },
        mutableListOf<Zemljiste>().apply {
            parcel.readList(this, Zemljiste::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(naziv)
        parcel.writeString(porodica)
        parcel.writeString(medicinskoUpozorenje)
        parcel.writeList(medicinskeKoristi)
        parcel.writeParcelable(profilOkusa, flags)
        parcel.writeList(jela)
        parcel.writeList(klimatskiTipovi)
        parcel.writeList(zemljisniTipovi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Biljka> {
        override fun createFromParcel(parcel: Parcel): Biljka {
            return Biljka(parcel)
        }

        override fun newArray(size: Int): Array<Biljka?> {
            return arrayOfNulls(size)
        }
    }
}

enum class Zemljiste(val naziv: String) {
    PJESKOVITO("Pjeskovito zemljište"),
    GLINENO("Glinеno zemljište"),
    ILOVACA("Ilovača"),
    CRNICA("Crnica"),
    SLJUNKOVITO("Šljunovito zemljište"),
    KRECNJACKO("Krečnjačko zemljište");
}

enum class KlimatskiTip(val opis: String) {
    SREDOZEMNA("Mediteranska klima - suha, topla ljeta i blage zime"),
    TROPSKA("Tropska klima - topla i vlažna tokom cijele godine"),
    SUBTROPSKA("Subtropska klima - blage zime i topla do vruća ljeta"),
    UMJERENA("Umjerena klima - topla ljeta i hladne zime"),
    SUHA("Sušna klima - niske padavine i visoke temperature tokom cijele godine"),
    PLANINSKA("Planinska klima - hladne temperature i kratke sezone rasta"),
}

enum class MedicinskaKorist(val opis: String) {
    SMIRENJE("Smirenje - za smirenje i relaksaciju"),
    PROTUUPALNO("Protuupalno - za smanjenje upale"),
    PROTIVBOLOVA("Protivbolova - za smanjenje bolova"),
    REGULACIJAPRITISKA("Regulacija pritiska - za regulaciju visokog/niskog pritiska"),
    REGULACIJAPROBAVE("Regulacija probave"),
    PODRSKAIMUNITETU("Podrška imunitetu"),
}

enum class ProfilOkusaBiljke(val opis: String): Parcelable {
    MENTA("Mentol - osvježavajući, hladan ukus"),
    CITRUSNI("Citrusni - osvježavajući, aromatičan"),
    SLATKI("Sladak okus"),
    BEZUKUSNO("Obični biljni okus - travnat, zemljast ukus"),
    LJUTO("Ljuto ili papreno"),
    KORIJENASTO("Korenast - drvenast i gorak ukus"),
    AROMATICNO("Začinski - topli i aromatičan ukus"),
    GORKO("Gorak okus");

    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(opis) // Write enum constant name with spaces directly
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfilOkusaBiljke> {
        override fun createFromParcel(parcel: Parcel): ProfilOkusaBiljke {
            // Read enum constant name and replace underscores with spaces
            val opis = parcel.readString()!!.replace("_", " ")
            return values().find { it.opis == opis } ?: throw IllegalArgumentException("Enum constant not found")
        }

        override fun newArray(size: Int): Array<ProfilOkusaBiljke?> {
            return arrayOfNulls(size)
        }
    }
}
