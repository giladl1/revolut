package Tings.com.revolut.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {
    @Query("SELECT * from genre_table Where title= :title ")
    fun getGenres(title: String): MutableList<Genre>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertSingleGenre(myGenre: Genre)

}