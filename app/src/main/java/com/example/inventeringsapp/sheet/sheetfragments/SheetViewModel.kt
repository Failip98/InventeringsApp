package com.example.inventeringsapp.sheet.sheetfragments

import com.example.inventeringsapp.repository.Repository
import com.example.inventeringsapp.sheet.SheetActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject
private const val TAG = "SheetViewModel"
class SheetViewModel @Inject constructor(val repository : Repository){

    fun fetchList(sheetId:String,pageName:String){
        CoroutineScope(IO).launch {
            val listItems = repository.fetchList(sheetId, pageName)
            CoroutineScope(Main).launch{
                SheetActivity.listItems = ArrayList(listItems)
            }
        }
    }
}