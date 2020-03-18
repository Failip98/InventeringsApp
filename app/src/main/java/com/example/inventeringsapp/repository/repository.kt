package com.example.inventeringsapp.repository

import android.util.Log
import com.example.inventeringsapp.sheet.sheetfragments.ListItem
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


private const val TAG = "REPOSITORY"
class Repository @Inject constructor() {

    fun fetchList(sheetId:String,pageName:String): MutableList<ListItem> {
        val spreadsheetId = sheetId
        val range = pageName + "!A:F"
        val results: MutableList<ListItem> = ArrayList()
        try {
            val response =
                DB.mService!!.spreadsheets().values()[spreadsheetId, range]
                    .execute()
            val values = response.getValues()
            if (values != null) {
                val nf: NumberFormat = NumberFormat.getInstance(Locale.ENGLISH)
                for (row in values.drop(1)) {
                    var listItem = ListItem()
                    listItem.id = row[0].toString()
                    listItem.name = row[1].toString()
                    listItem.barcode = row[2].toString()
                    listItem.quantity = nf.parse(row[3].toString()).toDouble()
                    listItem.cost = nf.parse(row[4].toString()).toDouble()
                    listItem.valueprice = nf.parse(row[5].toString()).toDouble()
                    results.add(listItem)
                }
            }
        }catch (e: java.lang.Exception){
            Log.d(TAG, e.message.toString())
        }
        return results
    }
}