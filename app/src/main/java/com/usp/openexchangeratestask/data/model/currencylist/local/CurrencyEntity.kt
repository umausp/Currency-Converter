package com.usp.openexchangeratestask.data.model.currencylist.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "currencies")
@Parcelize
data class CurrencyEntity(
    @PrimaryKey
    val code: String,
    val name: String?
) : Parcelable
