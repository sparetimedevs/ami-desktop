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

package com.sparetimedevs.ami.app.graphicmusicnotation.place

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.asComposePath

const val ACTION_IDLE = 0
const val ACTION_DOWN = 1
const val ACTION_MOVE = 2

var motionEvent by mutableStateOf(ACTION_IDLE)

@Composable
fun PlaceGraphicMusicNotationContent(
    component: PlaceGraphicMusicNotationComponent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier.verticalScroll(rememberScrollState()).horizontalScroll(rememberScrollState())
    ) {
        var currentPosition by remember { mutableStateOf(Offset.Unspecified) }

        PlaceGraphicMusicNotationBackdrop(modifier, component.getLineThickness())

        Text("Here we are going to make the awesome place graphic music notation functionality")

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

                    // performUndoWhenApplicable(component)
                }
                ACTION_DOWN -> {
                    // processFirstPoint(component, currentPosition)
                }
                ACTION_MOVE -> {
                    if (currentPosition != Offset.Unspecified) {
                        // Sometimes the awaitFirstDown is not triggered? (This is also an issue for
                        // subsequent lines.)
                        val newPathNodeForHorizontalLine: PathNode =
                            PathBuilder().horizontalLineTo(currentPosition.x).nodes.first()

                        // component.addToPathData(newPathNodeForHorizontalLine)
                    }
                }
            }

            val circleColor: Color = Color(0xFFFF7272)
            val getCircleSize = 30.0f

            val offsetX = 117.5f
            val offsetY = 370.0f
            val crossSize = 24.0f

            val x1: Float = calcX(offsetX, 0, crossSize)
            val y1: Float = calcY(offsetY, 0, crossSize)
            val x2: Float = calcX(offsetX, 1, crossSize)
            val y2: Float = calcY(offsetY, 1, crossSize)
            val x3Start: Float = calcX(offsetX, 2, crossSize)
            val y3Start: Float = calcY(offsetY, 2, crossSize)
            val x3End: Float = calcX(offsetX, 3, crossSize)
            val x4CStart: Float = calcX(offsetX, 4, crossSize)
            val y4CStart: Float = calcY(offsetY, 11, crossSize)
            val x4CEnd: Float = calcX(offsetX, 7, crossSize)
            val x4EStart: Float = calcX(offsetX, 4, crossSize)
            val y4EStart: Float = calcY(offsetY, 7, crossSize)
            val x4EEnd: Float = calcX(offsetX, 7, crossSize)
            val x4GStart: Float = calcX(offsetX, 4, crossSize)
            val y4GStart: Float = calcY(offsetY, 4, crossSize)
            val x4GEnd: Float = calcX(offsetX, 7, crossSize)

            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = x1, y = y1)
                    .lineToRelative(0.0f, 0.0f)
                    .moveTo(x = x2, y = y2)
                    .lineToRelative(0.0f, 0.0f)
                    .moveTo(x = x3Start, y = y3Start)
                    .horizontalLineTo(x = x3End)
                    .moveTo(x = x4CStart, y = y4CStart)
                    .horizontalLineTo(x = x4CEnd)
                    .moveTo(x = x4EStart, y = y4EStart)
                    .horizontalLineTo(x = x4EEnd)
                    .moveTo(x = x4GStart, y = y4GStart)
                    .horizontalLineTo(x = x4GEnd)
                    .nodes

            drawPath(
                path = pathData.asComposePath(),
                color = circleColor,
                style = Stroke(width = getCircleSize, cap = StrokeCap.Round)
            )
        }
    }
}
