package com.github.penfeizhou.animation.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.penfeizhou.animation.demo.databinding.ActivityFrameSequenceTestBinding

class FrameSequenceTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameSequenceTestBinding
    private val viewModel: FrameSequenceViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameSequenceTestBinding.inflate(layoutInflater)
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

        // List only files that have animation (based on common demo files)
        val animationFiles = listOf(
                "wheel.avif", "world-cup.avif",
                "apng_detail_guide.png", "test2.png",
                "1.gif", "2.gif", "3.gif", "4.gif", "5.gif", "6.gif", "world-cup.gif",
                "1.webp", "2.webp", "animated_webp_with_transparency.webp"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, animationFiles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiles.adapter = adapter

        binding.spinnerFiles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectFile(animationFiles[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnNextFrame.setOnClickListener {
            viewModel.nextFrame()
        }

        binding.btnNextFrameLoop.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.startLoop()
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    viewModel.stopLoop()
                    true
                }

                else -> false
            }
        }

        binding.btnRunPerformance.setOnClickListener {
            viewModel.runPerformanceTest()
        }

        // Observers
        viewModel.frameBitmap.observe(this) { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        }

        viewModel.frameInfo.observe(this) { info ->
            binding.tvFrameInfo.text = "Frame Info: $info"
        }

        viewModel.fileInfo.observe(this) { info ->
            binding.tvFileInfo.text = info
        }

        viewModel.performanceProgress.observe(this) { progress ->
            binding.pbPerformance.progress = progress
        }

        viewModel.performanceInfo.observe(this) { info ->
            binding.tvPerformanceInfo.text = "Performance Info: $info"
        }

        viewModel.isDecodingAll.observe(this) { isDecoding ->
            binding.btnRunPerformance.isEnabled = !isDecoding
            binding.btnRunPerformance.text = if (isDecoding) "Decoding..." else "Run Decode All Frames"
            binding.pbPerformance.visibility = if (isDecoding) View.VISIBLE else View.GONE
            binding.spinnerFiles.isEnabled = !isDecoding
            binding.btnNextFrame.isEnabled = !isDecoding
            binding.btnNextFrameLoop.isEnabled = !isDecoding
        }
    }
}
