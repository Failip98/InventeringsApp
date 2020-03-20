package com.example.inventeringsapp.sheet.sheetfragments

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
import com.google.android.material.tabs.TabLayout
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest
import com.google.api.services.sheets.v4.model.DimensionRange
import com.google.api.services.sheets.v4.model.Request
import kotlinx.android.synthetic.main.fragment_deliteitem.*
import java.io.IOException

private const val TAG = "DeliteItemFragment"
class DeliteItemFragment : Fragment() {

    var idToRemove = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_deliteitem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_remove.setOnClickListener {
            delite()
            Handler().postDelayed({
                (activity as SheetActivity?)?.printSheet()
            },1000)
        }
    }

    fun delite(){
        var input = editText_remove.text.toString()
        if (input.length != 9 || input == ""){
            editText_remove.setHintTextColor(Color.RED)
            editText_remove.setTextColor(Color.RED)
            Toast.makeText(context, "Moste be 9 numbers", Toast.LENGTH_SHORT).show()
        }else{
            idToRemove = input.toInt()
            var a = SheetActivity.listItems
            var index = -1
            var i = 0
            while (i<a.size){
                if (a[i].id.equals(idToRemove.toString())){
                    index = i
                }
                i++
            }
            Log.d(TAG,index.toString())
            if (index == -1){
                editText_remove.setTextColor(Color.RED)
                Toast.makeText(context, "Can´t finde Id", Toast.LENGTH_SHORT).show()
            }else{
                editText_remove.getText().clear()
                Thread(Runnable {
                    val content = BatchUpdateSpreadsheetRequest()
                    val request: Request = Request()
                        .setDeleteDimension(
                            DeleteDimensionRequest()
                                .setRange(
                                    DimensionRange()
                                        .setDimension("ROWS")
                                        .setStartIndex(index+1)
                                        .setEndIndex(index+2)
                                )
                        )
                    val requests: MutableList<Request> = ArrayList<Request>()
                    requests.add(request)
                    content.requests = requests
                    try {
                        DB.mService?.spreadsheets()?.batchUpdate(SheetActivity.sheetId, content)
                            ?.execute()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }).start()
            }
        }
    }
}