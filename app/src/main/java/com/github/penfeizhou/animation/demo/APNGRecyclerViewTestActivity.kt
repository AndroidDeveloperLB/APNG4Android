package com.github.penfeizhou.animation.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.penfeizhou.animation.demo.databinding.ActivityApngListTestBinding

class APNGRecyclerViewTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApngListTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApngListTestBinding.inflate(layoutInflater)
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
            binding.rv.setPadding(
                binding.rv.paddingLeft,
                binding.rv.paddingTop,
                binding.rv.paddingRight,
                navigationBarsWithIme.bottom
            )
            insets
        }

        binding.rv.layoutManager = GridLayoutManager(this, 3)
        binding.rv.adapter = TestAdapter(this)
    }
}
