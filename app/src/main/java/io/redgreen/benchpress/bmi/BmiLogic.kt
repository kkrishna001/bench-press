package io.redgreen.benchpress.bmi

import com.spotify.mobius.Next
import com.spotify.mobius.Update

object BmiLogic : Update<BmiModel, BmiEvent, BmiEffect> {

    override fun update(model: BmiModel, event: BmiEvent): Next<BmiModel, BmiEffect> {
        return when (event) {
            is ChangeWeightEvent -> Next.next(model.changeWeight(event.weight))
            is ChangeHeightEvent -> Next.next(model.changeHeight(event.height))
            is ChangeUnitEvent -> Next.next(
                model.changeUnit(event.SIUnit),
                setOf<BmiEffect>(ShowUnitChanged)
            )
        }
    }
}