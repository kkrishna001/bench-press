package io.redgreen.benchpress.bmi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.math.pow
import kotlin.math.roundToInt

@Parcelize
data class BmiModel(val height: Float, val weight: Float, val SIUnit: SIUnits) : Parcelable {

    companion object {
        fun start(startWeight: Float, startHeight: Float): BmiModel =
            BmiModel(startWeight, startHeight, SIUnits.BRITISH)
    }

    fun calculateWeight(): Float {
        val factor = 10.0.pow(2.0)
        return if (SIUnit == SIUnits.STANDARD) {
            ((weight * factor).roundToInt() / factor).toFloat()
        } else {
            (((weight * 2.20F) * factor).roundToInt() / factor).toFloat()
        }
    }

    fun calculateHeight(): Float {
        val factor = 10.0.pow(2.0)
        return if (SIUnit == SIUnits.STANDARD) {
            ((height * factor).roundToInt() / factor).toFloat()
        } else {
            (((height / 2.54F) * factor).roundToInt() / factor).toFloat()
        }
    }

    fun calculateBmi(): Float {
        val bmi = ((weight / (height * height) * 10000))
        val factor = 10.0.pow(2.0)
        return ((bmi * factor).roundToInt() / factor).toFloat()
    }

    fun getWeightCategory(): DifferentWeights {
        val computedBmi = calculateBmi()
        return when {
            computedBmi <= 18.5 -> DifferentWeights.UNDERWEIGHT
            computedBmi > 18.5 && computedBmi <= 25 -> DifferentWeights.NORMAL_WEIGHT
            computedBmi > 25 && computedBmi < 30 -> DifferentWeights.OVER_WEIGHT
            else -> DifferentWeights.OBESE
        }
    }

    fun getWeightUnit(): String {
        return if (SIUnit == SIUnits.STANDARD) "kg" else "pound"
    }

    fun getHeightUnit(): String {
        return if (SIUnit == SIUnits.STANDARD) "cm" else "inches"
    }

    fun getSIUnit(): String {
        return SIUnit.toString()
    }

    fun changeHeight(height: Float): BmiModel =
        copy(height = height)

    fun changeWeight(weight: Float): BmiModel =
        copy(weight = weight)

    fun changeUnit(SIUnit: SIUnits): BmiModel =
        copy(SIUnit = SIUnit)

}