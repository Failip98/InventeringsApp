package com.example.inventeringsapp.sheet.sheetfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.inventeringsapp.LiveBarcodeScanningActivity
import com.example.inventeringsapp.R

class ScanItemFragment(context: Context) : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_scanitem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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