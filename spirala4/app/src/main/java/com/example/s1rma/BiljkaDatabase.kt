package com.example.s1rma

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.room.Update


@Database(entities = [Biljka::class, BiljkaBitmap::class], version = 1)
@TypeConverters(Konvertori::class)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO

    companion object {
        private var INSTANCE: BiljkaDatabase? = null

        fun getInstance(context: Context): BiljkaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BiljkaDatabase::class.java,
                    "biljke-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
@Dao
interface BiljkaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBiljka(biljka: Biljka): Long

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        val biljke = getOfflineBiljkas()
        var updatedCount = 0
        for (biljka in biljke) {
            val fixedBiljka = TrefleDAO().fixData(biljka)
            if (biljka != fixedBiljka) {
                updateBiljka(fixedBiljka)
                updatedCount++
            }
        }
        return updatedCount
    }


    @Query("SELECT * FROM biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Query("DELETE FROM biljka")
    suspend fun clearBiljkas()


    @Query("SELECT * FROM Biljka WHERE id = :id")
   suspend fun getBiljkaById(id: Int): Biljka?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertBiljkaBitmap(biljkaBitmap: BiljkaBitmap): Long

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljka")
   suspend fun getBiljkaBitmapById(idBiljka: Int): BiljkaBitmap?

    @Transaction
   suspend fun addImage(idBiljke: Int, bitmap: Bitmap): Boolean {
val getBitmap = getBiljkaBitmapById(idBiljke)
        return if(getBitmap==null){
            val biljkaBitmap = BiljkaBitmap(idBiljka = idBiljke, bitmap = bitmap)
            val id=insertBiljkaBitmap(biljkaBitmap)
            id!=-1L
        }else false
    }


    @Query("DELETE FROM biljkaBitmap")
    suspend fun clearBiljkaBitmaps()

    @Query("SELECT * FROM biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljkas(): List<Biljka>

    @Update
    suspend fun updateBiljka(biljka: Biljka)


    @Transaction
    suspend fun clearData() {
        clearBiljkas()
        clearBiljkaBitmaps()
    }
}
