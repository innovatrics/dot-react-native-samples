package com.dotreactnativesamples.face

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureDetection
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureResult

class BasicFaceAutoCaptureFragment : FaceAutoCaptureFragment() {

    private val faceAutoCaptureViewModel: FaceAutoCaptureViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    override fun onNoCameraPermission() {

    }

    override fun onCandidateSelectionStarted() {

    }

    override fun onCaptured(result: FaceAutoCaptureResult) {
        faceAutoCaptureViewModel.setResult(result)
    }

    override fun onProcessed(detection: FaceAutoCaptureDetection) {

    }

    override fun provideConfiguration(): Configuration {
        return Configuration()
    }
}
