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

package com.sparetimedevs.ami.app.graphicmusicnotation.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

const val ACTION_IDLE = 0
const val ACTION_DOWN = 1
const val ACTION_MOVE = 2

var motionEvent by mutableStateOf(ACTION_IDLE)

@Composable
fun DrawGraphicMusicNotationContent(
    component: DrawGraphicMusicNotationComponent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier.verticalScroll(rememberScrollState()).horizontalScroll(rememberScrollState())
    ) {
        var currentPosition by remember { mutableStateOf(Offset.Unspecified) }

        DrawBackdrop(modifier, component.getBackdrop(), component.getLineThickness())

        Canvas(
            modifier =
                Modifier.height(800.dp).width(2000.dp).pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown().also {
                            motionEvent = ACTION_DOWN
                            currentPosition = it.position
                        }

                        do {
                            // This PointerEvent contains details including events, id, position and
                            // more
                            val event: PointerEvent = awaitPointerEvent()
                            event.changes.forEach { pointerInputChange: PointerInputChange ->
                                if (pointerInputChange.positionChange() != Offset.Zero)
                                    pointerInputChange.consume()
                            }
                            motionEvent = ACTION_MOVE
                            currentPosition = event.changes.first().position
                        } while (event.changes.any { it.pressed })

                        motionEvent = ACTION_IDLE
                    }
                }
        ) {
            when (motionEvent) {
                ACTION_IDLE -> {
                    // Do nothing
                    // Maybe while motion event is idle, an undo is performed
                    performUndoWhenApplicable(component)
                }
                ACTION_DOWN -> {
                    processFirstPoint(component, currentPosition)
                }
                ACTION_MOVE -> {
                    if (currentPosition != Offset.Unspecified) {
                        // Sometimes the awaitFirstDown is not triggered? (This is also an issue for
                        // subsequent lines.)
                        val newPathNodeForHorizontalLine: PathNode =
                            PathBuilder().horizontalLineTo(currentPosition.x).nodes.first()
                        component.addToPathData(newPathNodeForHorizontalLine)
                    }
                }
            }
            drawPath(
                path = component.getPathData().asComposePath(),
                color = Color.Black,
                style = Stroke(width = component.getLineThickness())
            )
        }
    }
}

private fun processFirstPoint(
    component: DrawGraphicMusicNotationComponent,
    currentPosition: Offset
): Unit {
    val yValuesOfBackdrop =
        component.getBackdrop().flatMap { it.pathData.map { pathNode -> getYValue(pathNode) } }
    val yOnBackdrop: Float =
        yValuesOfBackdrop.reduce { acc: Float, next: Float ->
            val differenceBetweenNextYAndCurrentPositionY = (next - currentPosition.y).absoluteValue
            val differenceBetweenAccYAndCurrentPositionY = (acc - currentPosition.y).absoluteValue
            if (
                differenceBetweenNextYAndCurrentPositionY < differenceBetweenAccYAndCurrentPositionY
            )
                next
            else acc
        }

    val newPathNodeForStartingPoint: PathNode =
        PathBuilder().moveTo(currentPosition.x, yOnBackdrop).nodes.first()
    component.addToPathData(newPathNodeForStartingPoint)
}

private fun getYValue(pathNode: PathNode): Float =
    when (pathNode) {
        is PathNode.MoveTo -> pathNode.y
        // The other types of PathNode should actually not be relevant
        is PathNode.ArcTo -> pathNode.arcStartY
        is PathNode.CurveTo -> pathNode.y1
        is PathNode.LineTo -> pathNode.y
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
        is PathNode.Close -> Float.MIN_VALUE
        is PathNode.HorizontalTo -> Float.MIN_VALUE
        is PathNode.RelativeHorizontalTo -> Float.MIN_VALUE
    }

fun List<PathNode>.asComposePath(): Path {
    val path = Path()
    val parser = PathParser()
    return parser.addPathNodes(this).toPath(path)
}

private fun performUndoWhenApplicable(component: DrawGraphicMusicNotationComponent): Unit {
    if (shouldUndoLastCreatedLine()) component.undoLastCreatedLine()
    // And then set the boolean we checked to false.
}

private fun shouldUndoLastCreatedLine(): Boolean = false
