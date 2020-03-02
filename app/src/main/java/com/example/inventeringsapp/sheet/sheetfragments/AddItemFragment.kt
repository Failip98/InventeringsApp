package com.example.inventeringsapp.sheet.sheetfragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.inventeringsapp.R
import com.example.inventeringsapp.repository.DB.Companion.mService
import com.example.inventeringsapp.sheet.SheetActivity
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.android.synthetic.main.fragment_additem.*
import java.io.IOException
import java.util.*


class AddItemFragment : Fragment() {

    lateinit var textView: TextView

    var id = ""
    var name = ""
    var barcode = ""
    var quantity = 0.0
    var cost = 0.0
    var valueprice = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_additem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //addItem()

        btn_addItem.setOnClickListener {
            name = editText_addItemName.text.toString()
            if(editText_addItemQuantity.text.toString() != ""){
                quantity = editText_addItemQuantity.text.toString().toDouble()
            }else{
                quantity = quantity.toInt().toDouble()
            }

            if(editText_addItemCost.text.toString() != ""){
                cost = editText_addItemCost.text.toString().toDouble()
            }else{
                cost = cost.toInt().toDouble()
            }


            if (name == "") {
                if (name == "") {
                    editText_addItemName.setHint("Fill in Name")
                    editText_addItemName.setHintTextColor(Color.RED)
                }
            }
            else{
                editText_addItemName.getText().clear()
                editText_addItemQuantity.getText().clear()
                editText_addItemCost.getText().clear()
                addItem()
                Handler().postDelayed({
                    (activity as SheetActivity?)?.printSheet()
                },1000)
            }
        }
    }


    fun addItem() {
        id = System.currentTimeMillis().toString()
        val appendBody =
            ValueRange()
                .setValues(
                    Arrays.asList(
                        Arrays.asList(id,name,barcode,quantity,cost,valueprice)) as List<MutableList<Any>>?
                )
        Thread(Runnable {
            try {
                val appendResult: AppendValuesResponse = mService?.spreadsheets()?.values()
                    ?.append(SheetActivity.sheetId, SheetActivity.pageName, appendBody)
                    ?.setValueInputOption("USER_ENTERED")
                    ?.setInsertDataOption("INSERT_ROWS")
                    ?.setIncludeValuesInResponse(true)
                    ?.execute()!!
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

}