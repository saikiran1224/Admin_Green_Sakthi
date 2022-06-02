package com.greensakthi.administrator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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

class MyOrdersAdapter(private val context: Context, private val myOrdersList: ArrayList<OrderData>):
    RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.layout_order_info,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.fuelTitle.text = myOrdersList[position].fuelName
        holder.orderID.text = "#" + myOrdersList[position].orderID
        holder.orderStatus.text = myOrdersList[position].orderStatus
        holder.quantity_UnitPrice.text = myOrdersList[position].fuelQuantitySelected + " x " + myOrdersList[position].fuelUnitPrice
        holder.finalPrice.text = "₹ " + myOrdersList[position].finalPrice

        if (myOrdersList[position].transactionMode == "COD") {

            holder.relativeLayout.setBackgroundColor(context.resources.getColor(R.color.due_bg))
        } else {

            holder.relativeLayout.setBackgroundColor(context.resources.getColor(R.color.edt_bg))

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

}