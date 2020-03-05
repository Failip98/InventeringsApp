package com.example.inventeringsapp.sheet.sheetfragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.inventeringsapp.R
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.SheetActivity
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.android.synthetic.main.fragment_updateitem.*
import java.util.*


class UpdateItemFragment (context: Context) : Fragment() {

    var idToUpdate = 0
    var name = ""
    var quantity = 0.0
    var cost = 0.0
    var valueprice = 0.0
    var barcode =""
    var index = 0
    var sheetList : MutableList<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_updateitem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_update.setOnClickListener {
            readinput()
        }
    }

    fun readinput() {
        var badinput = 0
       var idInput = editText_updateId.text.toString()
        if(idInput != ""){
            if (idInput.length == 9) {
                idToUpdate = idInput.toInt()
            } else {
                //editText_updateId.setTextColor(Color.RED)
                editText_updateId.setHint("Id most be 9 numbers")
                Toast.makeText(context, "Id most be 9 numbers", Toast.LENGTH_SHORT).show()
                badinput ++
            }
        }else{
            badinput ++
            editText_updateId.setHint("Ned a Id")
        }


        if (editText_updateName.text.toString() != "") {
            name = editText_updateName.text.toString()
        }

        if (editText_updateQuantity.text.toString() != "") {
            if (editText_updateQuantity.text.toString().length < 10){
                cost = editText_updateQuantity.text.toString().toDouble()
            }else{
                editText_updateQuantity.setHint("Number to big")
                Toast.makeText(context, "Number to big", Toast.LENGTH_SHORT).show()
                badinput ++
            }
        }

        if (editText_updateCost.text.toString() != "") {
            if (editText_updateCost.text.toString().length < 10){
                quantity = editText_updateCost.text.toString().toDouble()
            }else{
                Toast.makeText(context, "Number to big", Toast.LENGTH_SHORT).show()
                editText_updateCost.setHint("Number to big")
                badinput ++
            }
        }

        if (badinput == 0){
            editText_updateName.getText().clear()
            editText_updateName.setHint("Name")
            editText_updateQuantity.getText().clear()
            editText_updateQuantity.setHint("Quantity")
            editText_updateCost.getText().clear()
            editText_updateCost.setHint("Cost")
            findindex()
        }
    }

    fun findindex(){
        var i = 0
        sheetList = SheetActivity.sheetList
        while (i < sheetList!!.size) {
            if (sheetList!![i].contains(idToUpdate.toString())) {
                index = i
            }
            i++
        }
        if (index == 0) {
            editText_updateId.setHint("Can´t finde Id")
            Toast.makeText(context, "Can´t finde Id", Toast.LENGTH_SHORT).show()
        } else {
            editText_updateId.getText().clear()
            editText_updateId.setHint("Id to update")
            setvalue()
        }
    }

    fun setvalue(){
        var row = sheetList!![index].split(", ").toTypedArray()

        if (name == ""){
            name = row.elementAt(1)
        }
        if (barcode == ""){
            var barinput = row.elementAt(2).toString()
            if(barinput.contains(Regex("[0-9]"))){
                barcode = row.elementAt(2)
            }else{
                barcode = ""
            }
        }
        if (quantity == 0.0){
            quantity = row.elementAt(3).replace(",",".").toDouble()
        }
        if (cost == 0.0){
            cost = row.elementAt(4).replace(",",".").toDouble()
        }
        update()
        Handler().postDelayed({
            (activity as SheetActivity?)?.printSheet()
        },1000)
    }

    fun update(){
        Thread(Runnable {
            val spreadsheetId = SheetActivity.sheetId
            val range = SheetActivity.pageName +"!A"+(index+1)+":F"+(index+1)
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