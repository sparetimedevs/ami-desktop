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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.asComposePath
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch

val BackgroundColor: Color = Color(0xFFFAF5F0)

@Composable
fun PlaceGraphicMusicNotationBackdrop(modifier: Modifier = Modifier, lineThickness: Float) {
    Canvas(modifier = modifier.height(800.dp).width(2000.dp).background(BackgroundColor)) {
        drawGridOfCrosses(lineThickness)
    }
}

private fun DrawScope.drawGridOfCrosses(lineThickness: Float): Unit {
    val octave: Octave = Octave.unsafeCreate(4)
    val size: Float = 24f
    val offsetX: Float = 57.5f
    val offsetY: Float = 370f
    val oneOctave: List<PitchAndColor> =
        listOf(
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.B, octave = octave),
                color = Color(0xFFD000C9),
                contrastColor = Color(0xFF444444)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.A_SHARP, octave = octave),
                color = Color(0xFF9B00FB),
                contrastColor = Color.Gray
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.A, octave = octave),
                color = Color(0xFF4823AC),
                contrastColor = Color(0xFFFAF5F0)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.G_SHARP, octave = octave),
                color = Color(0xFF2846DA),
                contrastColor = Color(0xFFFAF5F0)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.G, octave = octave),
                color = Color(0xFF355D4B),
                contrastColor = Color(0xFFFAF5F0)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.F_SHARP, octave = octave),
                color = Color(0xFF60AA28),
                contrastColor = Color(0xFF444444)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.F, octave = octave),
                color = Color(0xFFA6F138),
                contrastColor = Color(0xFF444444)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.E, octave = octave),
                color = Color(0xFFF3F33A),
                contrastColor = Color(0xFF444444)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.D_SHARP, octave = octave),
                color = Color(0xFFF2C82F),
                contrastColor = Color(0xFF444444)
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.D, octave = octave),
                color = Color(0xFFDB9423),
                contrastColor = Color.Gray
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.C_SHARP, octave = octave),
                color = Color(0xFFDD5812),
                contrastColor = Color.Gray
            ),
            PitchAndColor(
                pitch = Pitch(noteName = NoteName.C, octave = octave),
                color = Color(0xFFD00040),
                contrastColor = Color.Gray
            )
        )
    oneOctave.forEachIndexed { crossColumnNumber, pitchAndColor ->
        val y: Float = calcY(offsetY, crossColumnNumber, size)
        this.drawLine(
            pitchAndColor.contrastColor,
            Offset(offsetX, y),
            Offset(offsetX + 2000f, y),
            size * 2
        )
        this.drawLineOfCrosses(
            crossColumnNumber,
            pitchAndColor,
            lineThickness,
            size,
            offsetX,
            offsetY
        )
    }
}

private fun DrawScope.drawLineOfCrosses(
    crossColumnNumber: Int,
    pitchAndColor: PitchAndColor,
    lineThickness: Float,
    size: Float,
    offsetX: Float,
    offsetY: Float
): Unit {
    val color: Color = pitchAndColor.color
    (1..500).toList().forEach { crossRowNumber ->
        this.drawCross(
            crossRowNumber,
            crossColumnNumber,
            color,
            lineThickness,
            size,
            offsetX,
            offsetY
        )
    }
}

private fun DrawScope.drawCross(
    crossRowNumber: Int,
    crossColumnNumber: Int,
    color: Color,
    lineThickness: Float,
    size: Float,
    offsetX: Float,
    offsetY: Float
): Unit {
    val x: Float = calcX(offsetX, crossRowNumber, size)
    val y: Float = calcY(offsetY, crossColumnNumber, size)
    drawPath(
        path =
            PathBuilder()
                .moveTo(0f + x, y)
                .horizontalLineTo(size + x)
                .moveTo(0f + x + (size / 2), y - (size / 2))
                .verticalLineTo((size / 2) + y)
                .getNodes()
                .asComposePath(),
        color = color,
        style = Stroke(width = lineThickness)
    )
}

data class PitchAndColor(val pitch: Pitch, val color: Color, val contrastColor: Color)

fun calcX(offsetX: Float, crossRowNumber: Int, size: Float): Float =
    offsetX + (crossRowNumber * size * 2f)

fun calcY(offsetY: Float, crossColumnNumber: Int, size: Float): Float =
    offsetY + (crossColumnNumber * size * 2f)
