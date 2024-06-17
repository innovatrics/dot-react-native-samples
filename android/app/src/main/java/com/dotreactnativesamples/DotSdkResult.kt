package com.dotreactnativesamples

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DotSdkResult(
    val imageUri: String?,
    val jsonData: String,
): Parcelable
