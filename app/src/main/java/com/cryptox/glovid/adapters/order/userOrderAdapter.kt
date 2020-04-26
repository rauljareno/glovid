package com.cryptox.glovid.adapters.order

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.viewModels.orders.OrderStatus
import com.cryptox.glovid.viewModels.orders.OrderType
import kotlinx.android.synthetic.main.item_user_order.view.*

class UserOrderAdapter(val items : ArrayList<Order>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_order, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items[position].type == OrderType.ASK.toString()) {
            holder.tvName?.text = context.getString(R.string.ask)
            holder.tvDesc?.text = String.format(context.getString(R.string.user_needs), items[position].detail)
            holder.icUser?.setImageResource(R.drawable.ask)

        } else if (items[position].type == OrderType.GIVE.toString()) {
            holder.tvName?.text = context.getString(R.string.donation)
            holder.tvDesc?.text = String.format(context.getString(R.string.user_offers), items[position].detail)
            holder.icUser?.setImageResource(R.drawable.donate)
        }
        if (items[position].status == OrderStatus.PENDING.toString()) {
            holder.icStatus?.setImageResource(R.drawable.order_status_pending)
            holder.tvStatus?.text = context.getString(R.string.order_status_pending)
        }
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvName = view.tv_name
    val tvDesc = view.tv_description
    val icUser = view.user_icon
    val icStatus = view.image_status
    val tvStatus = view.tv_status
}