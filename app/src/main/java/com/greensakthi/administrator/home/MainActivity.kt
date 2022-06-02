package com.greensakthi.administrator.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.greensakthi.administrator.R
import com.greensakthi.administrator.adapters.MyOrdersAdapter
import com.greensakthi.administrator.models.OrderData
import com.greensakthi.administrator.utils.AppPreferences

class MainActivity : AppCompatActivity() {

    lateinit var myOrdersRecycler: RecyclerView
    lateinit var myOrdersList: ArrayList<OrderData>

    private var customerUUID: String = ""

    lateinit var noDataAnim: LottieAnimationView
    lateinit var txtNoDataAnim: TextView

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppPreferences.init(this)

        txtNoDataAnim = findViewById(R.id.txtNoDataAnim)
        noDataAnim = findViewById(R.id.no_data_anim)

        db = Firebase.firestore

        myOrdersRecycler = findViewById(R.id.myOrdersRecycler)
        myOrdersList = ArrayList()

        loadOrdersData()
    }

    override fun onRestart() {
        super.onRestart()

        loadOrdersData()
    }

    private fun loadOrdersData() {

        myOrdersList.clear()

        db.collection("Orders_Data")
            .get()
            .addOnSuccessListener {

                for(document in it.documents) {
                    val orderData = document.toObject<OrderData>()
                    myOrdersList.add(orderData!!)
                }

                if(!myOrdersList.isEmpty()) {

                    myOrdersRecycler.visibility = View.VISIBLE

                    txtNoDataAnim.visibility = View.GONE
                    noDataAnim.visibility = View.GONE

                    val myOrdersAdapter =  MyOrdersAdapter(this, myOrdersList)
                    val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                    myOrdersRecycler.adapter = myOrdersAdapter
                    myOrdersRecycler.layoutManager = linearLayoutManager
                    myOrdersAdapter.notifyDataSetChanged()

                } else {
                    // No Data in MyOrders
                    txtNoDataAnim.visibility = View.VISIBLE
                    noDataAnim.visibility = View.VISIBLE

                    myOrdersRecycler.visibility = View.GONE

                    //  Toast.makeText(context, "Sorry, No Orders found!", Toast.LENGTH_SHORT).show()
                }
            }

    }
}