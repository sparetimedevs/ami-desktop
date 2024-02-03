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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
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
import com.sparetimedevs.ami.app.graphicmusicnotation.place.PlaceGraphicMusicNotationComponent

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

        //        DrawBackdrop(modifier, component.getBackdrop(), component.getLineThickness())

        Text("Here we are going to make the awesome place graphic music notation functionality")

        //        Canvas(
        //            modifier =
        //                Modifier.height(800.dp).width(2000.dp).pointerInput(Unit) {
        //                    awaitEachGesture {
        //                        awaitFirstDown().also {
        //                            motionEvent = ACTION_DOWN
        //                            currentPosition = it.position
        //                        }
        //
        //                        do {
        //                            // This PointerEvent contains details including events, id,
        // position and
        //                            // more
        //                            val event: PointerEvent = awaitPointerEvent()
        //                            event.changes.forEach { pointerInputChange: PointerInputChange
        // ->
        //                                if (pointerInputChange.positionChange() != Offset.Zero)
        //                                    pointerInputChange.consume()
        //                            }
        //                            motionEvent = ACTION_MOVE
        //                            currentPosition = event.changes.first().position
        //                        } while (event.changes.any { it.pressed })
        //
        //                        motionEvent = ACTION_IDLE
        //                    }
        //                }
        //        ) {
        //            when (motionEvent) {
        //                ACTION_IDLE -> {
        //                    // Do nothing
        //                    // Maybe while motion event is idle, an undo is performed
        //                    performUndoWhenApplicable(component)
        //                }
        //                ACTION_DOWN -> {
        //                    processFirstPoint(component, currentPosition)
        //                }
        //                ACTION_MOVE -> {
        //                    if (currentPosition != Offset.Unspecified) {
        //                        // Sometimes the awaitFirstDown is not triggered? (This is also an
        // issue for
        //                        // subsequent lines.)
        //                        val newPathNodeForHorizontalLine: PathNode =
        //
        // PathBuilder().horizontalLineTo(currentPosition.x).getNodes().first()
        //                        component.addToPathData(newPathNodeForHorizontalLine)
        //                    }
        //                }
        //            }
        //            drawPath(
        //                path = component.getPathData().asComposePath(),
        //                color = Color.Black,
        //                style = Stroke(width = component.getLineThickness())
        //            )
        //        }
    }
}
