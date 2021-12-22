package io.redgreen.benchpress.bmi

sealed class BmiEvent

data class ChangeHeightEvent(val height: Float) : BmiEvent()
data class ChangeWeightEvent(val weight: Float) : BmiEvent()
data class ChangeUnitEvent(val SIUnit: SIUnits) : BmiEvent()