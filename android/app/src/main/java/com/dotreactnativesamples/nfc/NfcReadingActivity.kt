package com.dotreactnativesamples.nfc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.dotreactnativesamples.R
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject

class NfcReadingActivity : AppCompatActivity(R.layout.activity_nfc_reading) {

    companion object {
        const val EXTRA_KEY_JSON_CONFIGURATION = "configuration"
        const val EXTRA_KEY_RESULT = "result"
    }

    private val nfcReadingViewModel: NfcReadingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setJsonNfcKey()

        lifecycleScope.launch {
            nfcReadingViewModel.configuration.collectLatest {
                handleConfiguration(it)
            }
        }

        lifecycleScope.launch {
            nfcReadingViewModel.result.collectLatest {
                if (it != null) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_KEY_RESULT, it)
                    setResult(Activity.RESULT_OK, intent)
                    nfcReadingViewModel.handleResult()
                    finish()
                }
            }
        }
    }

    private fun setJsonNfcKey() {
        intent.getStringExtra(EXTRA_KEY_JSON_CONFIGURATION)?.let {
            nfcReadingViewModel.setJsonNfcKey(JSONObject(it))
        }
    }

    private fun handleConfiguration(configuration: NfcTravelDocumentReaderFragment.Configuration?) {
        val container = findViewById<FragmentContainerView>(R.id.container)
        val basicNfcReadingFragment = container.getFragment<BasicNfcReadingFragment?>()

        if (configuration != null && basicNfcReadingFragment == null) {
            supportFragmentManager.commit {
                add(container.id, BasicNfcReadingFragment::class.java, null)
            }
        }
    }
}
