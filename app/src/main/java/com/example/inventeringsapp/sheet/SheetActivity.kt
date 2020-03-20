package com.example.inventeringsapp.sheet

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
import com.example.inventeringsapp.repository.DB
import com.example.inventeringsapp.sheet.sheetfragments.*
import kotlinx.android.synthetic.main.activity_sheet.*
import javax.inject.Inject


private const val TAG = "SheetActivity"
class SheetActivity : AppCompatActivity(), ListItemActionListener {

    @Inject
    lateinit var viewModel: SheetViewModel

    val fragmentManager = supportFragmentManager
    val addItemFragment = AddItemFragment()
    val deliteItemFragment = DeliteItemFragment()
    val scanItemFragment = ScanItemFragment(this)
    val updateitemFragment = UpdateItemFragment(this)
    lateinit var listItemAdapter: ListItemAdapter


    companion object {


        var sheetId = ""
        var pageName = ""
        var listItems = arrayListOf<ListItem>()
        val emptyFragment = EmptyFragment()
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
        printSheet()
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

    fun getDataFromApi() {
        Log.d(TAG,"Print new List")
        viewModel.fetchList(sheetId,pageName)
        Handler().postDelayed({
            createRecyclerView()
        }, 1500)
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
    }
}
