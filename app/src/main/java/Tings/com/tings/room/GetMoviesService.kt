package Tings.com.tings.room

import Tings.com.tings.json.mov
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.room.Room
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class GetMoviesService : Service() {
    private val myBinder = MyLocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return myBinder
        TODO("Return the communication channel to the service.")
    }
    fun getCurrentTime(): String {
        val dateformat = SimpleDateFormat("HH:mm:ss MM/dd/yyyy",
                Locale.US)
        return dateformat.format(Date())
    }
    fun getMovies(): MutableList<Movie>? = runBlocking{
        var movies: MutableList<Movie>? = null

            var movieDatabase: MovieRoomDatabase? = Room.databaseBuilder(applicationContext,
                    MovieRoomDatabase::class.java, "DATABASE_NAME")
                    .fallbackToDestructiveMigration()
                    .build()

            movies = movieDatabase!!.movieDao().getAllMovies()

        movies
    }
//        var background = object : Thread(){
//            override fun run() {
//
//            }
//        }.start()



    inner class MyLocalBinder : Binder() {
        fun getService() : GetMoviesService {
            return this@GetMoviesService
        }
    }
}
