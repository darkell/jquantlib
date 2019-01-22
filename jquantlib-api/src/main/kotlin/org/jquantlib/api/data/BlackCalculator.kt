package org.jquantlib.api.data

data class BlackCalculator(
    val strike: Double,
    val forward: Double,
    val stdDev: Double,
    val discount: Double,
    val variance: Double,
    val dX_dS: Double,
    val n_d1: Double,
    val cum_d1: Double,
    val n_d2: Double,
    val cum_d2: Double,
    val d1: Double,
    val d2: Double,
    val alpha: Double,
    val beta: Double,
    val dAlpha_dD1: Double,
    val dBeta_dD2: Double,
    val x: Double,
    val dx_dStrike: Double
)