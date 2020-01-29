package Tings.com.tings

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
import android.provider.Telephony
import android.util.Log
import androidx.room.Room
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

import kotlinx.android.synthetic.main.activity_get_json.*
import kotlinx.coroutines.*
import java.util.*


class MyMoviesActivity : AppCompatActivity() {
    val MY_PERMISSIONS_REQUEST_INTERNET=1
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var URL:String
    private lateinit var ratesList: MutableList<SpecificCurrency>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movies)
        setSupportActionBar(toolbar1)
        URL="http://revolut.duckdns.org/latest?base=EUR"
        getJsonFromUrl()
//        URL ="http://api.androidhive.info/json/movies.json"
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

//        GlobalScope.launch {
//            // we'll get the movies and genres and then transform them into mov class for the adapter
//            data = movieDatabase.movieDao().getAllMovies()//getting all movies from room db
//            var dataWithGenres:MutableList<mov> = arrayListOf()
//            data?.forEachIndexed { index, movie ->
//                //list of Genres for current movie
//                var curMovieGenresList=movieDatabase.genreDao().getGenres(movie.title)
//                //list of Genres as strings for current movie
//                var genresStrings:MutableList<String> = getGenresStrings(curMovieGenresList)
//                //mov with genres as a list of strings
//                var movWithGenre:mov=mov(movie.title,movie.image,movie.rating,movie.releaseYear,genresStrings)
//                //list of movs-we add current mov to it
//                dataWithGenres.add(index,movWithGenre)
//                println(movie)
//            }
//            operateAdapter( dataWithGenres)//data
//        }

        }

    private fun getJsonFromUrl() {
        try {

            Fuel.post(URL, listOf()).responseJson { request, response, result ->
                Log.d("plzzzzz", result.get().content)
                toGson(result.get().content)
//                onTaskCompleted(result.get().content)
            }
        } catch (e: Exception) {

        } finally {

        }
    }
    private fun toGson(json:String){
        val gson: Gson = Gson()
        val currencyResults: Currency =gson.fromJson(json, Currency::class.java)
//        val curcur: Rates=gson.fromJson(json.begin)
        //
        ratesList= arrayListOf()
        var bundle:Bundle
        var currencyName:String
        var currencyValue:Double?

        currencyName="aud"
        currencyValue= currencyResults.rates?.AUD
        createRatesList(currencyName,currencyValue)
        currencyName="bgn"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="brl"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="cad"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="chf"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="CNY"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="CZK"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="DKK"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="GBP"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="HKD"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="HRK"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="HUF"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="IDR"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="ILS"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="INR"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="ISK"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="JPY"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="KRW"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="MSN"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="KRW"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="MXN"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="MYR"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="NOK"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="NZD"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="PHP"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="RON"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="RUB"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="SEK"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="SGD"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="THB"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="TRY"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="USD"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="ZAR"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        //
        operateAdapter(ratesList )
    }
    private fun createRatesList(currencyName:String,currencyValue:Double?){
        val specificCurrency:SpecificCurrency=SpecificCurrency(currencyName,currencyValue)

        ratesList.add(specificCurrency)
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
    fun operateAdapter(currency: MutableList<SpecificCurrency>){//movies:MutableList<mov>){

        viewManager = LinearLayoutManager(this)
        viewAdapter = RecyclerAdapter(currency)//movies)//, paymentIds )//dataset

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
data class Currency(

        val base: String?=null,
        val date: String?=null,
        @SerializedName("rates")
        val rates: Rates?=null  //MutableList<Rates>


)

data class Rates (

        val AUD : Double,
        val BGN : Double,
        val BRL : Double,
        val CAD : Double,
        val CHF : Double,
        val CNY : Double,
        val CZK : Double,
        val DKK : Double,
        val GBP : Double,
        val HKD : Double,
        val HRK : Double,
        val HUF : Double,
        val IDR : Int,
        val ILS : Double,
        val INR : Double,
        val ISK : Double,
        val JPY : Double,
        val KRW : Double,
        val MXN : Double,
        val MYR : Double,
        val NOK : Double,
        val NZD : Double,
        val PHP : Double,
        val PLN : Double,
        val RON : Double,
        val RUB : Double,
        val SEK : Double,
        val SGD : Double,
        val THB : Double,
        val TRY : Double,
        val USD : Double,
        val ZAR : Double
)


