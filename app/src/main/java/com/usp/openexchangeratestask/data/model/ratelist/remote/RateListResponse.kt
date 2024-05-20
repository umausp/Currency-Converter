package com.usp.openexchangeratestask.data.model.ratelist.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RateListResponse(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
) : Parcelable

@Parcelize
data class RateModel(val code: String, val rate: Double) : Parcelable

fun RateListResponse.toModel(): List<RateModel> {
    return rates.map { RateModel(it.key, it.value) }
}
