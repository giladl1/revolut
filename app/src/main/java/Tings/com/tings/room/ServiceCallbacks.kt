package Tings.com.tings.room

import Tings.com.tings.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import layout.RecyclerAdapter

interface ServiceCallbacks {
    fun operateAdapter( movies:MutableList<Movie>)


}