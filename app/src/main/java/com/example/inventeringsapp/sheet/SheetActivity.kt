package com.example.inventeringsapp.sheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.example.inventeringsapp.R
import com.example.inventeringsapp.repository.DB
import kotlinx.android.synthetic.main.activity_sheet.*
import java.util.ArrayList

class SheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)
        btn_sheetInfo.setOnClickListener {
            printSheet()
        }
    }
    
    fun printSheet(){
        var a = doInBackground()
        Handler().postDelayed({
            onPostExecute(a)
        }, 1000)
    }
    protected fun doInBackground(): List<String?>? {
        return getDataFromApi()
    }

    private fun getDataFromApi(): List<String>? {
        //"1oX3wvT_i0c5V8Pme7AOeoBd8t1Lf-3zzWHjBzfTT2Gw"
        var sheetId = intent.getStringExtra("sheet_id")
        var pageName = intent.getStringExtra("pageName")
        val spreadsheetId = sheetId
        val range = pageName+"!A:F"
        val results: MutableList<String> =
            ArrayList()
        Thread(Runnable {

            val response =
                DB.mService!!.spreadsheets().values()[spreadsheetId, range]
                    .execute()
            val values = response.getValues()
            if (values != null) {
                for (row in values) {
                    for(col in values){
                        if (col[2].equals("")){
                            col.removeAt(2)
                            col.add(2,"----------------------")
                        }
                    }
                    results.add(row[0].toString() +", "+row[1]+", "+ row[2]+", "+ row[3]+", "+ row[4]+", "+ row[5])
                }
            }
        }).start()
        return results
    }

    protected fun onPostExecute(output: List<String?>?) {
        if (output == null || output.size == 0) {
            mOutputText.setText("No results returned.")
        } else {
            mOutputText.setText(TextUtils.join("\n", output))
        }
    }
}
