package com.greensakthi.administrator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.greensakthi.administrator.R
import com.greensakthi.administrator.home.OrderDetailsActivity
import com.greensakthi.administrator.models.OrderData
import java.text.NumberFormat
import java.util.*

class MyOrdersAdapter(private val context: Context, private var myOrdersList: List<OrderData>):
    RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.layout_order_info,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatTextViewDrawableApis")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.fuelTitle.text = myOrdersList[position].fuelName
        holder.orderID.text = "#" + myOrdersList[position].orderID
        holder.orderStatus.text = myOrdersList[position].orderStatus
        holder.quantity_UnitPrice.text = myOrdersList[position].fuelQuantitySelected + " x " + myOrdersList[position].fuelUnitPrice

        // for converting into Indian Currency
        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 2
        numberFormat.currency = Currency.getInstance("INR")

        holder.finalPrice.text = numberFormat.format(myOrdersList[position].finalPrice.toFloat()).toString()

        if (myOrdersList[position].transactionMode == "COD") {
            holder.relativeLayout.setBackgroundColor(context.resources.getColor(R.color.due_bg))
        } else {
            holder.relativeLayout.setBackgroundColor(context.resources.getColor(R.color.edt_bg))
        }

        val orderStatus = myOrdersList[position].orderStatus
        if(orderStatus == "Placed") {
            holder.orderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_placed, 0, 0, 0)
            holder.orderStatus.compoundDrawableTintList = ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else if(orderStatus == "Confirmed") {
            holder.orderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_confirmed_svgrepo_com, 0, 0, 0)
            holder.orderStatus.compoundDrawableTintList = ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else if(orderStatus == "In Transit") {
            holder.orderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_in_transit, 0, 0, 0)
            holder.orderStatus.compoundDrawableTintList = ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else {
            holder.orderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_delivered, 0, 0, 0)
            holder.orderStatus.compoundDrawableTintList = ColorStateList.valueOf(context.resources.getColor(R.color.white))
        }

        holder.txtTimeStamp.text = myOrdersList[position].dateTimePlaced

        holder.cardOrderInfo.setOnClickListener {

            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra("orderId",myOrdersList.get(position).orderID)
            intent.putExtra("custID", myOrdersList.get(position).custID)
            intent.putExtra("custName",myOrdersList.get(position).custName)
            intent.putExtra("custPhone", myOrdersList.get(position).custPhone)
            intent.putExtra("transactionID",myOrdersList.get(position).transactionID)
            intent.putExtra("transactionMode",myOrdersList.get(position).transactionMode)
            intent.putExtra("fuelTitle",myOrdersList.get(position).fuelName)
            intent.putExtra("fuelQuantitySelected", myOrdersList.get(position).fuelQuantitySelected)
            intent.putExtra("fuelUnitPrice", myOrdersList.get(position).fuelUnitPrice)
            intent.putExtra("dateTimeOrdered",myOrdersList.get(position).dateTimePlaced)
            intent.putExtra("custAddress", myOrdersList.get(position).address)
            intent.putExtra("orderStatus", myOrdersList.get(position).orderStatus)
            intent.putExtra("finalPrice", myOrdersList.get(position).finalPrice)
            intent.putExtra("key",myOrdersList.get(position).key)
            intent.putExtra("orderedFor", myOrdersList.get(position).orderedFor)
            intent.putExtra("vehicleName", myOrdersList.get(position).vehicleName)
            intent.putExtra("modelNumber", myOrdersList.get(position).modelNumber)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return myOrdersList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val fuelTitle = itemView.findViewById<TextView>(R.id.txtFuelName)
        val orderID = itemView.findViewById<TextView>(R.id.txtOrderId)
        val orderStatus = itemView.findViewById<TextView>(R.id.txtOrderStatus)
        val quantity_UnitPrice = itemView.findViewById<TextView>(R.id.quantity_UnitPrice)
        val finalPrice = itemView.findViewById<TextView>(R.id.txtFinalPrice)

        val txtTimeStamp = itemView.findViewById<TextView>(R.id.txtTimeStamp)

        val relativeLayout = itemView.findViewById<RelativeLayout>(R.id.headerLayout)

        val cardOrderInfo = itemView.findViewById<MaterialCardView>(R.id.cardOrderInfo)

    }

    // filter method for Search Option
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<OrderData?>) {
        myOrdersList = filteredList as java.util.ArrayList<OrderData>
        notifyDataSetChanged()
    }

}