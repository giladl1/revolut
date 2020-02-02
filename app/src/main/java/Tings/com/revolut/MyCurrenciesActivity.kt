package Tings.com.revolut

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_get_json.*
import kotlinx.android.synthetic.main.content_my_movies.*
import layout.RecyclerAdapter
import java.util.*
import kotlin.concurrent.schedule


class MyCurrenciesActivity : AppCompatActivity() {
    val MY_PERMISSIONS_REQUEST_INTERNET = 1
    //    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var URL: String
    private lateinit var ratesList: MutableList<SpecificCurrency>
    private lateinit var currencyDetailsMap: MutableMap<String, CurrencyDetails>
    private lateinit var formerJson: String // saves the last retrieved json
    //sharedpref definitions:
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "lastScrollTime"
    private lateinit var sharedPref: SharedPreferences
    private lateinit var timer:Timer// to operate the data update every 1 second
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movies)
        setSupportActionBar(toolbar1)
        initializeScrollListener()
        initializeSharedPreferences()
        currencyDetailsMap = mutableMapOf<String, CurrencyDetails>()
        getInternetPermission()
        URL = "http://revolut.duckdns.org/latest?base=EUR"
        getDetailsJsonFromUrl()//get data of countries


    }

    override fun onStart() {
        super.onStart()
        getJsonFromUrl()//get revolut data
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    //*********************************************************************
    //****will help us know when we shouldn't update the recyclerview data
    //*********************************************************************
    private fun initializeScrollListener() {
        recyclerView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                val lastScrollTime = System.currentTimeMillis()
                val editor = sharedPref.edit()
                editor.putLong(PREF_NAME, lastScrollTime) //putBoolean(PREF_NAME, true)
                editor.apply()
                //To change body of created functions use File | Settings | File Templates.
            }
        })
        formerJson = " "
    }
    //*********************************************************************
    //****will help us know when user scrolled the recyclerview
    //*********************************************************************
    private fun initializeSharedPreferences() {
        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.putLong(PREF_NAME, 0)
        editor.apply()
    }
    //*********************************************************************
    //****we must get user permission to access internet
    //*********************************************************************
    private fun getInternetPermission(){
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
        != PackageManager.PERMISSION_GRANTED) {
        // Permission is not granted
        askPermission()
    }
    else askPermission()
    }
    //*********************************************************************
    //****get data from revolut server
    //*********************************************************************
    private fun getJsonFromUrl() {
        try {
            timer=Timer("jsonReading", false)
            //get the data every one second unless user touched the recyclerview:
            val timerTask:TimerTask=timer.schedule(0,1000) {
                Fuel.post(URL, listOf()).responseJson { request, response, result ->
                    val currentJson:String=result.get().content
                    val lastScrollTime=sharedPref.getLong(PREF_NAME,0)
                    val now= System.currentTimeMillis()
                    val secondsSinceLastScroll: Long =(now-lastScrollTime)/1000
                    //no need to update data if user is touching the recyclerview or if data is updated:
                    if((!currentJson.equals(formerJson)) && secondsSinceLastScroll>6) {
                        formerJson = currentJson
                        toGson(currentJson)
                    }
                }
            }//end Timer

            } catch (e: Exception) {

            } finally {

            }

    }
    //*********************************************************************
    //****get countries data from server
    //*********************************************************************
    private fun getDetailsJsonFromUrl() {
        try {

            val DETAILSURL:String="https://restcountries.eu/rest/v2/all?fields=flag;currencies;alpha2Code"//http://www.geognos.com/api/en/countries/info/all.json"//http://restcountries.eu/rest/v2/alpha/col"//https://restcountries.eu/rest/v2/all"//?fields=flag;currencies
            Fuel.get(DETAILSURL, listOf()).responseJson { request, response, result ->
                toGsonDetails(result.get().content)
            }
        } catch (e: Exception) {

        } finally {

        }
    }
    //*********************************************************************
    //****get countries data into json
    //*********************************************************************
    private fun toGsonDetails(json:String) {
        val gson: Gson = Gson()
        val currencyDetailsResults = gson.fromJson(json, Array<CurrencyDetails>::class.java).toList()
        currencyDetailsResults.forEachIndexed { index, it ->
        val code = currencyDetailsResults.get(index).currencies[0].code
        currencyDetailsMap.put(code, currencyDetailsResults[index])
        }
    }
    //****************************************************************************************
    //****get revolut data into json,and create list of currency values for the recyclerview:
    //****************************************************************************************
    private fun toGson(json:String){
        val gson: Gson = Gson()
        val currencyResults: Currency =gson.fromJson(json, Currency::class.java)
        ratesList= arrayListOf()
        var currencyName:String
        var currencyValue:Double?
        currencyName="EUR"
        currencyValue= 1.0
        createRatesList(currencyName,currencyValue)
        currencyName="AUD"
        currencyValue= currencyResults.rates?.AUD
        createRatesList(currencyName,currencyValue)
        currencyName="BGN"
        currencyValue= currencyResults.rates?.BGN
        createRatesList(currencyName,currencyValue)
        currencyName="BRL"
        currencyValue= currencyResults.rates?.BRL
        createRatesList(currencyName,currencyValue)
        currencyName="CAD"
        currencyValue= currencyResults.rates?.CAD
        createRatesList(currencyName,currencyValue)
        currencyName="CHF"
        currencyValue= currencyResults.rates?.CHF
        createRatesList(currencyName,currencyValue)
        currencyName="CNY"
        currencyValue= currencyResults.rates?.CNY
        createRatesList(currencyName,currencyValue)
        currencyName="CZK"
        currencyValue= currencyResults.rates?.CZK
        createRatesList(currencyName,currencyValue)
        currencyName="DKK"
        currencyValue= currencyResults.rates?.DKK
        createRatesList(currencyName,currencyValue)
        currencyName="GBP"
        currencyValue= currencyResults.rates?.GBP
        createRatesList(currencyName,currencyValue)
        currencyName="HKD"
        currencyValue= currencyResults.rates?.HKD
        createRatesList(currencyName,currencyValue)
        currencyName="HRK"
        currencyValue= currencyResults.rates?.HRK
        createRatesList(currencyName,currencyValue)
        currencyName="HUF"
        currencyValue= currencyResults.rates?.HUF
        createRatesList(currencyName,currencyValue)
        currencyName="IDR"
        currencyValue= currencyResults.rates?.IDR
        createRatesList(currencyName,currencyValue)
        currencyName="ILS"
        currencyValue= currencyResults.rates?.ILS
        createRatesList(currencyName,currencyValue)
        currencyName="INR"
        currencyValue= currencyResults.rates?.INR
        createRatesList(currencyName,currencyValue)
        currencyName="ISK"
        currencyValue= currencyResults.rates?.ISK
        createRatesList(currencyName,currencyValue)
        currencyName="JPY"
        currencyValue= currencyResults.rates?.JPY
        createRatesList(currencyName,currencyValue)
        currencyName="KRW"
        currencyValue= currencyResults.rates?.KRW
        createRatesList(currencyName,currencyValue)
        currencyName="MXN"
        currencyValue= currencyResults.rates?.MXN
        createRatesList(currencyName,currencyValue)
        currencyName="MYR"
        currencyValue= currencyResults.rates?.MYR
        createRatesList(currencyName,currencyValue)
        currencyName="NOK"
        currencyValue= currencyResults.rates?.NOK
        createRatesList(currencyName,currencyValue)
        currencyName="NZD"
        currencyValue= currencyResults.rates?.NZD
        createRatesList(currencyName,currencyValue)
        currencyName="PHP"
        currencyValue= currencyResults.rates?.PHP
        createRatesList(currencyName,currencyValue)
        currencyName="RON"
        currencyValue= currencyResults.rates?.RON
        createRatesList(currencyName,currencyValue)
        currencyName="RUB"
        currencyValue= currencyResults.rates?.RUB
        createRatesList(currencyName,currencyValue)
        currencyName="SEK"
        currencyValue= currencyResults.rates?.SEK
        createRatesList(currencyName,currencyValue)
        currencyName="SGD"
        currencyValue= currencyResults.rates?.SGD
        createRatesList(currencyName,currencyValue)
        currencyName="THB"
        currencyValue= currencyResults.rates?.THB
        createRatesList(currencyName,currencyValue)
        currencyName="TRY"
        currencyValue= currencyResults.rates?.TRY
        createRatesList(currencyName,currencyValue)
        currencyName="USD"
        currencyValue= currencyResults.rates?.USD
        createRatesList(currencyName,currencyValue)
        currencyName="ZAR"
        currencyValue= currencyResults.rates?.ZAR
        createRatesList(currencyName,currencyValue)
        //
        operateAdapter(ratesList )
    }
    //*********************************************************************
    //****create rates list to update in recyclerview
    //*********************************************************************
    private fun createRatesList(currencyName:String,currencyValue:Double?){

        val currencyBigName:String?= currencyDetailsMap.get(currencyName)?.currencies?.get(0)?.name//the full name of currency
        val alpha2Code:String? =currencyDetailsMap.get(currencyName)?.alpha2Code//link to currency home country
        val flagLink:String?="https://www.countryflags.io/"+alpha2Code+"/flat/64.png"//country code so we can get flag in adapter
        //create specific currency with all its params:
        val specificCurrency:SpecificCurrency=SpecificCurrency(currencyName,currencyBigName ,currencyValue,flagLink)
        //create the rateList with the details of the currency
        ratesList.add(specificCurrency)
    }
    //*********************************************************************
    //****permission from user to enable internet in the app
    //*********************************************************************
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
        viewAdapter = RecyclerAdapter(currency,this)//movies)//, paymentIds )//dataset

        recyclerView.apply {
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
    //*********************************************************************
    //****data classes for revolut server
    //*********************************************************************
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
            val IDR : Double,
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
    //*********************************************************************
    //****data classes for the country details server
    //*********************************************************************
    //for the details json:
    data class CurrencyDetails (

            @SerializedName("currencies") val currencies : List<Currencies>,
            @SerializedName("flag") val flag : String,
            @SerializedName("alpha2Code") val alpha2Code : String
    )
    data class Currencies (

            @SerializedName("code") val code : String,
            @SerializedName("name") val name : String,
            @SerializedName("symbol") val symbol : String
    )



