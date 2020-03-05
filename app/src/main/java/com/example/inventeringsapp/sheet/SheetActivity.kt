package com.example.inventeringsapp.sheet

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.inventeringsapp.R
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.sheetfragments.*
import kotlinx.android.synthetic.main.activity_sheet.*
import java.util.*

class SheetActivity : AppCompatActivity() {

    private var mLastError: Exception? = null

    private val fragmentManager = supportFragmentManager
    private val emptyFragment = EmptyFragment()
    private val addItemFragment = AddItemFragment()
    private val deliteItemFragment = DeliteItemFragment()
    private val scanItemFragment = ScanItemFragment();
    private val updateitemFragment = UpdateItemFragment(this);


    companion object {
        var sheetId = ""
        var pageName = ""
        var sheetList = mutableListOf<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)
        changeFragment(emptyFragment)
        printSheet()

    }

    fun printSheet(){
        var broutSheetList = doInBackground()
        Handler().postDelayed({
            onPostExecute(broutSheetList)
        }, 1500)
    }

    fun changeFragment(fragment: Fragment){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.menu_addItem->{
                Log.d("___", item.itemId.toString())
                changeFragment(addItemFragment)
            }
            R.id.menu_scanItem->{
                Log.d("___", item.itemId.toString())
                changeFragment(scanItemFragment)
            }
            R.id.menu_removeItem->{
                Log.d("___", item.itemId.toString())
                changeFragment(deliteItemFragment)
            }
            R.id.menu_updateItem->{
                Log.d("___", item.itemId.toString())
                changeFragment(updateitemFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun doInBackground(): List<String?>? {
        //return getDataFromApi()
        return try {
            getDataFromApi()
        } catch (e: java.lang.Exception) {
            mLastError = e
            null
        }
    }

    fun getDataFromApi(): List<String>? {
        //"1oX3wvT_i0c5V8Pme7AOeoBd8t1Lf-3zzWHjBzfTT2Gw"

        if (DB.devmode == true){
            sheetId = "1oX3wvT_i0c5V8Pme7AOeoBd8t1Lf-3zzWHjBzfTT2Gw"
            pageName = "Test"
        }else{
            sheetId = intent?.getStringExtra("sheet_id").toString()
            pageName = intent?.getStringExtra("pageName").toString()
        }
        //var sheetId = intent.getStringExtra("sheet_id")
        //var pageName = intent.getStringExtra("pageName")
        val spreadsheetId = sheetId
        val range = pageName + "!A:F"
        val results: MutableList<String> =
            ArrayList()
        Thread(Runnable {
            try {
            val response =
                DB.mService!!.spreadsheets().values()[spreadsheetId, range]
                    .execute()
            val values = response.getValues()
            if (values != null) {
                for (row in values) {
                    for (col in values) {
                        if (col[2].equals("")) {
                            col.removeAt(2)
                            col.add(2, "----------------------")
                        }
                    }
                    results.add(row[0].toString() + ", " + row[1] + ", " + row[2] + ", " + row[3] + ", " + row[4] + ", " + row[5])
                }
            }
            }catch (e: java.lang.Exception){
                mLastError = e
            }
        }).start()
        return results
    }

    fun onPostExecute(output: List<String?>?) {
        if (output == null || output.size == 0) {
            if (DB.devmode == true){
                mOutputText.text = ("The following error occurred:\n" + mLastError!!.message)
            }else{
                mOutputText.setText("Check your sheet settings")
            }
        } else {
            mOutputText.setText(TextUtils.join("\n", output))

        }
        Handler().postDelayed({
            sheetList = output as MutableList<String>
        },1000)
    }

}
