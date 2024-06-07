/*
 * Copyright (c) 2023 sparetimedevs and respective authors and developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparetimedevs.ami.app.graphicmusicnotation.vector

import androidx.compose.ui.graphics.vector.PathNode
import arrow.core.EitherNel
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.graphic.asAmiMeasures
import com.sparetimedevs.ami.graphic.vector.NoteVectors
import com.sparetimedevs.ami.graphic.vector.Vector
import com.sparetimedevs.ami.graphic.vector.WHOLE_NOTE_WIDTH
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure

fun List<PathNode>.asAmiMeasures(
    graphicProperties: GraphicProperties
): EitherNel<ValidationError, List<Measure>> =
    asAmiMeasures(asNotesVectorsPerMeasure(this, graphicProperties))

fun asNotesVectorsPerMeasure(
    pathData: List<PathNode>,
    graphicProperties: GraphicProperties
): Map<String, List<NoteVectors>> {
    val pathDataPerMeasure: Map<String, List<PathNode>> =
        pathData.fold(mutableMapOf()) { acc, pathNode ->
            val maybeMeasureKey: String? =
                when (pathNode) {
                    is PathNode.MoveTo -> {
                        measureKey(pathNode.x, graphicProperties)
                    }
                    else ->
                        if (acc.entries.isNotEmpty()) {
                            val entry = acc.entries.find { it.value.last() is PathNode.MoveTo }
                            entry?.key ?: acc.entries.last().key
                        } else null
                }

            if (maybeMeasureKey != null) {
                acc[maybeMeasureKey] = acc[maybeMeasureKey]?.plus(pathNode) ?: listOf(pathNode)
            }
            acc
        }

    return pathDataPerMeasure.mapValues { asNotesVectorsForOneMeasure(it.value, graphicProperties) }
}

private fun measureKey(moveToXValue: Float, graphicProperties: GraphicProperties): String {
    val moveToXValueMinusOffset = moveToXValue - graphicProperties.offsetX

    val measureKey =
        when {
            moveToXValueMinusOffset < graphicProperties.measureWidth &&
                moveToXValueMinusOffset >= 0 -> {
                "measure-1"
            }
            moveToXValueMinusOffset <
                ((graphicProperties.measureWidth * 2) + graphicProperties.spaceBetweenMeasures) &&
                moveToXValueMinusOffset >= graphicProperties.measureWidth -> {
                "measure-2"
            }
            moveToXValueMinusOffset <
                ((graphicProperties.measureWidth * 3) +
                    (graphicProperties.spaceBetweenMeasures * 2)) &&
                moveToXValueMinusOffset >=
                    ((graphicProperties.measureWidth * 2) +
                        graphicProperties.spaceBetweenMeasures) -> {
                "measure-3"
            }
            moveToXValueMinusOffset <
                ((graphicProperties.measureWidth * 4) +
                    (graphicProperties.spaceBetweenMeasures * 3)) &&
                moveToXValueMinusOffset >=
                    ((graphicProperties.measureWidth * 3) +
                        graphicProperties.spaceBetweenMeasures) -> {
                "measure-4"
            }
            else -> {
                "measure-unknown"
            }
        }
    return measureKey
}

private fun asNotesVectorsForOneMeasure(
    pathDataForOneMeasure: List<PathNode>,
    graphicProperties: GraphicProperties
): List<NoteVectors> {
    var tempStartVector = Vector(0.0, 0.0)
    val notesVectors = mutableListOf<NoteVectors>()
    pathDataForOneMeasure.forEach { pathNode ->
        when (pathNode) {
            is PathNode.MoveTo -> {
                tempStartVector =
                    Vector(
                        calcX(pathNode.x, graphicProperties),
                        calcY(pathNode.y, graphicProperties)
                    )
            }
            is PathNode.HorizontalTo -> {
                val endVector = Vector(calcX(pathNode.x, graphicProperties), tempStartVector.y)
                notesVectors.add(NoteVectors(tempStartVector, endVector))
            }
            is PathNode.LineTo -> {
                val endVector =
                    Vector(
                        calcX(pathNode.x, graphicProperties),
                        calcY(pathNode.y, graphicProperties)
                    )
                notesVectors.add(NoteVectors(tempStartVector, endVector))
            }
            // The other types of PathNode should actually not be relevant
            is PathNode.ArcTo -> pathNode.arcStartY
            is PathNode.CurveTo -> pathNode.y1
            is PathNode.QuadTo -> pathNode.y1
            is PathNode.ReflectiveCurveTo -> pathNode.y1
            is PathNode.ReflectiveQuadTo -> pathNode.y
            is PathNode.RelativeArcTo -> pathNode.arcStartDy
            is PathNode.RelativeCurveTo -> pathNode.dy1
            is PathNode.RelativeLineTo -> pathNode.dy
            is PathNode.RelativeMoveTo -> pathNode.dy
            is PathNode.RelativeQuadTo -> pathNode.dy1
            is PathNode.RelativeReflectiveCurveTo -> pathNode.dy1
            is PathNode.RelativeReflectiveQuadTo -> pathNode.dy
            is PathNode.RelativeVerticalTo -> pathNode.dy
            is PathNode.VerticalTo -> pathNode.y
            PathNode.Close -> Float.MIN_VALUE
            is PathNode.RelativeHorizontalTo -> Float.MIN_VALUE
        }
    }
    return notesVectors.toList()
}

private fun calcX(xFromPathNode: Float, graphicProperties: GraphicProperties): Double =
    (xFromPathNode - graphicProperties.offsetX) * WHOLE_NOTE_WIDTH / graphicProperties.measureWidth

private fun calcY(yFromPathNode: Float, graphicProperties: GraphicProperties): Double {
    // We need to transform from upper left 0,0 to bottom left 0,0 coordinate system.
    val height = 32 // Why 32? Not sure, but it is the same in AmiMeasuresAsPathData.kt.

    val yInVectorScaleFromTopToBottom =
        (yFromPathNode - graphicProperties.offsetY) / graphicProperties.wholeStepExpressedInY

    val yInVectorScaleFromBottomToTop = height - yInVectorScaleFromTopToBottom

    return yInVectorScaleFromBottomToTop
}
