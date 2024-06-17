package com.dotreactnativesamples.face

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dotreactnativesamples.DotSdkResult
import com.dotreactnativesamples.json.createGson
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureResult
import com.innovatrics.dot.image.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FaceAutoCaptureViewModel : ViewModel() {

    private val _result: MutableStateFlow<DotSdkResult?> = MutableStateFlow(null)
    private val gson = createGson()

    val result = _result.asStateFlow()

    fun setResult(faceAutoCaptureResult: FaceAutoCaptureResult) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val bitmap = BitmapFactory.create(faceAutoCaptureResult.bgrRawImage)
                val tempFile = File.createTempFile("face_auto_capture", ".png")
                FileOutputStream(tempFile).use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }

                _result.value = DotSdkResult(
                    imageUri = Uri.fromFile(tempFile).toString(),
                    jsonData = createJsonData(faceAutoCaptureResult)
                )
            }
        }
    }

    private suspend fun createJsonData(faceAutoCaptureResult: FaceAutoCaptureResult): String {
        return withContext(Dispatchers.Default) {
            val faceResult = faceAutoCaptureResult.detectedFace?.resolve()
            gson.toJson(faceResult)
        }
    }

    fun handleResult() {
        _result.value = null
    }
}
