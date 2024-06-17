package com.dotreactnativesamples.face

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dotreactnativesamples.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MagnifyLivenessActivity : AppCompatActivity(R.layout.activity_magnify_liveness) {

    companion object {
        const val EXTRA_KEY_RESULT = "result"
    }

    private val magnifEyeLivenessViewModel: MagnifEyeLivenessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            magnifEyeLivenessViewModel.result.collectLatest {
                if (it != null) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_KEY_RESULT, it)
                    setResult(Activity.RESULT_OK, intent)
                    magnifEyeLivenessViewModel.handleResult()
                    finish()
                }
            }
        }
    }
}
