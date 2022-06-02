package com.greensakthi.administrator.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.greensakthi.administrator.R

class OrderDetailsActivity : AppCompatActivity() {

    lateinit var txtOrderID: TextView

    lateinit var txtFuelName: TextView
    lateinit var txtFuelUnitPrice: TextView
    lateinit var txtQuantityFinalPrice: TextView

    lateinit var txtCustID: TextView
    lateinit var txtCustName: TextView
    lateinit var txtCustPhone: TextView
    lateinit var txtCustAddress: TextView

    lateinit var txtDateTime: TextView
    lateinit var txtTransMode: TextView
    lateinit var txtTransMessage: TextView
    lateinit var txtOrderStatus: TextView

    lateinit var btnCall: TextView
    lateinit var btnLocate: TextView

    lateinit var btnRecvdPayment: TextView
    lateinit var btnRecvdPayment_colored: TextView

    lateinit var btnPlaced: TextView
    lateinit var btnPlaced_colored: TextView
    lateinit var btnConfirmed: TextView
    lateinit var btnConfirmed_colored: TextView
    lateinit var btnInTransit: TextView
    lateinit var btnInTransit_Colored: TextView
    lateinit var btnDelivered: TextView
    lateinit var btnDelivered_Colored: TextView

    private var orderID = ""
    private var fuelName = ""
    private var fuelUnitPrice = ""
    private var fuelQuantity = ""
    private var fuelFinalPrice = ""

    private var custID = ""
    private var custName = ""
    private var custPhone = ""
    private var custAddress = ""

    private var dateTimePlaced = ""
    private var transMode = ""
    private var transMessage = ""
    private var orderStatus = ""
    private var key = ""

    lateinit var db: FirebaseFirestore

    @SuppressLint("SetTextI18n", "UseCompatTextViewDrawableApis")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        txtOrderID = findViewById(R.id.txtOrderId)

        db = Firebase.firestore

        txtFuelName = findViewById(R.id.txtFuelName)
        txtFuelUnitPrice = findViewById(R.id.txtFuelUnitPrice)
        txtQuantityFinalPrice = findViewById(R.id.quantity_UnitPrice)

        txtCustID = findViewById(R.id.txtCustID)
        txtCustName = findViewById(R.id.txtCustName)
        txtCustPhone = findViewById(R.id.txtCustPhone)
        txtCustAddress = findViewById(R.id.txtCustAddress)

        btnCall = findViewById(R.id.btnCall)
        btnLocate = findViewById(R.id.btnLocate)

        txtDateTime = findViewById(R.id.dateTimePlaced)
        txtTransMode = findViewById(R.id.txtTransactionMode)
        txtTransMessage = findViewById(R.id.txtTransactionMessage) // here for COD - Amount Due for Online - TransID
      //  txtOrderStatus = findViewById(R.id.txtOrderStatus)

        btnRecvdPayment = findViewById(R.id.btnRecvdPayment)
        btnRecvdPayment_colored = findViewById(R.id.btnRecvdPayment_colored)

        btnPlaced = findViewById(R.id.btnPlaced)
        btnPlaced_colored = findViewById(R.id.btnPlaced_colored)
        btnConfirmed = findViewById(R.id.btnConfirmed)
        btnConfirmed_colored = findViewById(R.id.btnConfirmed_colored)
        btnInTransit = findViewById(R.id.btnInTransit)
        btnInTransit_Colored = findViewById(R.id.btnInTransit_colored)
        btnDelivered = findViewById(R.id.btnDelivered)
        btnDelivered_Colored = findViewById(R.id.btnDelivered_colored)

        // retrieving details from intent
        val intent = intent
        orderID = intent.getStringExtra("orderId").toString()
        fuelName = intent.getStringExtra("fuelTitle").toString()
        fuelUnitPrice = intent.getStringExtra("fuelUnitPrice").toString()
        fuelQuantity = intent.getStringExtra("fuelQuantitySelected").toString()
        fuelFinalPrice = intent.getStringExtra("finalPrice").toString()
        custID = intent.getStringExtra("custID").toString()
        custName = intent.getStringExtra("custName").toString()
        custPhone = intent.getStringExtra("custPhone").toString()
        custAddress = intent.getStringExtra("custAddress").toString()
        dateTimePlaced = intent.getStringExtra("dateTimeOrdered").toString()
        transMode = intent.getStringExtra("transactionMode").toString()
        transMessage = intent.getStringExtra("transactionID").toString()
        orderStatus = intent.getStringExtra("orderStatus").toString()
        key = intent.getStringExtra("key").toString()

