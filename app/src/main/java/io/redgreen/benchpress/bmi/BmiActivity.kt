package io.redgreen.benchpress.bmi

import android.content.Context
import android.content.Intent
import android.widget.SeekBar
import android.widget.Toast
import com.spotify.mobius.Next
import io.reactivex.ObservableTransformer
import io.redgreen.benchpress.R
import io.redgreen.benchpress.architecture.BaseActivity
import kotlinx.android.synthetic.main.bmi_activity.*

class BmiActivity : BaseActivity<BmiModel, BmiEvent, BmiEffect>(), Interactor3 {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BmiActivity::class.java))
        }

        private const val startHeight = 162.0F
        private const val startWeight = 50.0F
    }

    override fun layoutResId(): Int {
        return R.layout.bmi_activity
    }

    override fun setup() {
        setupSeekBars()
        setupUnitChangeSwitch()
    }

    override fun initialModel(): BmiModel {
        return BmiModel.start(startHeight, startWeight)
    }

    override fun updateFunction(model: BmiModel, event: BmiEvent): Next<BmiModel, BmiEffect> {
        return BmiLogic.update(model, event)
    }

    override fun render(model: BmiModel) {
        val weight = "${model.calculateWeight()} ${model.getWeightUnit()}"
        val height = "${model.calculateHeight()} ${model.getHeightUnit()}"
        measurementSystemSwitch.text = model.getSIUnit()
        bmiTextView.text = model.calculateBmi().toString()
        bmiCategoryTextView.text = model.getWeightCategory().weight
        weightTextView.text = weight
        heightTextView.text = height
    }

    override fun effectHandler(): ObservableTransformer<BmiEffect, BmiEvent> {
        return BmiEffectHandler().createEffectHandler(
            interact = this@BmiActivity
        )
    }

    private fun setupSeekBars() {
        weightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                eventSource.notifyEvent((ChangeWeightEvent((30 + progress).toFloat())))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        heightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                eventSource.notifyEvent(ChangeHeightEvent((120 + progress).toFloat()))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun setupUnitChangeSwitch() {
        measurementSystemSwitch.setOnCheckedChangeListener { _, isChecked ->
            val unitForMeasurement = if (isChecked) SIUnits.STANDARD else SIUnits.BRITISH
            eventSource.notifyEvent(ChangeUnitEvent(unitForMeasurement))
        }
    }

    override fun showUnitChanged() =
        Toast.makeText(this, "${measurementSystemSwitch.text}", Toast.LENGTH_SHORT).show()


}
