package ru.dinarastepina.myapplication.presentation.utils

import android.animation.TimeInterpolator
import android.view.animation.LinearInterpolator
import com.scichart.charting.visuals.renderableSeries.XyRenderableSeriesBase
import com.scichart.extensions.builders.AnimatorBuilderBase
import com.scichart.extensions.builders.SciChartBuilder

fun <TBuilder : AnimatorBuilderBase.RenderPassDataAnimatorBuilder<TBuilder>> sweepAnimation(builder: TBuilder, init: SweepAnimator.() -> Unit) {
    val animator = SweepAnimator().apply(init)
    animator(builder, animator)
        .withSweepTransformation()
        .start()
}

private fun <TBuilder : AnimatorBuilderBase<TBuilder>> animator(builder: TBuilder, animator: SweepAnimator): TBuilder {
    return builder
        .withInterpolator(animator.interpolator)
        .withDuration(animator.duration)
        .withStartDelay(animator.startDelay)
}

fun <T : XyRenderableSeriesBase> T.sweepAnimation(init: SweepAnimator.() -> Unit) = sweepAnimation(
    SciChartBuilder.instance().newAnimator(this), init)


open class SweepAnimator(var interpolator: TimeInterpolator = LinearInterpolator(), var duration: Long = 3000, var startDelay: Long = 350)
