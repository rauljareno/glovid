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
import com.cryptox.glovid.data.model.CategoryEnum
import com.cryptox.glovid.data.model.Subcategory
import com.cryptox.glovid.data.model.SubcategoryEnum
import com.cryptox.glovid.ui.category.SubcategoryActivity
import com.cryptox.glovid.ui.donation.NewDonationActivity
import com.cryptox.glovid.ui.errand.NewErrandActivity
import com.cryptox.glovid.viewModels.orders.OrderType

class SubcategoryAdapter(orderType: OrderType, category: CategoryEnum) : BaseAdapter() {
    private val list = subcategories(category)
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
        tv.text = parent.context?.getString(list[position].subcategoryName)
        image.setImageResource(list[position].subcategoryImage)

        view.setOnClickListener{
            if (orderType == OrderType.ASK) {
                val activity = parent.context as Activity
                val intent = Intent(activity, NewErrandActivity::class.java)
                intent.putExtra("Category", list[position].categoryType)
                intent.putExtra("Subcategory", list[position].subcategoryType)
                activity.startActivityForResult(intent, SubcategoryActivity.NEW_ORDER_REQUEST)
            } else {
                val activity = parent.context as Activity
                val intent = Intent(activity, NewDonationActivity::class.java)
                intent.putExtra("Category", list[position].categoryType)
                intent.putExtra("Subcategory", list[position].subcategoryType)
                activity.startActivityForResult(intent, SubcategoryActivity.NEW_ORDER_REQUEST)
            }
        }

