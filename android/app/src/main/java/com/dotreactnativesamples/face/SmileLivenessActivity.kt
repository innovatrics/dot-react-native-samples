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

class SmileLivenessActivity : AppCompatActivity(R.layout.activity_smile_liveness) {

    companion object {
        const val EXTRA_KEY_RESULT = "result"
    }

    private val smileLivenessViewModel: SmileLivenessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            smileLivenessViewModel.result.collectLatest {
                if (it != null) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_KEY_RESULT, it)
                    setResult(Activity.RESULT_OK, intent)
                    smileLivenessViewModel.handleResult()
                    finish()
                }
            }
        }
    }
}
