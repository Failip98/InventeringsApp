package com.example.inventeringsapp.sheet.sheetfragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.inventeringsapp.R
import com.example.inventeringsapp.repository.DB.Companion.mService
import com.example.inventeringsapp.sheet.SheetActivity
import com.example.inventeringsapp.sheet.SheetActivity.Companion.lastFaildscanget
import com.example.inventeringsapp.sheet.SheetActivity.Companion.listItems
import com.google.android.material.tabs.TabLayout
import com.google.api.services.sheets.v4.SheetsRequestInitializer
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.IOException
import java.util.*
import kotlin.random.Random

private const val TAG = "AddItemFragment"
class AddItemFragment : Fragment() {

    var id = ""
    var name = ""
    var barcode = ""
    var quantity = 0.0
    var cost = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_additem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_addItem.setOnClickListener {
            var q = false
            var c = false
            var n = false
            if(editText_addItemQuantity.text.toString() != "" || editText_addItemQuantity.text.toString().length > 9 && editText_addItemQuantity.text.toString().length < 10){
                quantity = editText_addItemQuantity.text.toString().toDouble()
                q = true
            }else{
                editText_addItemQuantity.setHintTextColor(Color.RED)
            }

            if(editText_addItemCost.text.toString() != "" || editText_addItemCost.text.toString().length > 9 && editText_addItemCost.text.toString().length < 10){
                cost = editText_addItemCost.text.toString().toDouble()
                c = true
            }else{
                editText_addItemCost.setHintTextColor(Color.RED)
            }

            if (editText_addItemName.text.toString() == "") {
                editText_addItemName.setHint("Fill in Name")
                editText_addItemName.setHintTextColor(Color.RED)
            }else{
                n = true
                name = editText_addItemName.text.toString()
            }

            if (q && c && n){
                if(checkBox_barcode.isChecked){
                    barcode = lastFaildscanget
                }else{
                    barcode = ""
                }
                editText_addItemName.getText().clear()
                editText_addItemQuantity.getText().clear()
                editText_addItemCost.getText().clear()
                editText_addItemQuantity.setHintTextColor(Color.GRAY)
                editText_addItemCost.setHintTextColor(Color.GRAY)
                editText_addItemName.setHintTextColor(Color.GRAY)
                Log.d(TAG,name + "tacken")
                addItem(id,name,barcode,quantity,cost)
                view.hideKeyboard()
                Handler().postDelayed({
                    (activity as SheetActivity?)?.printSheet()
                },1000)
            }else{
                Log.d(TAG,n.toString()+"||"+q.toString()+"||"+c.toString())
            }

        }
    }

    override fun onResume() {
        super.onResume()
        checkBox_barcode.isChecked = false
        if (lastFaildscanget != ""){
            barcode = lastFaildscanget
        }else{
            barcode = ""
        }
    }

    companion object{
        var id :String = ""
        fun addItem(id:String, name:String, barcode:String, quantity:Double,cost:Double) {
            Log.d(TAG,name + "compani object")
            this.id = id
            this.id = Random.nextInt(100000000,999999999).toString()
            val appendBody =
                ValueRange()
                    .setValues(
                        Arrays.asList(
                            Arrays.asList(this.id,name,barcode,quantity,cost,"=SUM(D"+(listItems.size+2)+"*E"+(listItems.size+2)+")")) as List<MutableList<Any>>?
                    )
            Thread(Runnable {
                try {
                    val appendResult: AppendValuesResponse = mService?.spreadsheets()?.values()
                        ?.append(SheetActivity.sheetId, SheetActivity.pageName, appendBody)
                        ?.setValueInputOption("USER_ENTERED")
                        ?.setInsertDataOption("INSERT_ROWS")
                        ?.setIncludeValuesInResponse(true)
                        ?.execute()!!
                    lastFaildscanget = ""
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}