package com.example.inventeringsapp.repository

import android.app.Activity
import com.example.inventeringsapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class DB{
    companion object{
        var auth = FirebaseAuth.getInstance()

        fun mGoogleSignInClient (activity: Activity): GoogleSignInClient {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(activity, gso)
        }

    }
}