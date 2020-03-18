package com.example.inventeringsapp.sheet.sheetfragments

data class ListItem(
    var id : String = "",
    var name : String = "",
    var barcode : String = "",
    var quantity : Double = 0.0,
    var cost : Double = 0.0,
    var valueprice : Double= 0.0
)