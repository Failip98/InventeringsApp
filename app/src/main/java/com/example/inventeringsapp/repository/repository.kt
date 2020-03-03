package com.example.inventeringsapp.repository

import android.util.Log
import javax.inject.Inject


private const val TAG = "REPOSITORY"
class Repository @Inject constructor() {

    var sheetId = ""

    fun test(){
        Log.d(TAG,"Repository_TEST")
    }
}