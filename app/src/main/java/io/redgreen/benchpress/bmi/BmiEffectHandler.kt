package io.redgreen.benchpress.bmi

import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.redgreen.benchpress.hellostranger.ShowErrorEffect

class BmiEffectHandler {
    fun createEffectHandler(
        interact: Interactor3
    ): ObservableTransformer<BmiEffect, BmiEvent> {
        return RxMobius.subtypeEffectHandler<BmiEffect, BmiEvent>()
            .addConsumer(
                ShowUnitChanged::class.java, {
                    interact.showUnitChanged()
                }, AndroidSchedulers.mainThread()
            )
            .build()

    }
}