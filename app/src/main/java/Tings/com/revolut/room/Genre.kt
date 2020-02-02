package Tings.com.revolut.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "genre_table",primaryKeys = arrayOf("title", "num"))
class Genre(
        @ColumnInfo(name="title") val title: String,
        @ColumnInfo (name="num") val num: Int,
        @ColumnInfo val genre: String
)