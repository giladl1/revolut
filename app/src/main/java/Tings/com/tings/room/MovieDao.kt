package Tings.com.tings.room

import Tings.com.tings.json.mov
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * from movie_table ORDER BY title ASC")
    fun getAllMovies():  MutableList<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOnlySingleMovie(myMov: Movie)



//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    abstract fun insert(movie: Movie)

//    @Query("DELETE  FROM movie_table")
//    abstract fun deleteAll()
}