        // setting values to the Text Views
        txtOrderID.text = "Order ID: #$orderID"

        txtFuelName.text = fuelName
        txtFuelUnitPrice.text = fuelUnitPrice
        txtQuantityFinalPrice.text = "$fuelQuantity Litres | ₹  $fuelFinalPrice"

        txtCustID.text = custID
        txtCustName.text = custName
        txtCustPhone.text = custPhone
        txtCustAddress.text = custAddress

        txtDateTime.text = dateTimePlaced
        txtTransMode.text = transMode

        // Transaction Status
        if(transMode == "COD") {

            txtTransMessage.text = "Collect Amount"
            txtTransMessage.compoundDrawableTintList = ColorStateList.valueOf(resources.getColor(R.color.red))
            txtTransMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_warning_24,0,0,0)

        } else {

            btnRecvdPayment.visibility = View.GONE

            txtTransMessage.text = transMessage
            txtTransMessage.setTextColor(resources.getColor(R.color.green))
            txtTransMessage.compoundDrawableTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
            txtTransMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24,0,0,0)

        }

        // Payment Status
        btnRecvdPayment.setOnClickListener {

            // Change the Transaction Status - Paid (COD - Paid)
            db.collection("Orders_Data")
                .document(key)
                .update(mapOf(
                    "transactionID" to "Received Amount",
                    "transactionMode" to "COD-paid"
                ))
                .addOnSuccessListener {

                    btnRecvdPayment.visibility = View.GONE

                    txtTransMessage.text = "Amount Received"
                    txtTransMessage.setTextColor(resources.getColor(R.color.green))
                    txtTransMessage.compoundDrawableTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
                    txtTransMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24,0,0,0)

                    //  btnRecvdPayment_colored.visibility = View.VISIBLE
                    Toast.makeText(this, "Changes saved Successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { Toast.makeText(this, it.localizedMessage!!.toString(), Toast.LENGTH_LONG).show() }
        }

        // Call Intent
        btnCall.setOnClickListener {

            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$custPhone")
            startActivity(dialIntent)

        }

        // Message intent
        btnLocate.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$custAddress"))
            startActivity(intent)
        }

        // Order Status
        when(orderStatus) {
            "Placed" -> {

                btnPlaced_colored.visibility = View.VISIBLE
                btnPlaced.visibility = View.GONE
            }

            "Confirmed" -> {
                btnConfirmed_colored.visibility = View.VISIBLE
                btnConfirmed.visibility = View.GONE
            }

            "In Transit" -> {
                btnInTransit_Colored.visibility = View.VISIBLE
                btnInTransit.visibility = View.GONE
            }

            "Delivered" -> {
                btnDelivered_Colored.visibility = View.VISIBLE
                btnDelivered.visibility = View.GONE
            }
            else -> {
                // do nothing
                Toast.makeText(this, "Some Error Occurred! Unable to retrieve Order Status.", Toast.LENGTH_LONG).show()
            }
        }

        // Confirmed Button onClick
        btnConfirmed.setOnClickListener {

            db.collection("Orders_Data")
                .document(key)
                .update("orderStatus","Confirmed")
                .addOnSuccessListener {

                    sendIntent()

                    btnConfirmed_colored.visibility = View.VISIBLE
                    btnConfirmed.visibility = View.GONE
                    btnConfirmed_colored.isEnabled = false

                }
        }

        // In Transit Button onClick
        btnInTransit.setOnClickListener {

            db.collection("Orders_Data")
                .document(key)
                .update("orderStatus","In Transit")
                .addOnSuccessListener {

                    sendIntent()

                    btnInTransit_Colored.visibility = View.VISIBLE
                    btnInTransit.visibility = View.GONE
                    btnInTransit_Colored.isEnabled = false
                }
        }

        // Delivered Button onClick
        btnDelivered.setOnClickListener {

            db.collection("Orders_Data")
                .document(key)
                .update("orderStatus","Delivered")
                .addOnSuccessListener {

                    sendIntent()

                    btnDelivered_Colored.visibility = View.VISIBLE
                    btnDelivered.visibility = View.GONE
                    btnDelivered_Colored.isEnabled = false
                }
        }

    }

    fun sendIntent() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}