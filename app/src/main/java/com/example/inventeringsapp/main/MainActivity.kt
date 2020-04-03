package com.example.inventeringsapp.main

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.inventeringsapp.OnStart
import com.example.inventeringsapp.R
import com.example.inventeringsapp.doc.DocActivity
import com.example.inventeringsapp.login.LoginActivity
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.SheetActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OnStart.applicationComponent.inject(this)
        btn_goToSheet.setOnClickListener {

            var sheetId = editText_sheetId.text.toString()
            var pagename = editText_pagename.text.toString()
            var apiKey = editText_key.text.toString()
            if ( sheetId == "" || pagename == "" || DB.apiKey == "") {
                if (sheetId == "") {
                    editText_sheetId.setHint("Fill in Sheet_Id")
                    editText_sheetId.setHintTextColor(Color.RED)
                }else{
                    editText_sheetId.setHintTextColor(Color.GRAY)
                }
                if (pagename == "") {
                    editText_pagename.setHint("Fill in Page_Name")
                    editText_pagename.setHintTextColor(Color.RED)
                }else{
                    editText_pagename.setHintTextColor(Color.GRAY)
                }
                if (pagename == "") {
                    editText_key.setHint("Fill in API Key")
                    editText_key.setHintTextColor(Color.RED)
                }else{
                    editText_key.setHintTextColor(Color.GRAY)
                }
            }
            else{
                if (DB.devmode == true){
                  DB.apiKey = "0lmvri8kzv31wmukozh92wdep8pv5s"
                }else{
                    DB.apiKey = apiKey
                }
                val intent = Intent(this, SheetActivity::class.java)
                    .putExtra("sheet_id",sheetId)
                    .putExtra("pageName",pagename)
                DB.sheetId = sheetId
                DB.pagename = pagename
                startActivity(intent)
            }
        }
        btn_doc.setOnClickListener {
            val intent = Intent(this, DocActivity::class.java)
            startActivity(intent)
        }
        btn_logout.setOnClickListener {
            signOut()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = DB.auth.currentUser
        if (currentUser == null || DB.mService == null){
            updateUI(null)
        }else{
            updateUI(currentUser)
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            btn_logout.visibility = View.VISIBLE
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signOut() {
        DB.auth.signOut()
        DB.mGoogleSignInClient?.signOut()?.addOnCompleteListener(this) {
            updateUI(null)
        }
    }

}
