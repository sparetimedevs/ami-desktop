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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepository
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch

interface DrawGraphicMusicNotationComponent {

    fun getLineThickness(): Float

    fun getPathData(): List<PathNode>

    fun addToPathData(pathNode: PathNode): List<PathNode>

    fun undoLastCreatedLine(): Unit

    fun getBackdrop(): List<TheLineThatRepresentsAPitch>
}

class DefaultDrawGraphicMusicNotationComponent(
    componentContext: ComponentContext,
    private val pathDataRepository: PathDataRepository
) : DrawGraphicMusicNotationComponent, ComponentContext by componentContext {

    override fun getLineThickness(): Float = THICKNESS_OF_LINES

    override fun getPathData(): List<PathNode> = pathDataRepository.getPathData()

    override fun addToPathData(pathNode: PathNode): List<PathNode> =
        pathDataRepository.addToPathData(pathNode)

    override fun undoLastCreatedLine(): Unit {
        pathDataRepository.undoLastCreatedLine()
    }

    override fun getBackdrop(): List<TheLineThatRepresentsAPitch> {
        // There should be a backdrop behind the editor canvas to guide the user into how high/low
        // something is (colors!) and what the boarders of the measures are.

        // Keep in mind that:
        // 200 + 400 + 200 + 400 + 200 + 400 + 200 + 400 = 800 + 1600 = 2400 pixels width while the
        // dp of the window is set to 1200.
        val width = 500f
        val thicknessOfLines = getLineThickness()
        val offsetX = 87.5f
        val offsetY = 400f
        val spaceBetweenMeasures = 75f

        // See https://chroma-notes.com/
        return getTheLinesThatRepresentsAnOctave( // Measure 1
            octave = Octave.unsafeCreate(5),
            width = width,
            thicknessOfLines = thicknessOfLines,
            offsetX = offsetX,
            offsetY = offsetY
        ) +
            getTheLinesThatRepresentsAnOctave(
                octave = Octave.unsafeCreate(4),
                width = width,
                thicknessOfLines = thicknessOfLines,
                offsetX = offsetX,
                offsetY = offsetY + (12 * thicknessOfLines)
            ) +
            getTheLinesThatRepresentsAnOctave( // Measure 2
                octave = Octave.unsafeCreate(5),
                width = width,
                thicknessOfLines = thicknessOfLines,
                offsetX = offsetX + width + spaceBetweenMeasures,
                offsetY = offsetY
            ) +
            getTheLinesThatRepresentsAnOctave(
                octave = Octave.unsafeCreate(4),
                width = width,
                thicknessOfLines = thicknessOfLines,
                offsetX = offsetX + width + spaceBetweenMeasures,
                offsetY = offsetY + (12 * thicknessOfLines)
            ) +
            getTheLinesThatRepresentsAnOctave( // Measure 3
                octave = Octave.unsafeCreate(5),
                width = width,
                thicknessOfLines = thicknessOfLines,
                offsetX = offsetX + width + spaceBetweenMeasures + width + spaceBetweenMeasures,
                offsetY = offsetY
            ) +
            getTheLinesThatRepresentsAnOctave(
                octave = Octave.unsafeCreate(4),
                width = width,
                thicknessOfLines = thicknessOfLines,
                offsetX = offsetX + width + spaceBetweenMeasures + width + spaceBetweenMeasures,
                offsetY = offsetY + (12 * thicknessOfLines)
            ) +
            getTheLinesThatRepresentsAnOctave( // Measure 4
                octave = Octave.unsafeCreate(5),
                width = width,
                thicknessOfLines = thicknessOfLines,
                offsetX =
                    offsetX +
                        width +
                        spaceBetweenMeasures +
                        width +
                        spaceBetweenMeasures +
                        width +
                        spaceBetweenMeasures,
                offsetY = offsetY
            ) +
            getTheLinesThatRepresentsAnOctave(
                octave = Octave.unsafeCreate(4),
                width = width,
                thicknessOfLines = thicknessOfLines,
                offsetX =
                    offsetX +
                        width +
                        spaceBetweenMeasures +
                        width +
                        spaceBetweenMeasures +
                        width +
                        spaceBetweenMeasures,
                offsetY = offsetY + (12 * thicknessOfLines)
            )
    }

    private fun getTheLinesThatRepresentsAnOctave(
        octave: Octave,
        width: Float,
        thicknessOfLines: Float,
        offsetX: Float,
        offsetY: Float
    ): List<TheLineThatRepresentsAPitch> {
        // See https://chroma-notes.com/ for colors.
        return listOf(
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.B, octave = octave),
                color = Color(0xFFD000C9),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 1 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.A_SHARP, octave = octave),
                color = Color(0xFF9B00FB),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 2 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.A, octave = octave),
                color = Color(0xFF4823AC),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 3 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.G_SHARP, octave = octave),
                color = Color(0xFF2846DA),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 4 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.G, octave = octave),
                color = Color(0xFF355D4B),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 5 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.F_SHARP, octave = octave),
                color = Color(0xFF60AA28),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 6 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.F, octave = octave),
                color = Color(0xFFA6F138),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 7 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.E, octave = octave),
                color = Color(0xFFF3F33A),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 8 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.D_SHARP, octave = octave),
                color = Color(0xFFF2C82F),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 9 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.D, octave = octave),
                color = Color(0xFFDB9423),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 10 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.C_SHARP, octave = octave),
                color = Color(0xFFDD5812),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 11 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            ),
            TheLineThatRepresentsAPitch(
                pitch = Pitch(noteName = NoteName.C, octave = octave),
                color = Color(0xFFD00040),
                pathData =
                    PathBuilder()
                        .moveTo(0f + offsetX, 12 * thicknessOfLines + offsetY)
                        .horizontalLineTo(width + offsetX)
                        .getNodes()
            )
        )
    }

    companion object {
        private const val THICKNESS_OF_LINES = 25.0f
    }
}

data class TheLineThatRepresentsAPitch(
    val pitch: Pitch,
    val color: Color,
    val pathData: List<PathNode>
)
