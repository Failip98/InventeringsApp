package com.example.inventeringsapp.sheet.sheetfragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.inventeringsapp.R
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.SheetActivity
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.android.synthetic.main.fragment_deliteitem.*
import kotlinx.android.synthetic.main.fragment_updateitem.*
import java.util.*


class UpdateItemFragment : Fragment() {

    var idToUpdate = 0
    var name = ""
    var quantity = 0.0
    var cost = 0.0
    var max = 999999999
    var min = 100000000
    var valueprice = 0.0
    var barcode = ""
    var index = 0
    var sheetList : MutableList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_updateitem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_update.setOnClickListener {
            readinput()
        }
    }

    fun readinput() {
        //var idInput = editText_updateId.text.toString() /// UN KOMENT WHEN DONE
        var idInput = "843618761"

        Log.d("___", "ID INPUT" + idInput)
        Log.d("___", "ID INPUT:" + idInput.length)

        if (idInput.length > 9 || idInput == "" && idInput.length < 10) {
            editText_updateId.setHintTextColor(Color.RED)
            editText_updateId.setTextColor(Color.RED)
            Log.d("___", "FEL")
        } else {
            idToUpdate = idInput.toInt()
            Log.d("___", "RÄTT")
        }
        if (editText_updateName.text.toString() != "") {
            name = editText_updateName.text.toString()
        }
        if (editText_updateQuantity.text.toString() != "" || editText_updateQuantity.text.toString().length > 9 && editText_updateQuantity.text.toString().length < 10) {
            cost = editText_updateQuantity.text.toString().toDouble()
        }
        if (editText_updateCost.text.toString() != "" || editText_updateCost.text.toString().length > 9 && editText_updateCost.text.toString().length < 10) {
            quantity = editText_updateCost.text.toString().toDouble()
        }
        findindex()
    }

    fun findindex() {
        Log.d("___", "ID to finde:" + idToUpdate)
        if (idToUpdate < min || idToUpdate > max) {
            editText_updateId.setTextColor(Color.RED)
            Log.d("___", "Not between " + max + " & " + min)
        } else {
            var i = 0
            sheetList = SheetActivity.sheetList
            Log.d("___", "List size: " + sheetList!!.size)
            while (i < sheetList!!.size) {
                if (sheetList!![i].contains(idToUpdate.toString())) {
                    Log.d("___", sheetList!![i].contains(idToUpdate.toString()).toString())
                    index = i
                    Log.d("","Found at index :" +index)
                }
                i++
            }
            if (index == 0) {
                editText_updateId.setTextColor(Color.RED)
                Log.d("___", "Can´t finde: " + index)
            } else {
                editText_updateId.getText().clear()
                Log.d("___", sheetList!![index])
                setvalue()
            }
        }
    }

    fun setvalue(){
        var row = sheetList!![index].split(", ").toTypedArray()
        
        if (name == ""){
            name = row.elementAt(1)
            Log.d("___","Name"+name)
        }
        if (barcode == ""){
            barcode = row.elementAt(2)
            Log.d("___","Barcode"+barcode)
        }
        if (quantity == 0.0){
            quantity = row.elementAt(3).replace(",",".").toDouble()
            Log.d("___","Quantity"+quantity)
        }
        if (cost == 0.0){
            cost = row.elementAt(4).replace(",",".").toDouble()
            Log.d("___","Cost"+cost)
        }
        update()
    }

    fun update(){

        Log.d("___","Found at index :" +index)

        Thread(Runnable {
            val spreadsheetId = SheetActivity.sheetId
            // The A1 notation of the values to update.
            val range = SheetActivity.pageName +"!A"+(index+1)+":F"+(index+1) //"my-range" // TODO: Update placeholder value.
            val valueInputOption = "RAW"
            val requestBody = ValueRange()
                .setValues(
                    Arrays.asList(
                        Arrays.asList(idToUpdate,name,barcode,quantity,cost,valueprice)) as List<MutableList<Any>>?
                )
            val request =
                DB.mService?.spreadsheets()?.values()?.update(spreadsheetId, range, requestBody)
            request?.valueInputOption = valueInputOption
            request?.execute()
        }).start()
    }
}