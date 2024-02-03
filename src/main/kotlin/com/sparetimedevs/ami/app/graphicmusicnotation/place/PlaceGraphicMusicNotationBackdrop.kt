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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.TheLineThatRepresentsAPitch
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.asComposePath
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch

val backgroundColor: Color = Color.White

@Composable
fun PlaceGraphicMusicNotationBackdrop(modifier: Modifier = Modifier, lineThickness: Float) {
    Canvas(modifier = modifier.height(800.dp).width(2000.dp).background(backgroundColor)) {
        //        drawBackdrop(lineThickness)
        drawLineOfCrosses(lineThickness)
    }
}

private fun DrawScope.drawLineOfCrosses(lineThickness: Float): Unit {
    val color: Color = Color(0xFF9B00FB)
    val size: Float = 24f
    val offsetX: Float = 87.5f
    val offsetY: Float = 400f
    (1..500).toList().forEach { crossNumber -> this.drawCross(lineThickness, crossNumber, color) }
}

private fun DrawScope.drawCross(lineThickness: Float, crossNumber: Int, color: Color): Unit {
    val size: Float = 24f
    val offsetX: Float = 57.5f + (crossNumber * size * 1.25f)
    val offsetY: Float = 400f
    drawPath(
        path =
            PathBuilder()
                .moveTo(0f + offsetX, offsetY)
                .horizontalLineTo(size + offsetX)
                .moveTo(0f + offsetX + (size / 2), offsetY - (size / 2))
                .verticalLineTo((size / 2) + offsetY)
                .getNodes()
                .asComposePath(),
        color = color,
        style = Stroke(width = lineThickness)
    )
}

private fun DrawScope.drawBackdrop(lineThickness: Float): Unit {
    backdropLines(lineThickness).forEach { l: TheLineThatRepresentsAPitch ->
        drawPath(
            path = l.pathData.asComposePath(),
            color = l.color,
            style = Stroke(width = lineThickness)
        )
    }
}

private fun backdropLines(lineThickness: Float): List<TheLineThatRepresentsAPitch> =
    listOf(
        TheLineThatRepresentsAPitch(
            pitch = Pitch(noteName = NoteName.B, octave = Octave.unsafeCreate(4)),
            color = Color(0xFFD000C9),
            pathData =
                PathBuilder()
                    .moveTo(0f + 87.5f, 1 * lineThickness + 400f)
                    .horizontalLineTo(500f + 87.5f)
                    .getNodes()
        )
    )
