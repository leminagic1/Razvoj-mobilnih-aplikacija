package com.example.s1rma

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(
    tableName="BiljkaBitmap",
    foreignKeys = [ForeignKey(
        entity = Biljka::class,
        parentColumns = ["id"],
        childColumns = ["idBiljke"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value=["idBiljke"])]
)
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) var id:Int=0,
    @ColumnInfo(name="idBiljke") val idBiljka: Int,
    @ColumnInfo(name="bitmap")
    @TypeConverters(Konvertori::class)val bitmap: Bitmap

)
