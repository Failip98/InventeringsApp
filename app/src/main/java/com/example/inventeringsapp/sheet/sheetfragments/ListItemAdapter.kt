package com.example.inventeringsapp.sheet.sheetfragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inventeringsapp.R

class ListItemAdapter(var listItems: ArrayList<ListItem>) :
    RecyclerView.Adapter<ListItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.listitem_fragment, parent, false)
        return ListItemViewHolder(
            view
        )
    }

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.setListItem(listItems[position])
    }

}