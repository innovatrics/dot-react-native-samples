package com.dotreactnativesamples.nfc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dotreactnativesamples.DotSdkResult
import com.dotreactnativesamples.json.createGson
import com.innovatrics.dot.nfc.NfcKey
import com.innovatrics.dot.nfc.TravelDocument
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class NfcReadingViewModel(application: Application) : AndroidViewModel(application) {

    private val resolveAuthorityCertificatesFileUseCase = ResolveAuthorityCertificatesFileUseCase(
        ioDispatcher = Dispatchers.IO,
        application,
        ResourceCopier(application.resources)
    )

    private val _result: MutableStateFlow<DotSdkResult?> = MutableStateFlow(null)
    private val gson = createGson()
    private val _configuration: MutableStateFlow<NfcTravelDocumentReaderFragment.Configuration?> = MutableStateFlow(null)

    val result = _result.asStateFlow()
    val configuration = _configuration.asStateFlow()

    fun setResult(travelDocument: TravelDocument) {
        _result.value = DotSdkResult(
            imageUri = null,
            jsonData = gson.toJson(travelDocument)
        )
    }

    fun handleResult() {
        _result.value = null
    }

    fun setJsonNfcKey(jsonObject: JSONObject) {
        viewModelScope.launch {
            val certificates = resolveAuthorityCertificatesFileUseCase()

            _configuration.value = NfcTravelDocumentReaderFragment.Configuration(
                nfcKey = NfcKey(
                    documentNumber = jsonObject.getString("documentNumber"),
                    dateOfExpiry = jsonObject.getString("dateOfExpiry"),
                    dateOfBirth = jsonObject.getString("dateOfBirth"),
                ),
                authorityCertificatesFilePath = certificates.path
            )
        }
    }
}
