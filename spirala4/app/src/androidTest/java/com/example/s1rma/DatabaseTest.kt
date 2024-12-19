package com.example.s1rma
import android.content.Context
import android.graphics.Bitmap
import androidx.core.database.getStringOrNull
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {



    companion object {
        lateinit var db: SupportSQLiteDatabase
        lateinit var context: Context
        lateinit var roomDb: BiljkaDatabase
        lateinit var biljkaDAO: BiljkaDAO

        @BeforeClass
        @JvmStatic
        fun createDB() = runBlocking {
            context = ApplicationProvider.getApplicationContext()
            roomDb = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
            biljkaDAO = roomDb.biljkaDao()


            db = roomDb.openHelper.readableDatabase
        }

        @AfterClass
        @JvmStatic
        fun closeDB() {
            roomDb.close()
        }
    }

    @Before
    fun setUp() {
        runBlocking {
            biljkaDAO.clearData()
        }
    }
    @Test
    fun dodajBiljkuUBazu() = runBlocking {

        val biljka = Biljka(
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
            )



        biljkaDAO.saveBiljka(biljka)


        val biljkasFromDb = biljkaDAO.getAllBiljkas()


        assertTrue(biljkasFromDb.isNotEmpty())
        assertThat(biljkasFromDb[0].naziv, `is`(equalTo("Bosiljak (Ocimum basilicum)")))
        assertThat(biljkasFromDb[0].porodica, `is`(equalTo("Lamiaceae (usnate)")))
        assertThat(biljkasFromDb[0].medicinskoUpozorenje, `is`(equalTo("Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.")))
}

    @Test
    fun provjeraPraznjenjaBaze() = runBlocking {
        val biljka = Biljka(
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
        )

        biljkaDAO.saveBiljka(biljka)


        biljkaDAO.clearData()

        val biljkasFromDb = biljkaDAO.getAllBiljkas()

        assertTrue(biljkasFromDb.isEmpty())
    }}