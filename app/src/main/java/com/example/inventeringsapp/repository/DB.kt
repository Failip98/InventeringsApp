package com.example.inventeringsapp.repository

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.api.services.sheets.v4.Sheets
import com.google.firebase.auth.FirebaseAuth

class DB{
    companion object{
        var auth = FirebaseAuth.getInstance()

        var mService: Sheets? = null

        var mGoogleSignInClient : GoogleSignInClient? = null

        var devmode = true
        var sheetId: String ?= null
        var pagename: String ?= null
        var apiKey : String ?= null
    }
}