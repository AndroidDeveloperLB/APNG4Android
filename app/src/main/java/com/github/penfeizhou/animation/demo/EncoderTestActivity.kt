package com.github.penfeizhou.animation.demo

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.github.penfeizhou.animation.apng.APNGDrawable
import com.github.penfeizhou.animation.awebpencoder.WebPEncoder
import com.github.penfeizhou.animation.demo.databinding.ActivityApnglibBinding
import kotlin.concurrent.thread

/**
 * @Description: 作用描述
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/29
 */
class EncoderTestActivity : AppCompatActivity() {
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
        val imageView = ImageView(this)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = 50
        layoutParams.topMargin = 50
        linearLayout.addView(imageView, layoutParams)

        thread {
            val ret = WebPEncoder.fromDecoder(
                APNGDrawable.fromAsset(
                    this@EncoderTestActivity,
                    "test2.png"
                ).frameSeqDecoder
            ).build()
            imageView.post {
                Glide.with(imageView)
                    .load(ret)
                    .into(imageView)
            }
        }
    }
}
