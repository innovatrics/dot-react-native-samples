package com.dotreactnativesamples.face

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureDetection
import com.innovatrics.dot.face.liveness.smile.SmileLivenessFragment
import com.innovatrics.dot.face.liveness.smile.SmileLivenessResult

class BasicSmileLivenessFragment : SmileLivenessFragment() {

    private val smileLivenessViewModel: SmileLivenessViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    override fun provideConfiguration(): Configuration {
        return Configuration()
    }

    override fun onNoCameraPermission() {

    }

    override fun onCriticalFacePresenceLost() {

    }

    override fun onFinished(result: SmileLivenessResult) {
        smileLivenessViewModel.setResult(result)
    }

    override fun onProcessed(detection: FaceAutoCaptureDetection) {

    }
}
