package com.usp.openexchangeratestask.utils

import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

fun List<RatesEntity>?.convertAmount(base: String, amount: Double): List<RatesEntity>? {
    if (this == null) return null
    if (base == "USD") return this

    val baseRate: Double = this.find { it.code == base }?.rate ?: 1.0

    val convertedAmount = (amount / baseRate)

    val roundedConvertedAmounts = this.map { entity ->
        val newAmount = (convertedAmount * entity.rate).toTwoDecimalString()
        RatesEntity(entity.code, newAmount)
    }

    return roundedConvertedAmounts
}

fun Double.toTwoDecimalString(): Double {
    return String.format(Locale.ROOT, "%.2f", this).toDouble()
}
