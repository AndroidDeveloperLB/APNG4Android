package com.github.penfeizhou.animation.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.penfeizhou.animation.demo.databinding.ActivityComposeBinding


/**
 *
 * @Description:    ComposeActivity
 * @Author:         pengfei.zhou
 * @CreateDate:     2023/9/6
 */
class ComposeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComposeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComposeBinding.inflate(layoutInflater)
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
            binding.composeView.updatePadding(bottom = navigationBarsWithIme.bottom)
            insets
        }

        binding.composeView.setContent {
            AnimationDemo()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimationDemo() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)
    ) {
        GlideImage(
            model = "file:///android_asset/test.avif",
            contentDescription = "Test",
        )
        GlideImage(
            model = "file:///android_asset/1.webp",
            contentDescription = "Test",
        )
        GlideImage(
            model = "file:///android_asset/test1.png",
            contentDescription = "Test",
        )
        GlideImage(
            model = "file:///android_asset/2.gif",
            contentDescription = "Test",
        )
    }
}
