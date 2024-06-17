package com.dotreactnativesamples.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dotreactnativesamples.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject

class DocumentAutoCaptureActivity : AppCompatActivity(R.layout.activity_document_auto_capture) {

    companion object {
        const val EXTRA_KEY_JSON_CONFIGURATION = "configuration"
        const val EXTRA_KEY_RESULT = "result"
    }

    private val documentAutoCaptureViewModel: DocumentAutoCaptureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setConfiguration()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            documentAutoCaptureViewModel.result.collectLatest {
                if (it != null) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_KEY_RESULT, it)
                    setResult(Activity.RESULT_OK, intent)
                    documentAutoCaptureViewModel.handleResult()
                    finish()
                }
            }
        }
    }

    private fun setConfiguration() {
        intent.getStringExtra(EXTRA_KEY_JSON_CONFIGURATION)?.let {
            documentAutoCaptureViewModel.setJsonConfiguration(JSONObject(it))
        }
    }
}
