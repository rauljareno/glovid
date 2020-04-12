package com.cryptox.glovid.adapters.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.cryptox.glovid.R

class OnboardingAdapter(private val mContext: Context) : PagerAdapter() {
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val modelObject = Model.values()[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(modelObject.layoutResId, collection, false) as ViewGroup
        collection.addView(layout)
        return layout
    }
    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }
    override fun getCount(): Int {
        return Model.values().size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    override fun getPageTitle(position: Int): CharSequence {
        val customPagerEnum = Model.values()[position]
        return mContext.getString(customPagerEnum.titleResId)
    }
}

enum class Model private constructor(val iconResId: Int, val titleResId: Int, val layoutResId: Int) {
    ASK(R.drawable.ringbell_white, R.string.home_ask, R.layout.layout_onboarding_1),
    ERRAND(R.drawable.errand_white, R.string.home_errand, R.layout.layout_onboarding_2),
    DONATE(R.drawable.donate_white, R.string.home_donate, R.layout.layout_onboarding_3)
}