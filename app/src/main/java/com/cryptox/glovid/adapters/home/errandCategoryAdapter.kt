package com.cryptox.glovid.adapters.home

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.cryptox.glovid.R

class ErrandCategoryAdapter : BaseAdapter() {
    private val list = categories()

    /*
        **** reference source developer.android.com ***

        View getView (int position, View convertView, ViewGroup parent)
            Get a View that displays the data at the specified position in the data set. You can
            either create a View manually or inflate it from an XML layout file. When the View
            is inflated, the parent View (GridView, ListView...) will apply default layout
            parameters unless you use inflate(int, android.view.ViewGroup, boolean)
            to specify a root view and to prevent attachment to the root.
    */
    override fun getView(position:Int, convertView: View?, parent: ViewGroup?):View {
        // Inflate the custom view
        val inflater = parent?.context?.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_errand_category,null)

        // Get the custom view widgets reference
        val tv = view.findViewById<TextView>(R.id.tv_name)
        val image = view.findViewById<ImageView>(R.id.pic)

        // Display category name on text view
        tv.text = list[position].first
        image.setImageResource(list[position].second)

        // Set background color for card view
        //card.setCardBackgroundColor(list[position].second)

        // Set a click listener for card view
        /*card.setOnClickListener{
            // Show selected color in a toast message
            Toast.makeText(parent.context,
                "Clicked : ${list[position].first}",Toast.LENGTH_SHORT).show()

            // Get the activity reference from parent
            val activity  = parent.context as Activity

            // Get the activity root view
            val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
                .getChildAt(0)

            // Change the root layout background color
            viewGroup.setBackgroundColor(list[position].second)
        }*/

        // Finally, return the view
        return view
    }

    /*
        **** reference source developer.android.com ***

        Object getItem (int position)
            Get the data item associated with the specified position in the data set.

        Parameters
            position int : Position of the item whose data we want within the adapter's data set.
        Returns
            Object : The data at the specified position.
    */
    override fun getItem(position: Int): Any? {
        return list[position]
    }

    /*
        **** reference source developer.android.com ***

        long getItemId (int position)
            Get the row id associated with the specified position in the list.

        Parameters
            position int : The position of the item within the adapter's data
                           set whose row id we want.
        Returns
            long : The id of the item at the specified position.
    */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Count the items
    override fun getCount(): Int {
        return list.size
    }

    // Custom method to generate list of color name value pair
    private fun categories():List<Pair<String, Int>>{
        return listOf(
            Pair("Compra", R.drawable.category_buy),
            Pair("Entretenimiento", R.drawable.category_entertainment),
            Pair("Correos", R.drawable.category_mail),
            Pair("Transporte", R.drawable.category_transport),
            Pair("Deberes", R.drawable.category_homework),
            Pair("Utensilios", R.drawable.category_tools),
            Pair("Cuidado", R.drawable.category_care),
            Pair("Gestión de citas", R.drawable.category_appointment),
            Pair("Pasear mascotas", R.drawable.category_pet_walking),
            Pair("Ayuda Psicológica", R.drawable.category_psychological_aid),
            Pair("Primeros Auxilios", R.drawable.category_first_aid),
            Pair("Tareas del hogar", R.drawable.category_housework),
            Pair("Otros", R.drawable.category_others)
        )
    }

    private fun subcategories(category: String):List<String> {
        return if (category == "Compra") {
            listOf(
                "Alimentación",
                "Farmacia",
                "Estanco",
                "Quiosco",
                "Regalo"
            )
        } else if (category == "Entretenimiento") {
            listOf(
                "Conversar",
                "Recomendación Serie/Película",
                "Cantar",
                "Bailar",
                "Jugar"
            )
        } else if (category == "Tareas del hogar") {
            listOf(
                "Arreglar desperfectos",
                "Limpieza del hogar",
                "Lavar ropa",
                "Sacar la basura"
            )
        } else if (category == "Cuidado") {
            listOf(
                "Niños",
                "Ancianos",
                "Mascotas"
            )
        } else {
            listOf()
        }
    }
}