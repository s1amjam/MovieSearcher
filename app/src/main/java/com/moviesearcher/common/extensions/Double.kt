package com.moviesearcher.common.extensions

import java.math.RoundingMode

fun Double.toOneScale(): String {
    return this.toBigDecimal().setScale(1, RoundingMode.UP).toString()
}