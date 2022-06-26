package com.greensakthi.administrator.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.greensakthi.administrator.R
import com.greensakthi.administrator.adapters.MyOrdersAdapter
import com.greensakthi.administrator.models.OrderData
import com.greensakthi.administrator.onboarding.LoginActivity
import com.greensakthi.administrator.utils.AppPreferences
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var myOrdersRecycler: RecyclerView
    lateinit var myOrdersList: ArrayList<OrderData>

    private var customerUUID: String = ""

    lateinit var txtHelloAdmin: TextView

    lateinit var noDataAnim: LottieAnimationView
    lateinit var txtNoDataAnim: TextView

    lateinit var mainLayout: RelativeLayout
    lateinit var loadingAnim: LottieAnimationView

    lateinit var db: FirebaseFirestore

    lateinit var edtSearchOrders: EditText

    lateinit var refreshIcon: ImageView
    lateinit var logoutBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppPreferences.init(this)

        txtNoDataAnim = findViewById(R.id.txtNoDataAnim)
        noDataAnim = findViewById(R.id.no_data_anim)

        logoutBtn = findViewById(R.id.logoutBtn)

        txtHelloAdmin = findViewById(R.id.txtHelloAdmin)
        txtHelloAdmin.isSelected = true

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
            Toast.makeText(this, "Updated data few seconds ago...", Toast.LENGTH_LONG).show()

        }

        logoutBtn.setOnClickListener {

            val logoutDialog = Dialog(this)
            logoutDialog.setContentView(R.layout.logout_dialog)
            logoutDialog.setCancelable(false)
            logoutDialog.setCanceledOnTouchOutside(false)
            logoutDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            logoutDialog.findViewById<Button>(R.id.btnLogout).setOnClickListener{
                // TODO: Ask user Logout dialog once again

                AppPreferences.isLogin = false

                // logging out from Firebase
                FirebaseAuth.getInstance().signOut()

                // redirecting back to Get Started Page
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }

            logoutDialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                logoutDialog.dismiss()
            }

            logoutDialog.show()


        }
    }

    override fun onRestart() {
        super.onRestart()

        loadOrdersData()
    }

    @SuppressLint("SetTextI18n")
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

                    // calculating total amount earned
                    var totalAmount = 0.00f

                    for(order in myOrdersList) totalAmount+=order.finalPrice.toFloat()

                    // for converting into Indian Currency
                    val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
                    numberFormat.maximumFractionDigits = 2
                    numberFormat.currency = Currency.getInstance("INR")

                    // Disabling the loading Animation
                    loadingAnim.visibility = View.GONE
                    mainLayout.visibility = View.VISIBLE

                    myOrdersRecycler.visibility = View.VISIBLE

                    txtNoDataAnim.visibility = View.GONE
                    noDataAnim.visibility = View.GONE

                    // setting the count of orders in Hello Admin text
                    txtHelloAdmin.text = "Total Earned ${numberFormat.format(totalAmount)} for ${myOrdersList.size} Order(s)"

                    // sorting the orders
                    val sortedList = myOrdersList.sortedWith(compareByDescending { it.dateTimePlaced })

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