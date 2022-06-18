package com.greensakthi.administrator.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
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
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var myOrdersRecycler: RecyclerView
    lateinit var myOrdersList: ArrayList<OrderData>

    private var customerUUID: String = ""

    lateinit var noDataAnim: LottieAnimationView
    lateinit var txtNoDataAnim: TextView

    lateinit var mainLayout: RelativeLayout
    lateinit var loadingAnim: LottieAnimationView

    lateinit var db: FirebaseFirestore

    lateinit var edtSearchOrders: EditText

    lateinit var refreshIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppPreferences.init(this)

        txtNoDataAnim = findViewById(R.id.txtNoDataAnim)
        noDataAnim = findViewById(R.id.no_data_anim)

        mainLayout = findViewById(R.id.mainLayout)
        loadingAnim = findViewById(R.id.loadingAnim)

        refreshIcon = findViewById(R.id.refreshIcon)

        db = Firebase.firestore

        edtSearchOrders = findViewById(R.id.edtSearchOrder)
        // emptying the Edit Text
        edtSearchOrders.setText("")

        myOrdersRecycler = findViewById(R.id.myOrdersRecycler)
        myOrdersList = ArrayList()


        mainLayout.visibility = View.GONE
        loadingAnim.visibility = View.VISIBLE

        // loading the orders Data
        loadOrdersData()

        refreshIcon.setOnClickListener{

            loadOrdersData()
            Toast.makeText(this, "Updated data", Toast.LENGTH_LONG).show()

        }
    }

    override fun onRestart() {
        super.onRestart()

        loadOrdersData()
    }

    private fun loadOrdersData() {

       // myOrdersList.clear()

        db.collection("Orders_Data")
            .get()
            .addOnSuccessListener {

                myOrdersList.clear()

                for(document in it.documents) {
                    val orderData = document.toObject<OrderData>()
                    myOrdersList.add(orderData!!)
                }

                if(!myOrdersList.isEmpty()) {

                    // Disabling the loading Animation
                    loadingAnim.visibility = View.GONE
                    mainLayout.visibility = View.VISIBLE

                    myOrdersRecycler.visibility = View.VISIBLE

                    txtNoDataAnim.visibility = View.GONE
                    noDataAnim.visibility = View.GONE

                    // sorting the orders
                    val sortedList = myOrdersList.sortedWith(compareByDescending { it.dateTimePlaced }, ) as List<OrderData>

                    val myOrdersAdapter =  MyOrdersAdapter(this, sortedList)
                    val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                    myOrdersRecycler.adapter = myOrdersAdapter
                    myOrdersRecycler.setHasFixedSize(true)
                    myOrdersRecycler.layoutManager = linearLayoutManager

                    // search EditText Listener
                    edtSearchOrders.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                        override fun afterTextChanged(s: Editable) {

                            // filtering the data based on the search phrase
                            val filteredlist: ArrayList<OrderData> = ArrayList()
                            for (item in sortedList) {
                                if (item.fuelQuantitySelected.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                        Locale.getDefault())) ||
                                    item.dateTimePlaced.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                        Locale.getDefault())) ||
                                    item.orderStatus.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                        Locale.getDefault())) ||
                                    item.fuelName.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                        Locale.getDefault())) ||
                                    item.finalPrice.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                        Locale.getDefault())) ||
                                    item.orderID.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                        Locale.getDefault()))) {

                                    filteredlist.add(item)

                                }
                            }
                            myOrdersAdapter.filterList(filteredlist)

                        }
                    })

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