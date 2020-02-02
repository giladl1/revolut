package Tings.com.revolut.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
class Movie(
        @PrimaryKey @ColumnInfo(name="title") val title: String,
        @ColumnInfo  val image: String,
        @ColumnInfo  val rating: Double,
        @ColumnInfo  val releaseYear: Int
)