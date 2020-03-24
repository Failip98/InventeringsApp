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
        textView_doc.text = "-Create a google sheet and share it so that every one white the link can edit the file." +
                "\n"+"-Press get shareable link and the part between /d/ and /edit?usp=sharing is sheet id excluding this to parts."+
                "\n"+"-Page 1 is the page name if you don´t rename it or want to get a other sheet page."+
                "\n"+"-Name cell A1 to F6 id, name, barcode, quantity, cost, value."+
                "\n"+"-Recommendation don't use column A to F more than to the list."
        btn_doc_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
