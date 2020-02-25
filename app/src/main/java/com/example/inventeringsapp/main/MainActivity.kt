package com.example.inventeringsapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.inventeringsapp.OnStart
import com.example.inventeringsapp.R
import com.example.inventeringsapp.login.LoginActivity
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.SheetActivity
import com.google.api.services.sheets.v4.model.Sheet
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
            val intent = Intent(this, SheetActivity::class.java)
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
        updateUI(currentUser)
        Log.d("___",DB.mService.toString())
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
        Log.d("___","LOGARUT!!!")
        DB.auth.signOut()
        DB.mGoogleSignInClient?.signOut()?.addOnCompleteListener(this) {
            updateUI(null)
        }
    }

}
