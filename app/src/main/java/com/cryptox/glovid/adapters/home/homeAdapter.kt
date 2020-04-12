package com.cryptox.glovid.adapters.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.ui.donation.DonationRequestedActivity
import com.cryptox.glovid.ui.errand.ErrandAcceptedActivity
import com.cryptox.glovid.ui.order.OrderDetailsActivity
import com.cryptox.glovid.utils.putExtraJson
import kotlinx.android.synthetic.main.list_item_ask_home.view.*

class HomeAdapter(val items : ArrayList<Order>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_ask_home, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName?.text = items[position].id
        holder.tvDesc?.text = items[position].detail
        if (items[position].type == 1) {
            holder.btOrder?.text = context.getString(R.string.accept_errand)
            holder.btOrder?.setTextColor(context.getColor(android.R.color.white))
            holder.btOrder?.background = context.getDrawable(R.color.colorAccent)
            holder.btOrder?.setOnClickListener {
                val intent = Intent(context, ErrandAcceptedActivity::class.java)
                intent.putExtraJson("ORDER", items[position])
                context.startActivity(intent)
            }
            holder.icUser?.visibility = View.VISIBLE
            holder.rlUsers?.visibility = View.GONE
        } else if (items[position].type == 2) {
            holder.btOrder?.text = context.getString(R.string.request_donation)
            holder.btOrder?.setTextColor(context.getColor(R.color.colorAccent))
            holder.btOrder?.background = context.getDrawable(R.drawable.border_button_background)
            holder.btOrder?.setOnClickListener {
                val intent = Intent(context, DonationRequestedActivity::class.java)
                intent.putExtraJson("ORDER", items[position])
                context.startActivity(intent)
            }
            holder.icUser?.visibility = View.VISIBLE
            holder.rlUsers?.visibility = View.GONE
        } else if (items[position].type == 3) {
            holder.btOrder?.text = context.getString(R.string.accept_errand)
            holder.btOrder?.setTextColor(context.getColor(android.R.color.white))
            holder.btOrder?.background = context.getDrawable(R.color.colorAccent)
            holder.btOrder?.setOnClickListener {
                val intent = Intent(context, ErrandAcceptedActivity::class.java)
                intent.putExtraJson("ORDER", items[position])
                context.startActivity(intent)
            }
            holder.icUser?.visibility = View.GONE
            holder.rlUsers?.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtraJson("ORDER", items[position])
            context.startActivity(intent)
        }
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvName = view.tv_name
    val tvDesc = view.tv_description
    val btOrder = view.order_button
    val icUser = view.user_icon
    val rlUsers = view.users_icon
}