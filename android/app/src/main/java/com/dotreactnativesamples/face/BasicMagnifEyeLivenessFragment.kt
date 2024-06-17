package com.dotreactnativesamples.face

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureDetection
import com.innovatrics.dot.face.liveness.magnifeye.MagnifEyeLivenessFragment
import com.innovatrics.dot.face.liveness.magnifeye.MagnifEyeLivenessResult

class BasicMagnifEyeLivenessFragment : MagnifEyeLivenessFragment() {

    private val magnifEyeLivenessViewModel: MagnifEyeLivenessViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    override fun provideConfiguration(): Configuration {
        return Configuration()
    }

    override fun onNoCameraPermission() {

    }

    override fun onFinished(result: MagnifEyeLivenessResult) {
        magnifEyeLivenessViewModel.setResult(result)
    }

    override fun onProcessed(detection: FaceAutoCaptureDetection) {

    }
}