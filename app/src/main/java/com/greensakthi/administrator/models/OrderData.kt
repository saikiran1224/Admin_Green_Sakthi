package com.greensakthi.administrator.models

data class OrderData(
    var orderID: String = "",
    var dateTimePlaced: String = "",
    var finalPrice: String = "",
    var address: String = "",
    var orderStatus: String = "",
    var transactionMode: String = "",
    var transactionID: String = "",
    var fuelName: String = "",
    var fuelUnitPrice: String = "",
    var fuelQuantitySelected: String = "",
    var custName: String = "",
    var custPhone: String = "",
    var custID: String = "",
    var key: String = "",
    var orderedFor: String = "",
    var vehicleName: String = "",
    var modelNumber: String = ""

    // The above Order Data can be sub divided into three different categories
    // - Customer details (custID, custName, custPhone, custAddress)
    // - Fuel Details (fuelName, fuelUnitPrice, quantity, finalPrice)
    // - Order Details (dateTimePlaced, transactionMode, *transStatus*, *orderStatus*)
)