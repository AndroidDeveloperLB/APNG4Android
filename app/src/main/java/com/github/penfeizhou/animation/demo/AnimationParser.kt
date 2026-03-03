package com.github.penfeizhou.animation.demo

import android.R.attr.height
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.github.penfeizhou.animation.apng.decode.APNGDecoder
import com.github.penfeizhou.animation.apng.decode.APNGParser
import com.github.penfeizhou.animation.avif.decode.AVIFDecoder
import com.github.penfeizhou.animation.avif.decode.AVIFParser
import com.github.penfeizhou.animation.decode.FrameSeqDecoder
import com.github.penfeizhou.animation.gif.decode.GifDecoder
import com.github.penfeizhou.animation.gif.decode.GifParser
import com.github.penfeizhou.animation.loader.ByteBufferLoader
import com.github.penfeizhou.animation.webp.decode.WebPDecoder
import com.github.penfeizhou.animation.webp.decode.WebPParser
import java.nio.ByteBuffer

import com.github.penfeizhou.animation.loader.Loader
import com.github.penfeizhou.animation.loader.StreamLoader
import java.io.InputStream
import com.github.penfeizhou.animation.loader.ResourceStreamLoader

object AnimationParser {
    fun parseAnimation(loader: Loader) {
        val decoder = createDecoder(loader) ?: return

//        val width = decoder.bounds.width()
//        val height = decoder.bounds.height()
//
//        Log.d("AppLog", "animations size:${width}x$height frameCount:${decoder.frameCount}")

//        decoder.decodeAllFrames(object : FrameSeqDecoder.FrameVisitor {
//            override fun onFrame(index: Int, bitmap: Bitmap, duration: Int): Boolean {
//                Log.d("AppLog", "frame $index delay:$duration ${bitmap.width}x${bitmap.height}")
//                // Return true to continue, false to stop
//                return true
//            }
//
//            override fun onException(t: Throwable) {
//                Log.e("AppLog", "Error decoding frames", t)
//            }
//        })

        // Alternative: Manual sequential decoding
        try {
            val frameCount = decoder.prepareSequentialDecode()
            val width = decoder.bounds.width()
            val height = decoder.bounds.height()
            Log.d("AppLog", "animations size:${width}x$height frameCount:${decoder.frameCount}")
            val bitmap = decoder.obtainBitmap(decoder.bounds.width(), decoder.bounds.height())
            if (bitmap != null) {
                for (i in 0 until frameCount) {
                    val duration = decoder.nextFrame(bitmap)
                    if (duration < 0) break
                    Log.d(
                        "AppLog",
                        "Manual frame $i delay:$duration bitmap:${bitmap.width}x${bitmap.height}"
                    )
                }
                decoder.recycleBitmap(bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            decoder.stop()
        }
    }

    private fun createDecoder(loader: Loader): FrameSeqDecoder<*, *>? {
        val reader = loader.obtain()
        return try {
            when {
                WebPParser.isAWebP(reader) -> WebPDecoder(loader, null)
                //need to reset the reader for each as it reached the end
                APNGParser.isAPNG(reader.also { it.reset() }) -> APNGDecoder(loader, null)
                GifParser.isGif(reader.also { it.reset() }) -> GifDecoder(loader, null)
                AVIFParser.isAVIF(reader.also { it.reset() }) -> AVIFDecoder(loader, null)
                else -> null
            }
        } finally {
            reader.close()
        }
    }

    fun parseAnimationFromStream(inputStreamProvider: () -> InputStream) {
        val loader = object : StreamLoader() {
            override fun getInputStream(): InputStream = inputStreamProvider()
        }
        parseAnimation(loader)
    }

    fun parseAnimationWithByteArray(data: ByteArray) {
        val loader = object : ByteBufferLoader() {
            override fun getByteBuffer(): ByteBuffer = ByteBuffer.wrap(data)
        }
        parseAnimation(loader)
    }

    fun parseAnimationWithResource(context: Context, animatedGif: Int) {
        val loader = ResourceStreamLoader(context, animatedGif)
        parseAnimation(loader)
    }
}
