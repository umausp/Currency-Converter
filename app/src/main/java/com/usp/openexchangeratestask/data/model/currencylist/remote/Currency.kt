package com.usp.openexchangeratestask.data.model.currencylist.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val code: String?,
    val name: String?
) : Parcelable
