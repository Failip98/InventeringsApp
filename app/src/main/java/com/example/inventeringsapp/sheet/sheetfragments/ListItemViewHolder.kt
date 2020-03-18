package com.example.inventeringsapp.sheet.sheetfragments

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem_fragment.view.*


class ListItemViewHolder(val view: View) :
    RecyclerView.ViewHolder(view) {
    @SuppressLint("SetTextI18n")
    fun setListItem(listItem: ListItem) {
        view.textView_ListItem_id.text = "ID: " + listItem.id
        view.textView_ListItem_name.text = listItem.name
        view.textView_ListItem_barcode.text = "Streckkod: "+listItem.barcode
        view.textView_ListItem_quantity.text = listItem.quantity.toString()+"st"
        view.textView_ListItem_cost.text = listItem.cost.toString()+"Kr"
        view.textView_ListItem_value.text = listItem.valueprice.toString()+"Kr"
    }
}