package com.cryptox.glovid.adapters.category

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Category
import com.cryptox.glovid.data.model.CategoryEnum
import com.cryptox.glovid.ui.category.CategoryActivity.Companion.SUBCATEGORY_REQUEST
import com.cryptox.glovid.ui.category.SubcategoryActivity
import com.cryptox.glovid.ui.category.SubcategoryActivity.Companion.NEW_ORDER_REQUEST
import com.cryptox.glovid.ui.donation.NewDonationActivity
import com.cryptox.glovid.ui.errand.NewErrandActivity
import com.cryptox.glovid.viewModels.orders.OrderType

class CategoryAdapter(orderType: OrderType) : BaseAdapter() {
    private val list = categories()
    private val orderType = orderType

    override fun getView(position:Int, convertView: View?, parent: ViewGroup?):View {
        // Inflate the custom view
        val inflater = parent?.context?.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_errand_category,null)

        // Get the custom view widgets reference
        val tv = view.findViewById<TextView>(R.id.tv_name)
        val image = view.findViewById<ImageView>(R.id.pic)

        // Display category name on text view
        tv.text = parent.context?.getString(list[position].categoryName)
        image.setImageResource(list[position].categoryImage)

        view.setOnClickListener{
            if (list[position].categoryType != CategoryEnum.CATEGORY_OTHERS && list[position].categoryType != CategoryEnum.CATEGORY_PET_WALKING) {
                // Get the activity reference from parent
                val activity = parent.context as Activity
                val intent = Intent(activity, SubcategoryActivity::class.java)
                intent.putExtra("Category", list[position].categoryType)
                intent.putExtra("OrderType", orderType)
                activity.startActivityForResult(intent, SUBCATEGORY_REQUEST)
            } else {
                if (orderType == OrderType.ASK) {
                    val activity = parent.context as Activity
                    val intent = Intent(activity, NewErrandActivity::class.java)
                    intent.putExtra("Category", list[position].categoryType)
                    activity.startActivityForResult(intent, NEW_ORDER_REQUEST)
                } else {
                    val activity = parent.context as Activity
                    val intent = Intent(activity, NewDonationActivity::class.java)
                    intent.putExtra("Category", list[position].categoryType)
                    activity.startActivityForResult(intent, SubcategoryActivity.NEW_ORDER_REQUEST)
                }
            }
        }

        // Finally, return the view
        return view
    }

    override fun getItem(position: Int): Any? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Count the items
    override fun getCount(): Int {
        return list.size
    }

    // Custom method to generate list of color name value pair
    private fun categories():List<Category>{
        return listOf(
            Category(CategoryEnum.CATEGORY_BUY, R.string.category_buy, R.drawable.category_buy),
            Category(CategoryEnum.CATEGORY_FUN, R.string.category_entertainment, R.drawable.category_fun),
            Category(CategoryEnum.CATEGORY_MAIL, R.string.category_mail, R.drawable.category_mail),
            Category(CategoryEnum.CATEGORY_TRANSPORT, R.string.category_transport, R.drawable.category_transport),
            Category(CategoryEnum.CATEGORY_HOMEWORK, R.string.category_homework, R.drawable.category_homework),
            Category(CategoryEnum.CATEGORY_ITEMS, R.string.category_items, R.drawable.category_tools),
            Category(CategoryEnum.CATEGORY_CARE, R.string.category_care, R.drawable.category_care),
            Category(CategoryEnum.CATEGORY_SERVICES, R.string.category_appointment, R.drawable.category_appointment),
            Category(CategoryEnum.CATEGORY_PET_WALKING, R.string.category_pet_walking, R.drawable.category_pet_walking),
            Category(CategoryEnum.CATEGORY_PSYCHOLOGICAL_AID, R.string.category_psychological_aid, R.drawable.category_psychological_aid),
            Category(CategoryEnum.CATEGORY_FIRST_AID, R.string.category_first_aid, R.drawable.category_first_aid),
            Category(CategoryEnum.CATEGORY_HOUSEWORK, R.string.category_housework, R.drawable.category_housework),
            Category(CategoryEnum.CATEGORY_OTHERS, R.string.category_others, R.drawable.category_more)
        )
    }
}