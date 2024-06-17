package com.dotreactnativesamples.nfc

import android.app.Application
import com.dotreactnativesamples.R
import java.io.File
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResolveAuthorityCertificatesFileUseCase(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val application: Application,
    private val resourceCopier: ResourceCopier,
) {

    private val filename = "authority_certificates.pem"

    suspend operator fun invoke() = withContext(ioDispatcher) {
        resolveAuthorityCertificatesFile()
    }

    private fun resolveAuthorityCertificatesFile() = File(application.filesDir, filename).apply {
        writeAuthorityCertificatesToFile(this)
    }

    private fun writeAuthorityCertificatesToFile(file: File) = resourceCopier.copyToFile(R.raw.master_list, file)
}
