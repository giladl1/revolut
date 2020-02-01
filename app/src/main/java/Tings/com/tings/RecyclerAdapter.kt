package layout

//import Tings.com.tings.firebaseClasses.Payment

import Tings.com.tings.R
import Tings.com.tings.SpecificCurrency
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.recyclerview_item_revolut.view.*


class RecyclerAdapter(private var myDataset: MutableList<SpecificCurrency>,context:Context)://MutableList<mov> ) ://Array<String>//
        RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    var exchangeRateList=mutableMapOf<String,Double>()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "edittext_focus_mode"
    private var sharedPref: SharedPreferences= context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val v: View,val context: Context) : RecyclerView.ViewHolder(v) {
        val imageViewCurrency : ImageView
        val textViewCurShort: TextView
        val textViewCurLong: TextView
        val editTextCurValue: EditText
//        val textViewImage: TextView//to save the url link to the image


        init {
            // Define click listener for the ViewHolder's View.
            imageViewCurrency=v.imageViewCurrency
            textViewCurShort = v.textViewCurShort
            textViewCurLong=v.textViewCurLong
            editTextCurValue=v.editTextCurVal


        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerAdapter.MyViewHolder {
        // create a new view //textView

        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_revolut, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(textView,parent.context)//,myDatasetIds
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        updateExchangeRateList()

        Glide.with(holder.context)
                .load(myDataset[position].flagLink)//"http://www.geognos.com/api/en/countries/flag/GR.png")//myDataset[position].flagLink)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageViewCurrency)

        holder.textViewCurShort.text=myDataset[position].currencyName
        holder.textViewCurLong.text=myDataset[position].currencyBigName
        holder.editTextCurValue.setText(myDataset[position].currencyValue.toString())
        holder.editTextCurValue.setOnFocusChangeListener(View.OnFocusChangeListener {
            view, b -> editTextHasFocus(b) })


        //////////////////////////////////////////////////////////////////////////////
            holder.itemView.setOnClickListener(View.OnClickListener { updateList(position) })

                    //listener for typing new value for amount of currency
                    holder.editTextCurValue.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                        if (event.action == KeyEvent.ACTION_UP ) {

                            val currentValue = holder.editTextCurValue.text.toString()
                            if(!(currentValue=="")) {

                                updateListCalculation(currentValue.toDouble(),keyCode)
//                                val position=holder.editTextCurValue.getText().length
//                                holder.editTextCurValue.getSelectionEnd()
                                holder.editTextCurValue.setSelection(0)
                                return@OnKeyListener true
                            }else
                                return@OnKeyListener false
                        }
                        else return@OnKeyListener false


                    })

        holder.editTextCurValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
 }

    private fun editTextHasFocus(b: Boolean) {
        if(b){//in focus
            val editor = sharedPref.edit()
            editor.putBoolean(PREF_NAME, true)
            editor.apply()
        }
        else{//not in focus
            val editor = sharedPref.edit()
            editor.putBoolean(PREF_NAME, false)
            editor.apply()
        }


    }

    private fun updateExchangeRateList() {
                    //updating the exchangeRateList that will help us calculate the rates on each change
                    myDataset.forEachIndexed { index, it ->
                        val exchangeRate: Double = it.currencyValue!!.div(myDataset[0].currencyValue!!)
                        exchangeRateList.put(it.currencyName, exchangeRate)
                    }
                }
    private fun getExchangeRate(currencyName: String): Double? {
        return exchangeRateList.get(currencyName)
    }
    private fun updateListCalculation(basicValue: Double,keycode:Int) { //: Boolean//basicValue is the base currency value now
            myDataset.forEachIndexed { index, it ->
                //it = myDataset[index]
                //get exchange rate of this currency against basic currency:
                val exchangeRate = getExchangeRate(it.currencyName)
                if (index != 0) {
                    val newValue: Double = basicValue * exchangeRate!!//((it.currencyValue)?.toDouble()
//                            ?: basicValue) * exchangeRate
                    it.currencyValue = newValue
                } else {
//                    it.currencyValue.append(currentValue)
                    it.currencyValue = basicValue//change the first row value
                }
            }
            notifyDataSetChanged()
//            return true
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    fun updateList(itemIndex: Int) {
            //WE'LL LET THE ACTIVITY KNOW TO NOT UPDATE THE RECYCLER NOW:

            val movingItem: SpecificCurrency = myDataset[itemIndex]
            myDataset.removeAt(itemIndex);
            myDataset.add(0, movingItem);
            notifyItemMoved(itemIndex, 0)
            notifyDataSetChanged()
            updateExchangeRateList()//that's how we'll know how to calculate the exchanges
    }
}




