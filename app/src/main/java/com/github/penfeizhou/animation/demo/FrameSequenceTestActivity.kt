package com.github.penfeizhou.animation.demo

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.penfeizhou.animation.apng.decode.APNGDecoder
import com.github.penfeizhou.animation.apng.decode.APNGParser
import com.github.penfeizhou.animation.avif.decode.AVIFDecoder
import com.github.penfeizhou.animation.avif.decode.AVIFParser
import com.github.penfeizhou.animation.decode.FrameSeqDecoder
import com.github.penfeizhou.animation.demo.databinding.ActivityFrameSequenceTestBinding
import com.github.penfeizhou.animation.gif.decode.GifDecoder
import com.github.penfeizhou.animation.gif.decode.GifParser
import com.github.penfeizhou.animation.loader.AssetStreamLoader
import com.github.penfeizhou.animation.loader.Loader
import com.github.penfeizhou.animation.loader.ResourceStreamLoader
import com.github.penfeizhou.animation.webp.decode.WebPDecoder
import com.github.penfeizhou.animation.webp.decode.WebPParser

class FrameSequenceTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameSequenceTestBinding
    private var decoder: FrameSeqDecoder<*, *>? = null
    private var currentBitmap: Bitmap? = null
    private var currentFrameIndex = 0
    private var totalFrames = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameSequenceTestBinding.inflate(layoutInflater)
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

        setupSpinner()

        binding.btnNextFrame.setOnClickListener {
            decodeNextFrame()
        }
    }

    private fun setupSpinner() {
        val assetFiles = try {
            assets.list("")?.filter {
                it.endsWith(".gif") || it.endsWith(".webp") || it.endsWith(".png") || it.endsWith(".avif")
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val rawFiles = listOf(
            "RAW: animated_gif",
            "RAW: animated_webp",
            "RAW: animated_webp_with_transparency"
        )

        val allFiles = rawFiles + assetFiles
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, allFiles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiles.adapter = adapter

        binding.spinnerFiles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = allFiles[position]
                initDecoder(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initDecoder(fileName: String) {
        decoder?.stop()
        decoder = null
        currentFrameIndex = 0
        
        val loader: Loader = if (fileName.startsWith("RAW: ")) {
            val resId = when (fileName.substringAfter("RAW: ")) {
                "animated_gif" -> R.raw.animated_gif
                "animated_webp" -> R.raw.animated_webp
                "animated_webp_with_transparency" -> R.raw.animated_webp_with_transparency
                else -> return
            }
            ResourceStreamLoader(this, resId)
        } else {
            AssetStreamLoader(this, fileName)
        }

        decoder = createDecoder(loader)
        if (decoder != null) {
            try {
                totalFrames = decoder!!.prepareSequentialDecode()
                val bounds = decoder!!.bounds
                binding.tvFileInfo.text = "File Info: ${bounds.width()}x${bounds.height()}, Total Frames: $totalFrames"
                binding.tvFrameInfo.text = "Frame Info: Press Next Frame"
                
                // Prepare bitmap for decoding
                currentBitmap?.recycle()
                currentBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888)
                binding.imageView.setImageBitmap(null)
            } catch (e: Exception) {
                binding.tvFileInfo.text = "Error: ${e.message}"
            }
        }
    }

    private fun createDecoder(loader: Loader): FrameSeqDecoder<*, *>? {
        val reader = loader.obtain()
        return try {
            when {
                WebPParser.isAWebP(reader) -> WebPDecoder(loader, null)
                APNGParser.isAPNG(reader.also { it.reset() }) -> APNGDecoder(loader, null)
                GifParser.isGif(reader.also { it.reset() }) -> GifDecoder(loader, null)
                AVIFParser.isAVIF(reader.also { it.reset() }) -> AVIFDecoder(loader, null)
                else -> null
            }
        } finally {
            reader.close()
        }
    }

    private fun decodeNextFrame() {
        val decoder = this.decoder ?: return
        val bitmap = this.currentBitmap ?: return

        try {
            if (currentFrameIndex >= totalFrames) {
                // Restart from beginning if needed, though prepareSequentialDecode might need to be called again
                // For this sample, we'll just stop at the end or you'd need to re-init.
                // Simple approach: re-init to beginning
                val selected = binding.spinnerFiles.selectedItem as String
                initDecoder(selected)
                return
            }

            val duration = decoder.nextFrame(bitmap)
            if (duration >= 0) {
                binding.imageView.setImageBitmap(bitmap)
                binding.tvFrameInfo.text = "Frame Info: Index: $currentFrameIndex, Duration: $duration ms, Size: ${bitmap.width}x${bitmap.height}"
                currentFrameIndex++
            } else {
                binding.tvFrameInfo.text = "Frame Info: End of sequence"
            }
        } catch (e: Exception) {
            binding.tvFrameInfo.text = "Error: ${e.message}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        decoder?.stop()
        currentBitmap?.recycle()
    }
}
