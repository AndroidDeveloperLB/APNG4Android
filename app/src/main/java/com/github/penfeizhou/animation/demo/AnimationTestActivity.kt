package com.github.penfeizhou.animation.demo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.penfeizhou.animation.apng.APNGDrawable
import com.github.penfeizhou.animation.avif.AVIFDrawable
import com.github.penfeizhou.animation.demo.databinding.ActivityApnglibBinding
import com.github.penfeizhou.animation.gif.GifDrawable
import com.github.penfeizhou.animation.webp.WebPDrawable

/**
 * @Description: 作用描述
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/29
 */
class AnimationTestActivity : AppCompatActivity() {
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
        val files = intent.getStringArrayExtra("files")
        if (files != null) {
            for (assetFile in files) {
                val imageView = ImageView(this)
                var drawable: Drawable? = null
                if (assetFile.endsWith("png")) {
                    drawable = APNGDrawable.fromAsset(this, assetFile)
                }
                if (assetFile.endsWith("webp")) {
                    drawable = WebPDrawable.fromAsset(this, assetFile)
                }
                if (assetFile.endsWith("gif")) {
                    drawable = GifDrawable.fromAsset(this, assetFile)
                }
                if (assetFile.endsWith("avif")) {
                    drawable = AVIFDrawable.fromAsset(this, assetFile)
                }
                imageView.setImageDrawable(drawable)
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.bottomMargin = 50
                layoutParams.topMargin = 50
                linearLayout.addView(imageView, layoutParams)
            }
        }
    }
}
