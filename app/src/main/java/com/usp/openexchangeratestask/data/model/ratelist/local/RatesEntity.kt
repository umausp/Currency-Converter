package com.usp.openexchangeratestask.data.model.ratelist.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateModel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "rates")
@Parcelize
data class RatesEntity(
    @PrimaryKey
    val code: String,
    val rate: Double
) : Parcelable

fun RateModel.toEntity(): RatesEntity {
    return RatesEntity(
        code = code,
        rate = rate
    )
}
