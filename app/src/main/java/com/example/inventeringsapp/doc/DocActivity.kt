package com.example.inventeringsapp.doc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inventeringsapp.R
import com.example.inventeringsapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_doc.*

class DocActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc)
        textView_doc.text =
            "Set up Google Sheet "+
                    "\n"+"- Create a Google Sheet and share it so that everyone with the link can edit the file." +
                    "\n"+"- Name cell A1 to F1 ID, Name, Barcode, Quantity, Cost and Value."+
                    "\n"+"- Don't use columns A to F for more than to the list."+
                    "\n"+
                    "\n"+"Sheet Id"+
                    "\n"+"- To get the sheet id copy the part of the link between /d/ and /edit?usp=sharing in the search bar."+
                    "\n"+
                    "\n"+"Page Name"+
                    "\n"+"- Find the page name in the bottom left corner."+
                    "\n"+
                    "\n"+"Api Key"+
                    "\n"+"- Go to https://www.barcodelookup.com."+
                    "\n"+"- Register for a free trail."+
                    "\n"+"- Verify your account to get the Api Key."
        btn_doc_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
