/*
 * Copyright (c) 2023-2025 sparetimedevs and respective authors and developers.
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

import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.graphic.asNotesVectors
import com.sparetimedevs.ami.graphic.vector.NoteVectors
import com.sparetimedevs.ami.graphic.vector.WHOLE_NOTE_WIDTH
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure

fun List<Measure>.asPathData(graphicProperties: GraphicProperties): List<PathNode> {
    val numberOfMeasuresOnDisplay = 4 // This can and probably should be dynamic.
    return this.take(numberOfMeasuresOnDisplay).flatMapIndexed { index: Int, measure: Measure ->
        val notesVectorsForOneMeasure = asNotesVectors(measure)
        val pathDataForOneMeasure = asPathData(notesVectorsForOneMeasure, graphicProperties, index)
        pathDataForOneMeasure
    }
}

private fun asPathData(
    notesVectors: List<NoteVectors>,
    graphicProperties: GraphicProperties,
    measureNumberOnDisplay: Int,
): List<PathNode> {
    val measureOffsetX: Double =
        if (measureNumberOnDisplay > 0) {
            (graphicProperties.measureWidth * measureNumberOnDisplay) +
                (graphicProperties.spaceBetweenMeasures * measureNumberOnDisplay)
        } else {
            0.0
        }
    return notesVectors.flatMap { vectors ->
        val startX: Double = calcX(vectors.start.x, graphicProperties, measureOffsetX)
        val startY: Double = calcY(vectors.start.y, graphicProperties)
        val endXWithCutOff: Double =
            calcX(
                vectors.end.x - graphicProperties.cutOffXToReflectNoteIsEnding,
                graphicProperties,
                measureOffsetX,
            )
        val endY: Double = calcY(vectors.end.y, graphicProperties)

        PathBuilder()
            .moveTo(startX.toFloat(), startY.toFloat())
            .lineTo(endXWithCutOff.toFloat(), endY.toFloat())
            .nodes
    }
}

private fun calcX(
    x: Double,
    graphicProperties: GraphicProperties,
    measureOffsetX: Double,
): Double =
    (x / WHOLE_NOTE_WIDTH * graphicProperties.measureWidth) +
        (graphicProperties.offsetX + measureOffsetX)

private fun calcY(
    y: Double,
    graphicProperties: GraphicProperties,
): Double {
    // We need to transform from bottom left 0,0 to top left 0,0 coordinate system.
    val height = 32 // Why 32? Not sure, but it is the same in PathDataAsAmiMeasures.kt.

    val yFromTop: Double = height - y
    return (yFromTop * graphicProperties.wholeStepExpressedInY) + graphicProperties.offsetY
}
