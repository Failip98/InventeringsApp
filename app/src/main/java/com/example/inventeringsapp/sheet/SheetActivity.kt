package com.example.inventeringsapp.sheet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventeringsapp.OnStart
import com.example.inventeringsapp.R
import com.example.inventeringsapp.Utils
import com.example.inventeringsapp.main.MainActivity
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.sheetfragments.*
import kotlinx.android.synthetic.main.activity_sheet.*
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

private const val TAG = "+SheetActivity"
class SheetActivity : AppCompatActivity(), ListItemActionListener {

    @Inject
    lateinit var viewModel: SheetViewModel

    var fragmentManager = supportFragmentManager
    var addItemFragment = AddItemFragment()
    var deliteItemFragment = DeliteItemFragment()
    var scanItemFragment = ScanItemFragment(this)
    var updateitemFragment = UpdateItemFragment(this)
    lateinit var listItemAdapter: ListItemAdapter

    companion object {
        var sheetId = ""
        var pageName = ""
        var listItems = arrayListOf<ListItem>()
        val emptyFragment = EmptyFragment()
        var lastClicktListItem = ""
        var lastFaildscanget = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)
        changeFragment(emptyFragment)
        OnStart.applicationComponent.inject(this)
        if (DB.devmode == true){
            sheetId = "1oX3wvT_i0c5V8Pme7AOeoBd8t1Lf-3zzWHjBzfTT2Gw"
            pageName = "Test"
            DB.sheetId = sheetId
            DB.pagename = pageName
        }else{
            sheetId = intent?.getStringExtra("sheet_id").toString()
            pageName = intent?.getStringExtra("pageName").toString()
        }

        btn_scanItem.setOnClickListener {
            changeFragment(scanItemFragment)
        }
        btn_addListItem.setOnClickListener {
            changeFragment(addItemFragment)
        }
        btn_editItem.setOnClickListener {
            changeFragment(updateitemFragment)
        }
        btn_deliteItem.setOnClickListener {
            changeFragment(deliteItemFragment)
        }
        btn_close.setOnClickListener {
            changeFragment(emptyFragment)
        }
        printSheet()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.menu_backToMain->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_renew->{
                printSheet()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }

    fun printSheet(){
        getDataFromApi()
    }

    fun changeFragment(fragment: Fragment){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, fragment)
        fragmentTransaction.commit()
    }

    fun getDataFromApi() {
        Log.d(TAG,"Print new List")
        viewModel.fetchList(sheetId,pageName)
        CoroutineScope(Main).launch {
            Handler().postDelayed({
                errorMessage()
                createRecyclerView()
            }, 1500)
        }
    }

    fun createRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        rv_list.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        rv_list.addItemDecoration(dividerItemDecoration)

        listItemAdapter = ListItemAdapter(listItems,this)
        rv_list.adapter = listItemAdapter
    }

    override fun itemClicked(listItem: ListItem) {
        Log.d("___",listItem.id)
        lastClicktListItem = listItem.id
    }

    @SuppressLint("SetTextI18n")
    fun errorMessage() {
        Log.d(TAG,"onPostExecute")
        Log.d(TAG, listItems.size.toString())
        if (listItems.size != 0){
            textView_error_mesage.visibility = View.GONE
        }
        else if(listItems.size == 0){
            textView_error_mesage.text = "List empty or wrong sheet id"
        }
    }

}
