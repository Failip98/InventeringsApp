package com.example.inventeringsapp.sheet.sheetfragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.inventeringsapp.R
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.SheetActivity
import com.example.inventeringsapp.sheet.SheetActivity.Companion.lastClicktListItem
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.android.synthetic.main.fragment_updateitem.*
import java.io.IOException
import java.util.*

private const val TAG = "UpdateItemFragment"
class UpdateItemFragment (context: Context) : Fragment() {

    var idToUpdate = 0
    var name = ""
    var quantity = -1.0
    var cost = -1.0
    var valueprice = 0.0
    var barcode =""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_updateitem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lastClicktListItem != "" || SheetActivity.dubblettId != ""){
            if (SheetActivity.dubblettId != ""){
                editText_updateId.setText(SheetActivity.dubblettId)
            }else if (lastClicktListItem != ""){
                editText_updateId.setText(lastClicktListItem)
            }
        }
        btn_update.setOnClickListener {
            readinput()
        }
    }

    override fun onResume() {
        super.onResume()
        if (lastClicktListItem != "" || SheetActivity.dubblettId != ""){
            if (SheetActivity.dubblettId != ""){
                editText_updateId.setText(SheetActivity.dubblettId)
            }else if (lastClicktListItem != ""){
                editText_updateId.setText(lastClicktListItem)
            }
        }
    }

    fun readinput() {
        var badinput = 0
       var idInput = editText_updateId.text.toString()
        if(idInput != ""){
            if (idInput.length == 9) {
                idToUpdate = idInput.toInt()
            } else {
                editText_updateId.setTextColor(Color.RED)
                editText_updateId.setHint("Id most be 9 numbers")
                Toast.makeText(context, "Id most be 9 numbers", Toast.LENGTH_SHORT).show()
                badinput ++
            }
        }else{
            badinput ++
            editText_updateId.setHint("Ned a Id")
            editText_updateId.setHintTextColor(Color.RED)
            Toast.makeText(context, "Id most be 9 numbers", Toast.LENGTH_SHORT).show()
        }


        if (editText_updateName.text.toString() != "") {
            name = editText_updateName.text.toString()
        }

        if (editText_updateQuantity.text.toString() != "") {
            if (editText_updateQuantity.text.toString().length < 10){
                quantity = editText_updateQuantity.text.toString().toDouble()
            }else{
                Toast.makeText(context, "Number to big", Toast.LENGTH_SHORT).show()
                editText_updateQuantity.setHint("Number to big")
                editText_updateQuantity.setHintTextColor(Color.RED)
                badinput ++
            }
        }

        if (editText_updateCost.text.toString() != "") {
            if (editText_updateCost.text.toString().length < 10){
                cost = editText_updateCost.text.toString().toDouble()
            }else{
                Toast.makeText(context, "Number to big", Toast.LENGTH_SHORT).show()
                editText_updateCost.setHint("Number to big")
                editText_updateCost.setHintTextColor(Color.RED)
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
            editText_updateId.setTextColor(Color.GRAY)
            editText_updateId.setHintTextColor(Color.GRAY)
            editText_updateQuantity.setHintTextColor(Color.GRAY)
            editText_updateCost.setHintTextColor(Color.GRAY)
            findindex()
        }
    }

    fun findindex(){
        var sheetList = SheetActivity.listItems
        var i = 0
        var index = -1
        while (i<sheetList.size){
            if (sheetList[i].id.equals(idToUpdate.toString())){
                index = i
            }
            i++
        }
        if (index == -1) {
            editText_updateId.setHint("Can´t finde Id")
            Toast.makeText(context, "Can´t finde Id", Toast.LENGTH_SHORT).show()
        } else {
            editText_updateId.getText().clear()
            editText_updateId.setHint("Id to update")
            Log.d(TAG,index.toString())
            setvalue(sheetList[index],index)
        }
    }


    fun setvalue(
        listItem: ListItem, index: Int) {
        if (name == ""){
            name = listItem.name
        }
        if (barcode == ""){
            barcode = listItem.barcode
        }
        if (quantity == -1.0){
            quantity = listItem.quantity
        }
        if (cost == -1.0){
            cost = listItem.cost
        }
        update(index)
        Handler().postDelayed({
            (activity as SheetActivity?)?.printSheet()
        },1000)
    }

    fun update(index:Int){
        Thread(Runnable {
            val spreadsheetId = SheetActivity.sheetId
            val range = SheetActivity.pageName +"!A"+(index+2)+":F"+(index+2)
            val valueInputOption = "RAW"
            Log.d(TAG,cost.toString())
            val requestBody = ValueRange()
                .setValues(
                    Arrays.asList(
                        Arrays.asList(idToUpdate,name,barcode,quantity,cost,valueprice)) as List<MutableList<Any>>?
                )
            try {
                val request =
                    DB.mService?.spreadsheets()?.values()?.update(spreadsheetId, range, requestBody)
                request?.valueInputOption = valueInputOption
                request?.execute()
                if (lastClicktListItem != "" || SheetActivity.dubblettId != ""){
                    if (SheetActivity.dubblettId != ""){
                        SheetActivity.dubblettId = ""
                        SheetActivity.dubblettIndex = -1
                        lastClicktListItem = ""
                    }else if (lastClicktListItem != ""){
                        lastClicktListItem = ""
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }
}