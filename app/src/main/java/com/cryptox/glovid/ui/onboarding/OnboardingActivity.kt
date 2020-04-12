package com.cryptox.glovid.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

import com.cryptox.glovid.R
import com.cryptox.glovid.adapters.onboarding.OnboardingAdapter
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {

    private val MIN_SCALE = 0.65f
    private val MIN_ALPHA = 0.3f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)
        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        viewPager.adapter = OnboardingAdapter(this)
        supportActionBar?.hide()

        val closeBtn = findViewById<ImageView>(R.id.close_btn)
        closeBtn.setOnClickListener {
            finish()
        }

        viewPager.setPageTransformer(true, this::zoomOutTransformation)
        viewPager.addOnPageChangeListener(ViewPagerListener(this::onPageSelected))
    }

    private fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                firstDotImageView.setImageResource(R.drawable.circle)
                secondDotImageView.setImageResource(R.drawable.circle_disabled)
                thirdDotImageView.setImageResource(R.drawable.circle_disabled)
            }
            1 -> {
                firstDotImageView.setImageResource(R.drawable.circle_disabled)
                secondDotImageView.setImageResource(R.drawable.circle)
                thirdDotImageView.setImageResource(R.drawable.circle_disabled)
            }
            2 -> {
                firstDotImageView.setImageResource(R.drawable.circle_disabled)
                secondDotImageView.setImageResource(R.drawable.circle_disabled)
                thirdDotImageView.setImageResource(R.drawable.circle)
            }
        }
    }

    private fun zoomOutTransformation(page: View, position: Float) {
        when {
            position < -1 ->
                page.alpha = 0f
            position <= 1 -> {
                page.scaleX = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.scaleY = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.alpha = Math.max(MIN_ALPHA, 1 - Math.abs(position))
            }
            else -> page.alpha = 0f
        }
    }

    class ViewPagerListener(private val closure: (Int) -> Unit) : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {
        }
        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }
        override fun onPageSelected(position: Int) = closure(position)
    }

}
