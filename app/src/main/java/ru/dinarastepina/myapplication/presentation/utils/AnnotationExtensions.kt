package ru.dinarastepina.myapplication.presentation.utils

import com.scichart.charting.visuals.SciChartSurface
import com.scichart.charting.visuals.annotations.AnnotationLabel
import com.scichart.charting.visuals.annotations.HorizontalLineAnnotation
import com.scichart.charting.visuals.annotations.IAnnotation
import com.scichart.charting.visuals.annotations.LineAnnotationWithLabelsBase

fun SciChartSurface.annotations(init: CollectionContext<IAnnotation>.() -> Unit) {
    CollectionContext<IAnnotation>(annotations, context).init()
}
fun CollectionContext<IAnnotation>.horizontalLineAnnotation(init: HorizontalLineAnnotation.() -> Unit) = collection.add(
    HorizontalLineAnnotation(context).apply(init))

fun LineAnnotationWithLabelsBase.annotationLabels(init: CollectionContext<AnnotationLabel>.() -> Unit) {
    CollectionContext<AnnotationLabel>(annotationLabels, context).init()
}
fun CollectionContext<AnnotationLabel>.annotationLabel(init: AnnotationLabel.() -> Unit) = collection.add(
    AnnotationLabel(context).apply(init))