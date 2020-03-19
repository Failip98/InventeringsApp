package com.example.inventeringsapp.sheet.sheetfragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import com.example.inventeringsapp.LiveBarcodeScanningActivity
import com.example.inventeringsapp.R
import com.example.inventeringsapp.Utils
import com.example.inventeringsapp.sheet.SheetActivity

class ScanItemFragment(context: Context) : Fragment(){
    lateinit var textView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanitem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.textView_scanitem)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        startScanner()
    }



    fun startScanner(){
        var activity = this@ScanItemFragment.activity
        activity?.startActivity(Intent(activity, LiveBarcodeScanningActivity::class.java))
        Handler().postDelayed({
        },1000)
    }
}