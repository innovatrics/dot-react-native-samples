package com.dotreactnativesamples

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import com.dotreactnativesamples.document.DocumentAutoCaptureActivity
import com.dotreactnativesamples.face.FaceAutoCaptureActivity
import com.dotreactnativesamples.face.MagnifyLivenessActivity
import com.dotreactnativesamples.face.SmileLivenessActivity
import com.dotreactnativesamples.nfc.NfcReadingActivity
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import com.innovatrics.dot.core.DotSdk
import com.innovatrics.dot.core.DotSdkConfiguration
import com.innovatrics.dot.document.DotDocumentLibrary
import com.innovatrics.dot.face.DotFaceLibrary
import com.innovatrics.dot.face.DotFaceLibraryConfiguration
import com.innovatrics.dot.face.detection.fast.DotFaceDetectionFastModule
import com.innovatrics.dot.face.expressionneutral.DotFaceExpressionNeutralModule
import com.innovatrics.dot.nfc.DotNfcLibrary
import java.io.InputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DotSdkReactModule(
    context: ReactApplicationContext,
    private val coroutineScope: CoroutineScope
) : ReactContextBaseJavaModule(context) {
    override fun getName(): String = "DotSdk"

    private companion object {
        const val REQUEST_CODE_DOCUMENT_AUTO_CAPTURE = 0
        const val REQUEST_CODE_NFC_READING = 1
        const val REQUEST_CODE_FACE_AUTO_CAPTURE = 2
        const val REQUEST_CODE_MAGNIFEYE_LIVENESS = 3
        const val REQUEST_CODE_SMILE_LIVENESS = 4
    }

    private val activityEventListener = object : BaseActivityEventListener() {
        override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
            handleActivityResult(activity, requestCode, resultCode, data)
        }
    }

    private val activityRequestPromises = mutableMapOf<Int, Promise>()

    override fun initialize() {
        reactApplicationContext.addActivityEventListener(activityEventListener)
    }

    override fun invalidate() {
        reactApplicationContext.removeActivityEventListener(activityEventListener)
    }

    @ReactMethod
    fun isInitialized(promise: Promise) {
        promise.resolve(DotSdk.isInitialized())
    }

    @ReactMethod
    fun initialize(promise: Promise) {
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    initializeDotSdk()
                    promise.resolve(null)
                } catch (t: Throwable) {
                    promise.reject(t)
                }
            }
        }
    }

    @ReactMethod
    fun startDocumentAutoCapture(configurationJson: String?, promise: Promise) {
        val intent = Intent(reactApplicationContext, DocumentAutoCaptureActivity::class.java)
        intent.putExtra(DocumentAutoCaptureActivity.EXTRA_KEY_JSON_CONFIGURATION, configurationJson)
        startActivityForResult(promise, intent, REQUEST_CODE_DOCUMENT_AUTO_CAPTURE)
    }

    @ReactMethod
    fun startNfcReading(configurationJson: String?, promise: Promise) {
        val intent = Intent(reactApplicationContext, NfcReadingActivity::class.java)
        intent.putExtra(NfcReadingActivity.EXTRA_KEY_JSON_CONFIGURATION, configurationJson)
        startActivityForResult(promise, intent, REQUEST_CODE_NFC_READING)
    }

    @ReactMethod
    fun startFaceAutoCapture(promise: Promise) {
        val intent = Intent(reactApplicationContext, FaceAutoCaptureActivity::class.java)
        startActivityForResult(promise, intent, REQUEST_CODE_FACE_AUTO_CAPTURE)
    }

    @ReactMethod
    fun startMagnifEyeLiveness(promise: Promise) {
        val intent = Intent(reactApplicationContext, MagnifyLivenessActivity::class.java)
        startActivityForResult(promise, intent, REQUEST_CODE_MAGNIFEYE_LIVENESS)
    }

    @ReactMethod
    fun startSmileLiveness(promise: Promise) {
        val intent = Intent(reactApplicationContext, SmileLivenessActivity::class.java)
        startActivityForResult(promise, intent, REQUEST_CODE_SMILE_LIVENESS)
    }

    private fun startActivityForResult(promise: Promise, intent: Intent, requestCode: Int) {
        currentActivity.let {
            if (it != null) {
                try {
                    it.startActivityForResult(intent, requestCode)
                    activityRequestPromises[requestCode] = promise
                } catch (e: ActivityNotFoundException) {
                    promise.reject(e)
                }
            } else {
                promise.reject(RuntimeException("Activity is not available"))
            }
        }
    }

    private fun initializeDotSdk() {
        val license = reactApplicationContext.resources.openRawResource(R.raw.dot_license).use(InputStream::readBytes)

        DotSdk.initialize(
            DotSdkConfiguration(
                context = reactApplicationContext,
                licenseBytes = license,
                libraries = listOf(
                    DotDocumentLibrary(),
                    createDotFaceLibrary(),
                    DotNfcLibrary()
                )
            )
        )
    }

    private fun createDotFaceLibrary(): DotFaceLibrary {
        val modules = createDotFaceLibraryModules()
        val configuration = DotFaceLibraryConfiguration(modules)
        return DotFaceLibrary(configuration)
    }

    private fun createDotFaceLibraryModules() = listOf(
        DotFaceDetectionFastModule.of(),
        DotFaceExpressionNeutralModule.of(),
    )

    private fun handleActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        val promise = activityRequestPromises[requestCode]
        check(promise != null) { "Promise for result code $resultCode is null" }

        when (requestCode) {
            REQUEST_CODE_DOCUMENT_AUTO_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = data!!.readResult(DocumentAutoCaptureActivity.EXTRA_KEY_RESULT)
                    promise.resolve(result)
                } else {
                    promise.resolve(null)
                }
            }

            REQUEST_CODE_NFC_READING -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = data!!.readResult(NfcReadingActivity.EXTRA_KEY_RESULT)
                    promise.resolve(result)
                } else {
                    promise.resolve(null)
                }
            }

            REQUEST_CODE_FACE_AUTO_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = data!!.readResult(FaceAutoCaptureActivity.EXTRA_KEY_RESULT)
                    promise.resolve(result)
                } else {
                    promise.resolve(null)
                }
            }

            REQUEST_CODE_MAGNIFEYE_LIVENESS -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = data!!.readResult(MagnifyLivenessActivity.EXTRA_KEY_RESULT)
                    promise.resolve(result)
                } else {
                    promise.resolve(null)
                }
            }

            REQUEST_CODE_SMILE_LIVENESS -> {
                if (resultCode == Activity.RESULT_OK) {
                    val result = data!!.readResult(SmileLivenessActivity.EXTRA_KEY_RESULT)
                    promise.resolve(result)
                } else {
                    promise.resolve(null)
                }
            }

            else -> throw IllegalArgumentException("Unhandled activity request code")
        }
    }

    private fun Intent.readResult(resultKey: String): ReadableMap {
        val dotSdkResult: DotSdkResult = getParcelableExtra(resultKey)!!

        return WritableNativeMap().apply {
            putString("imageUri", dotSdkResult.imageUri)
            putString("jsonData", dotSdkResult.jsonData)
        }
    }
}
