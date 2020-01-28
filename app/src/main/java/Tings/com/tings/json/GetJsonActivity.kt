package Tings.com.tings.json
import Tings.com.tings.MyMoviesActivity
import Tings.com.tings.R
import Tings.com.tings.room.MovieRoomDatabase
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_get_json.*
import org.json.JSONObject
import java.io.StringReader


class GetJsonActivity : AppCompatActivity() {
    lateinit var URL:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_json)
        setSupportActionBar(toolbar1)
        URL="http://revolut.duckdns.org/latest?base=EUR"
//        URL ="http://api.androidhive.info/json/movies.json"
        getJsonFromUrl()


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
        val gson:Gson= Gson()
        val currencyResults: currency=gson.fromJson(json,currency::class.java)
    }
    private fun onTaskCompleted(json: String) {
        println(json)

//        val result = Klaxon()
//                .parse<currency>(json);


//        val result = Klaxon()
//                .parse<Movies>("""
//
//                {
//                    "title" : "title"
//                    "image" : "wwww.sss"
//                    "rating" : 88
//                    "name" : "John Smith",
//                }
//        """)
        //here we parse the json into movies and then into an array of movies
        val klaxon = Klaxon()
        val currencyArray = arrayListOf<currency>()
        JsonReader(StringReader(json)).use {//jsonArray
            reader -> reader.beginObject { // } beginArray {

//            val name2=reader.nextName()
//            Log.v("yes",name2)
//            val name=reader.nextName()
            while (reader.hasNext()) {
                val curCurrency = klaxon.parse<currency>(reader)
                currencyArray.add(curCurrency!!)
            }
        }
        }
        Log.v("klaxon","passed")
        if(currencyArray==null || currencyArray.size==0)
            Log.v("moviesArr"," is null")
//        insertMoviesToRoom(moviesArray)


//        Log.v("now","the time")
//        println("rasa is : "+result?.rating.toString())


//        var data= mutableListOf<Movies>()//todo transform from json to room

//        println("res is: " + (result?.get(0)?.image ))

    }
    //here we will insert the parsed json into the room db:
    fun insertMoviesToRoom( jsonMovies:List<mov>){
//        GlobalScope.launch {
        var movieDatabase: MovieRoomDatabase =
                Room.databaseBuilder(applicationContext,
                        MovieRoomDatabase::class.java, "movies")
                        .fallbackToDestructiveMigration().allowMainThreadQueries()
                        .build()
        Log.v("insertMoviesToRoom","passed")


            jsonMovies?.forEach {
                Log.v("before println it","passed")
//                println(it)
                var myMovie= Tings.com.tings.room.Movie(it.title,it.image,it.rating,it.releaseYear)//Movies()
                Log.v("before it title","passed")

                var myGenres=it.genre
                myGenres?.forEachIndexed{index,it_g->
                    var singleGenre=Tings.com.tings.room.Genre(myMovie.title,index,it_g)
                    movieDatabase.genreDao().insertSingleGenre(singleGenre)
                }

                movieDatabase.movieDao().insertOnlySingleMovie(myMovie)//todo one movie here

                Log.v("insertToRoom",it.title)
//                insertGenresToRoom(title,List<Genres>)
                }
//        }
        val intent = Intent(this, MyMoviesActivity::class.java)
        startActivity(intent)
    }

}
//we use this class to parse the json to seperate movies:
//class movie(val title:String,val image:String,val rating:Double,val releaseYear:Int,val genre:List<String>)

@Entity(tableName = "movie_table")
data class mov(
        @PrimaryKey @ColumnInfo(name="title") val title: String,
        @ColumnInfo val image: String,
        @ColumnInfo val rating: Double,
        @ColumnInfo val releaseYear: Int,
        @ColumnInfo val genre: MutableList<String>
)
data class currencyList(
        val results: List<currency?>? =null
)
//@Entity(tableName = "currency_table")
data class currency(

         val base: String?=null,
         val date: String?=null,
         @SerializedName("rates")
         val rates: Rates?=null  //MutableList<Rates>


)
//        @PrimaryKey @ColumnInfo(name="title") val title: String,
//        @ColumnInfo val image: String,
//        @ColumnInfo val rating: Double,
//        @ColumnInfo val releaseYear: Int,
//        @ColumnInfo val genre: MutableList<String>


//data class Rates(
//        @PrimaryKey @ColumnInfo(name="title") val country: String,
//        @ColumnInfo val rate: String
//)

data class Rates (

         val aUD : Double,
         val bGN : Double,
         val bRL : Double,
         val cAD : Double,
         val cHF : Double,
         val cNY : Double,
         val cZK : Double,
         val dKK : Double,
         val gBP : Double,
         val hKD : Double,
         val hRK : Double,
         val hUF : Double,
         val iDR : Int,
         val iLS : Double,
         val iNR : Double,
         val iSK : Double,
         val jPY : Double,
         val kRW : Double,
         val mXN : Double,
         val mYR : Double,
         val nOK : Double,
         val nZD : Double,
         val pHP : Double,
         val pLN : Double,
         val rON : Double,
         val rUB : Double,
         val sEK : Double,
         val sGD : Double,
         val tHB : Double,
         val tRY : Double,
         val uSD : Double,
         val zAR : Double
)



