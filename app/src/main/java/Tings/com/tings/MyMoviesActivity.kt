package Tings.com.tings

import Tings.com.tings.json.mov
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import layout.RecyclerAdapter
import Tings.com.tings.room.*
import androidx.room.Room

import kotlinx.android.synthetic.main.activity_get_json.*
import kotlinx.coroutines.*


class MyMoviesActivity : AppCompatActivity() {
    val MY_PERMISSIONS_REQUEST_INTERNET=1
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var URL:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movies)
        setSupportActionBar(toolbar1)

        URL ="http://api.androidhive.info/json/movies.json"
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            askPermission()
        }
        else askPermission();

        var movieDatabase:MovieRoomDatabase=
                Room.databaseBuilder(this,
                        MovieRoomDatabase::class.java, "movies")
                        .fallbackToDestructiveMigration().allowMainThreadQueries()
                        .build()
        var data= mutableListOf<Movie>()

        GlobalScope.launch {
            // we'll get the movies and genres and then transform them into mov class for the adapter
            data = movieDatabase.movieDao().getAllMovies()//getting all movies from room db
            var dataWithGenres:MutableList<mov> = arrayListOf()
            data?.forEachIndexed { index, movie ->
                //list of Genres for current movie
                var curMovieGenresList=movieDatabase.genreDao().getGenres(movie.title)
                //list of Genres as strings for current movie
                var genresStrings:MutableList<String> = getGenresStrings(curMovieGenresList)
                //mov with genres as a list of strings
                var movWithGenre:mov=mov(movie.title,movie.image,movie.rating,movie.releaseYear,genresStrings)
                //list of movs-we add current mov to it
                dataWithGenres.add(index,movWithGenre)
                println(movie)
            }
            operateAdapter( dataWithGenres)//data
        }

        }


    private fun getGenresStrings(myGenres:MutableList<Genre>): MutableList<String> {
        var currentGenresStrings: MutableList<String> = arrayListOf()
        myGenres?.forEachIndexed { index, genre ->

                currentGenresStrings.add(index, myGenres[index].genre)

        }
        return currentGenresStrings
    }

    fun askPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.INTERNET),
                        MY_PERMISSIONS_REQUEST_INTERNET)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }
    /***************************************************************
    //here the recycler adapter is attached to the data and activity:
    **************************************************************/
    fun operateAdapter(movies:MutableList<mov>){

        viewManager = LinearLayoutManager(this)
        viewAdapter = RecyclerAdapter(movies)//, paymentIds )//dataset

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
}


