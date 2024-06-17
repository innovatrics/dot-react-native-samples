package com.dotreactnativesamples.document

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dotreactnativesamples.DotSdkResult
import com.dotreactnativesamples.json.createGson
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureFragment
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureResult
import com.innovatrics.dot.document.autocapture.MrzValidation
import com.innovatrics.dot.image.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DocumentAutoCaptureViewModel : ViewModel() {

    private val _result = MutableStateFlow<DotSdkResult?>(null)
    private val gson = createGson()

    val result = _result.asStateFlow()

    var configuration: DocumentAutoCaptureFragment.Configuration = DocumentAutoCaptureFragment.Configuration()
        private set

    fun setResult(documentAutoCaptureResult: DocumentAutoCaptureResult) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val bitmap = BitmapFactory.create(documentAutoCaptureResult.bgraRawImage)
                val tempFile = File.createTempFile("document_auto_capture", ".jpeg")
                FileOutputStream(tempFile).use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                }

                _result.value = DotSdkResult(
                    imageUri = Uri.fromFile(tempFile).toString(),
                    jsonData = gson.toJson(documentAutoCaptureResult)
                )
            }
        }
    }

    fun handleResult() {
        _result.value = null
    }

    fun setJsonConfiguration(jsonObject: JSONObject) {
        configuration = configuration.copy(
            mrzValidation = MrzValidation.valueOf(jsonObject.getString("mrzValidation"))
        )
    }
}
