package com.dotreactnativesamples.document

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureDetection
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureFragment
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureResult

class BasicDocumentAutoCaptureFragment : DocumentAutoCaptureFragment() {

    private val documentAutoCaptureViewModel: DocumentAutoCaptureViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    override fun onNoCameraPermission() {

    }

    override fun onCandidateSelectionStarted() {

    }

    override fun onCaptured(result: DocumentAutoCaptureResult) {
        documentAutoCaptureViewModel.setResult(result)
    }

    override fun onProcessed(detection: DocumentAutoCaptureDetection) {

    }

    override fun provideConfiguration(): Configuration {
        return documentAutoCaptureViewModel.configuration
    }
}
