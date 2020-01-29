package layout

//import Tings.com.tings.firebaseClasses.Payment

import Tings.com.tings.R
import Tings.com.tings.SpecificCurrency
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(private var myDataset: MutableList<SpecificCurrency>)://MutableList<mov> ) ://Array<String>//
        RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val v: View,val context: Context) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView
        val textViewRating: TextView
        val textViewReleaseYear: TextView
        val textViewGenres: TextView
        val textViewImage: TextView//to save the url link to the image
        val imageView : ImageView

        init {
            // Define click listener for the ViewHolder's View.
            textViewTitle = v.findViewById(R.id.titleTextView)
            textViewRating = v.findViewById(R.id.ratingTextView)
            textViewReleaseYear = v.findViewById(R.id.releaseYearTextView)
            textViewGenres = v.findViewById(R.id.genresTextview)
            textViewImage= v.findViewById(R.id.textViewImage)
            imageView=v.findViewById(R.id.imageView)

//            textView5 = v.findViewById(R.id.textView5)
//            textView6 = v.findViewById(R.id.textView6)
            v.setOnClickListener {
                Log.d("RecyclerAdapter", "Element $adapterPosition clicked.")
//                val intent = Intent(context, SpecificMovieActivity::class.java)
//                intent.putExtra("title",textViewTitle.text)
//                intent.putExtra("image",v.textViewImage.text)//the url link
//                intent.putExtra("rating",textViewRating.text)
//                intent.putExtra("releaseYear",textViewReleaseYear.text)
//                intent.putExtra("genres",textViewGenres.text)

//                intent.putExtra("image",textView4.text)

//                context.startActivity(intent)

            }
        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerAdapter.MyViewHolder {
        // create a new view //textView

        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_row2, parent, false) //as TextView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(textView,parent.context)//,myDatasetIds
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewTitle.text=myDataset[position].currencyName
        holder.textViewRating.text=myDataset[position].currencyValue.toString()
        holder.itemView.setOnClickListener(View.OnClickListener { updateList(position) })
//        holder.textViewTitle.text = myDataset[position].title
//        holder.textViewRating.text = myDataset[position].rating.toString()
//        holder.textViewReleaseYear.text = myDataset[position].releaseYear.toString()
//        holder.textViewGenres.text=getGenresInString( myDataset[position].genre)
//        holder.textViewImage.text = myDataset[position].image


//        Glide.with(holder.context)
//                .load(myDataset[position].image)
//                .into(holder.imageView)
    }

    private fun getGenresInString(genre: MutableList<String>): CharSequence? {
        var result:String=""
        genre.forEach{
            result=result+it+" | "
        }
        return result
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    fun updateList(itemIndex:Int) {
//        myDataset = data
//        notifyDataSetChanged()
        val movingItem:SpecificCurrency=myDataset[itemIndex]
        myDataset.removeAt(itemIndex);
        myDataset.add(0, movingItem);
        notifyItemMoved(itemIndex,0)
//// notify adapter
//        adapter.notifyItemMoved(fromPosition, toPosition);
//
//        myDataset.set(0,myDataset[itemIndex])
//        notifyItemChanged(0)
    }
}



