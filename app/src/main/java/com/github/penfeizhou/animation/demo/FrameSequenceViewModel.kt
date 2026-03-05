package com.github.penfeizhou.animation.demo

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.github.penfeizhou.animation.apng.decode.APNGDecoder
import com.github.penfeizhou.animation.avif.decode.AVIFDecoder
import com.github.penfeizhou.animation.decode.FrameSeqDecoder
import com.github.penfeizhou.animation.gif.decode.GifDecoder
import com.github.penfeizhou.animation.loader.Loader
import com.github.penfeizhou.animation.loader.ResourceStreamLoader
import com.github.penfeizhou.animation.webp.decode.WebPDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class FrameSequenceViewModel(application: Application) : AndroidViewModel(application) {
    private var decoder: FrameSeqDecoder<*, *>? = null
    private var currentBitmap: Bitmap? = null

    private val _frameBitmap = MutableLiveData<Bitmap?>()
    val frameBitmap: LiveData<Bitmap?> = _frameBitmap

    private val _frameInfo = MutableLiveData<String>()
    val frameInfo: LiveData<String> = _frameInfo

    private val _fileInfo = MutableLiveData<String>()
    val fileInfo: LiveData<String> = _fileInfo

    private val _performanceProgress = MutableLiveData<Int>()
    val performanceProgress: LiveData<Int> = _performanceProgress

    private val _performanceInfo = MutableLiveData<String>()
    val performanceInfo: LiveData<String> = _performanceInfo

    private val _isDecodingAll = MutableLiveData<Boolean>(false)
    val isDecodingAll: LiveData<Boolean> = _isDecodingAll

    private var loopJob: Job? = null

    fun selectFile(assetName: String) {
        stopDecoding()
        decoder?.stop()
        val context = getApplication<Application>()
        val loader = com.github.penfeizhou.animation.loader.AssetStreamLoader(context, assetName)
        decoder = createDecoder(loader)
        
        viewModelScope.launch(Dispatchers.IO) {
            decoder?.let {
                try {
                    it.prepareSequentialDecode()
                    val bounds = it.bounds
                    val frameCount = it.frameCount
                    _fileInfo.postValue("File: $assetName\nSize: ${bounds.width()}x${bounds.height()}\nFrames: $frameCount")
                    
                    currentBitmap?.recycle()
                    currentBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888)
                    nextFrame()
                } catch (e: IOException) {
                    _fileInfo.postValue("Error: ${e.message}")
                }
            }
        }
    }

    private fun createDecoder(loader: Loader): FrameSeqDecoder<*, *>? {
        val reader = loader.obtain()
        return try {
            when {
                com.github.penfeizhou.animation.webp.decode.WebPParser.isAWebP(reader) -> WebPDecoder(loader, null)
                com.github.penfeizhou.animation.apng.decode.APNGParser.isAPNG(reader.also { it.reset() }) -> APNGDecoder(loader, null)
                com.github.penfeizhou.animation.gif.decode.GifParser.isGif(reader.also { it.reset() }) -> GifDecoder(loader, null)
                com.github.penfeizhou.animation.avif.decode.AVIFParser.isAVIF(reader.also { it.reset() }) -> AVIFDecoder(loader, null)
                else -> null
            }
        } finally {
            reader.close()
        }
    }

    fun nextFrame() {
        viewModelScope.launch(Dispatchers.IO) {
            val d = decoder ?: return@launch
            val b = currentBitmap ?: return@launch
            val duration = d.nextFrame(b)
            if (duration >= 0) {
                _frameBitmap.postValue(b)
                _frameInfo.postValue("Index: ${d.frameIndex}, Duration: ${duration}ms")
            } else {
                // Wrap around
                d.prepareSequentialDecode()
                val firstDuration = d.nextFrame(b)
                _frameBitmap.postValue(b)
                _frameInfo.postValue("Index: ${d.frameIndex}, Duration: ${firstDuration}ms")
            }
        }
    }

    fun startLoop() {
        if (loopJob?.isActive == true) return
        loopJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val d = decoder ?: break
                val b = currentBitmap ?: break
                val duration = d.nextFrame(b)
                if (duration >= 0) {
                    _frameBitmap.postValue(b)
                    _frameInfo.postValue("Index: ${d.frameIndex}, Duration: ${duration}ms")
                    delay(duration.toLong().coerceAtLeast(16L))
                } else {
                    d.prepareSequentialDecode()
                }
            }
        }
    }

    fun stopLoop() {
        loopJob?.cancel()
    }

    fun runPerformanceTest() {
        if (_isDecodingAll.value == true) return
        _isDecodingAll.value = true
        _performanceInfo.value = "Starting..."
        
        viewModelScope.launch(Dispatchers.IO) {
            val d = decoder ?: run {
                _isDecodingAll.postValue(false)
                return@launch
            }
            val start = System.currentTimeMillis()
            val totalFrames = d.frameCount
            
            d.decodeAllFrames(object : FrameSeqDecoder.FrameVisitor {
                override fun onFrame(index: Int, bitmap: Bitmap, duration: Int): Boolean {
                    _performanceProgress.postValue((index + 1) * 100 / totalFrames)
                    return true
                }

                override fun onException(t: Throwable) {
                    _performanceInfo.postValue("Error: ${t.message}")
                }
            })
            
            val end = System.currentTimeMillis()
            _performanceInfo.postValue("Decoded $totalFrames frames in ${end - start}ms")
            _isDecodingAll.postValue(false)
        }
    }

    private fun stopDecoding() {
        stopLoop()
    }

    override fun onCleared() {
        super.onCleared()
        decoder?.stop()
        currentBitmap?.recycle()
    }
}