        return view
    }

    override fun getItem(position: Int): Any? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    private fun subcategories(category: CategoryEnum): List<Subcategory> {
        return when (category) {
            CategoryEnum.CATEGORY_BUY -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_BUY_FOOD,
                        R.string.category_buy_food,
                        R.drawable.category_buy_food
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_BUY_PHARMACY,
                        R.string.category_buy_pharmacy,
                        R.drawable.category_buy_pharmacy
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_BUY_TOBACCO,
                        R.string.category_buy_tobacco,
                        R.drawable.category_buy_tobacco
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_BUY_KIOSK,
                        R.string.category_buy_kiosk,
                        R.drawable.category_buy_kiosk
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_BUY_PRESENT,
                        R.string.category_buy_present,
                        R.drawable.category_buy_present
                    )
                )
            }
            CategoryEnum.CATEGORY_FUN -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FUN_CHAT,
                        R.string.category_fun_chat,
                        R.drawable.category_fun_chat
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FUN_MOVIE,
                        R.string.category_fun_movie,
                        R.drawable.category_fun_movie
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FUN_SING,
                        R.string.category_fun_sing,
                        R.drawable.category_fun_sing
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FUN_DANCE,
                        R.string.category_fun_dance,
                        R.drawable.category_fun_dance
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FUN_PLAY,
                        R.string.category_fun_play,
                        R.drawable.category_fun_play
                    )
                )
            }
            CategoryEnum.CATEGORY_MAIL -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_MAIL_SEND_PACKAGE,
                        R.string.category_mail_send_package,
                        R.drawable.category_mail
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_MAIL_RECEIVE_PACKAGE,
                        R.string.category_mail_receive_package,
                        R.drawable.category_mail
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_MAIL_SEND_MONEY,
                        R.string.category_mail_send_money,
                        R.drawable.category_mail
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_MAIL_RECEIVE_MONEY,
                        R.string.category_mail_receive_money,
                        R.drawable.category_mail
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_MAIL_FAX_BUROFAX,
                        R.string.category_mail_fax_burofax,
                        R.drawable.category_mail
                    )
                )
            }
            CategoryEnum.CATEGORY_TRANSPORT -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_TRANSPORT_HOSPITAL,
                        R.string.category_transport_hospital,
                        R.drawable.category_transport
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_TRANSPORT_AIRPORT,
                        R.string.category_transport_airport,
                        R.drawable.category_transport
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_TRANSPORT_STATION,
                        R.string.category_transport_station,
                        R.drawable.category_transport
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_TRANSPORT_WORK,
                        R.string.category_transport_work,
                        R.drawable.category_transport
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_TRANSPORT_SHOPPING,
                        R.string.category_transport_shopping,
                        R.drawable.category_transport
                    )
                )
            }
            CategoryEnum.CATEGORY_HOMEWORK -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOMEWORK_MATH,
                        R.string.category_homework_math,
                        R.drawable.category_homework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOMEWORK_LANGUAGE,
                        R.string.category_homework_language,
                        R.drawable.category_homework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOMEWORK_BIOLOGY,
                        R.string.category_homework_biology,
                        R.drawable.category_homework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOMEWORK_PHYSICS,
                        R.string.category_homework_physics,
                        R.drawable.category_homework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOMEWORK_CHEMISTRY,
                        R.string.category_homework_chemistry,
                        R.drawable.category_homework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOMEWORK_HISTORY,
                        R.string.category_homework_history,
                        R.drawable.category_homework
                    )
                )
            }
            CategoryEnum.CATEGORY_ITEMS -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_ITEMS_TOOLS,
                        R.string.category_items_tools,
                        R.drawable.category_tools
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_ITEMS_KITCHEN,
                        R.string.category_items_kitchen,
                        R.drawable.category_tools
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_ITEMS_BATHROOM,
                        R.string.category_items_bathroom,
                        R.drawable.category_tools
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_ITEMS_CLOTHES,
                        R.string.category_items_clothes,
                        R.drawable.category_tools
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_ITEMS_CLEANING,
                        R.string.category_items_cleaning,
                        R.drawable.category_tools
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_ITEMS_TOYS,
                        R.string.category_items_toys,
                        R.drawable.category_tools
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_ITEMS_FOOD,
                        R.string.category_items_food,
                        R.drawable.category_tools
                    )
                )
            }
            CategoryEnum.CATEGORY_CARE -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_CARE_CHILDREN,
                        R.string.category_care_children,
                        R.drawable.category_care
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_CARE_ELDERS,
                        R.string.category_care_elders,
                        R.drawable.category_care
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_CARE_PETS,
                        R.string.category_care_pets,
                        R.drawable.category_care
                    )
                )
            }
            CategoryEnum.CATEGORY_SERVICES -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_SERVICES_DOCTOR,
                        R.string.category_services_doctor,
                        R.drawable.category_appointment
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_SERVICES_PHONE,
                        R.string.category_services_phone,
                        R.drawable.category_appointment
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_SERVICES_INTERNET,
                        R.string.category_services_internet,
                        R.drawable.category_appointment
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_SERVICES_ELECTRICITY,
                        R.string.category_services_electricity,
                        R.drawable.category_appointment
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_SERVICES_GAS,
                        R.string.category_services_gas,
                        R.drawable.category_appointment
                    )
                )
            }
            CategoryEnum.CATEGORY_PSYCHOLOGICAL_AID -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_PSYCHOLOGICAL_AID_DEPRESSION,
                        R.string.category_psychological_aid_depression,
                        R.drawable.category_psychological_aid
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_PSYCHOLOGICAL_AID_ANXIETY,
                        R.string.category_psychological_aid_anxiety,
                        R.drawable.category_psychological_aid
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_PSYCHOLOGICAL_AID_ABUSE,
                        R.string.category_psychological_aid_abuse,
                        R.drawable.category_psychological_aid
                    )
                )
            }
            CategoryEnum.CATEGORY_FIRST_AID -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FIRST_AID_BURN,
                        R.string.category_first_aid_burn,
                        R.drawable.category_first_aid
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FIRST_AID_CUT,
                        R.string.category_first_aid_cut,
                        R.drawable.category_first_aid
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FIRST_AID_BLEEDING,
                        R.string.category_first_aid_bleeding,
                        R.drawable.category_first_aid
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FIRST_AID_SUFFOCATION,
                        R.string.category_first_aid_suffocation,
                        R.drawable.category_first_aid
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_FIRST_AID_FAINT,
                        R.string.category_first_aid_faint,
                        R.drawable.category_first_aid
                    )
                )
            }
            CategoryEnum.CATEGORY_HOUSEWORK -> {
                listOf(
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOUSEWORK_FIXING,
                        R.string.category_housework_fixing,
                        R.drawable.category_housework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOUSEWORK_CLEANING,
                        R.string.category_housework_cleaning,
                        R.drawable.category_housework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOUSEWORK_WASHING_CLOTHES,
                        R.string.category_housework_washing_clothes,
                        R.drawable.category_housework
                    ),
                    Subcategory(
                        category,
                        SubcategoryEnum.SUBCATEGORY_HOUSEWORK_TRASH,
                        R.string.category_housework_trash,
                        R.drawable.category_housework
                    )
                )
            }
            else -> {
                listOf()
            }
        }
    }
}