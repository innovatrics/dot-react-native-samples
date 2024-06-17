package com.dotreactnativesamples.nfc

import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.nfc.TravelDocument
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment

class BasicNfcReadingFragment : NfcTravelDocumentReaderFragment() {

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels()

    override fun onSucceeded(travelDocument: TravelDocument) {
        nfcReadingViewModel.setResult(travelDocument)
    }

    override fun onFailed(exception: Exception) {

    }

    override fun provideConfiguration(): Configuration {
        return nfcReadingViewModel.configuration.value!!
    }
}
