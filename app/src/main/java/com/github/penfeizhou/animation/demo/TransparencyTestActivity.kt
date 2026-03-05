package com.github.penfeizhou.animation.demo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.github.penfeizhou.animation.demo.databinding.ActivityTransparencyTestBinding
import kotlin.random.Random

class TransparencyTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransparencyTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransparencyTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val navigationBarsWithIme =
                insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime())
            binding.appBarLayout.setPadding(
                navigationBarsWithIme.left,
                navigationBarsWithIme.top,
                navigationBarsWithIme.right,
                0
            )
            binding.root.setPadding(navigationBarsWithIme.left, 0, navigationBarsWithIme.right, 0)
            binding.scrollView.setPadding(0, 0, 0, navigationBarsWithIme.bottom)
            insets
        }

        Glide.with(this)
            .load(R.raw.animated_webp_with_transparency)
            .into(binding.imageView)

        binding.btnChangeBackground.setOnClickListener {
            val color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            binding.backgroundContainer.setBackgroundColor(color)
        }
    }
}
