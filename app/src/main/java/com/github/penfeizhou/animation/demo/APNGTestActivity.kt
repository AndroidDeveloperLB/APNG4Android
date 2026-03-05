package com.github.penfeizhou.animation.demo

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.github.penfeizhou.animation.demo.databinding.ActivityApnglibBinding

/**
 * @Description: 作用描述
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/29
 */
class APNGTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApnglibBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApnglibBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets: WindowInsetsCompat ->
            val navigationBarsWithIme = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime()
            )
            binding.appBarLayout.setPadding(
                navigationBarsWithIme.left,
                navigationBarsWithIme.top,
                navigationBarsWithIme.right,
                0
            )
            binding.root.setPadding(navigationBarsWithIme.left, 0, navigationBarsWithIme.right, 0)
            binding.scrollView.setPadding(
                binding.scrollView.paddingLeft,
                binding.scrollView.paddingTop,
                binding.scrollView.paddingRight,
                navigationBarsWithIme.bottom
            )
            insets
        }

        val linearLayout = binding.layout
        val urls = arrayOf(
            "file:///android_asset/test.avif",
            "file:///android_asset/wheel.avif",
            "file:///android_asset/world-cup.avif",
            "file:///android_asset/apng_detail_guide.png",
            "file:///android_asset/1.gif",
            "file:///android_asset/2.gif",
            "file:///android_asset/3.gif",
            "file:///android_asset/4.gif",
            "file:///android_asset/5.gif",
            "file:///android_asset/1.webp",
            "file:///android_asset/2.webp"
        )
        for (url in urls) {
            val imageView = ImageView(this)
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = 50
            layoutParams.topMargin = 50
            linearLayout.addView(imageView, layoutParams)
            Glide.with(imageView)
                .load(url)
                .into(imageView)
        }
    }
}
