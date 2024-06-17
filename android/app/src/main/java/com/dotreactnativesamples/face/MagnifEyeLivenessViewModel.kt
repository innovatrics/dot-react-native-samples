package com.dotreactnativesamples.face

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dotreactnativesamples.DotSdkResult
import com.dotreactnativesamples.json.createGson
import com.innovatrics.dot.face.liveness.magnifeye.MagnifEyeLivenessResult
import com.innovatrics.dot.image.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MagnifEyeLivenessViewModel : ViewModel() {

    private val _result: MutableStateFlow<DotSdkResult?> = MutableStateFlow(null)
    private val gson = createGson()

    val result = _result.asStateFlow()

    fun setResult(magnifEyeLivenessResult: MagnifEyeLivenessResult) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val bitmap = BitmapFactory.create(magnifEyeLivenessResult.bgrRawImage)
                val tempFile = File.createTempFile("magnifeye_liveness", ".jpeg")
                FileOutputStream(tempFile).use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                }

                _result.value = DotSdkResult(
                    imageUri = Uri.fromFile(tempFile).toString(),
                    jsonData = createJsonData(magnifEyeLivenessResult)
                )
            }
        }
    }

    private suspend fun createJsonData(magnifEyeLivenessResult: MagnifEyeLivenessResult): String {
        return withContext(Dispatchers.Default) {
            val faceResult = magnifEyeLivenessResult.detectedFace.resolve()
            gson.toJson(faceResult)
        }
    }

    fun handleResult() {
        _result.value = null
    }
}
