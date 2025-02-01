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
import arrow.core.EitherNel
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteAttributes
import com.sparetimedevs.ami.music.data.kotlin.note.NoteDuration
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch
import com.sparetimedevs.ami.music.example.getExampleScoreHeighHoNobodyHome
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec

class PathDataAsAmiMeasuresKtTest :
    StringSpec({
        val graphicProperties =
            GraphicProperties(
                offsetX = 87.5,
                offsetY = 400.0,
                measureWidth = 500.0,
                spaceBetweenMeasures = 75.0,
                cutOffXToReflectNoteIsEnding = 0.0,
                wholeStepExpressedInY = 50.0,
            )

        "asAmiMeasure should work for 1 measure" {
            val pathData: List<PathNode> =
                PathBuilder().moveTo(x = 87.5f, y = 700.0f).horizontalLineTo(x = 587.5f).nodes

            val expectedResult: List<Measure> =
                listOf(
                    Measure(
                        null,
                        listOf(
                            Note.Pitched(
                                NoteDuration(NoteValue.WHOLE),
                                NoteAttributes(null, null, null, null),
                                Pitch(NoteName.C, Octave.unsafeCreate(4)),
                            ),
                        ),
                    ),
                )

            val result: EitherNel<ValidationError, List<Measure>> =
                pathData.asAmiMeasures(graphicProperties)

            result shouldBeRight expectedResult
        }

        "asAmiMeasure should work for 2 measures" {
            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = 87.5f, y = 700.0f)
                    .horizontalLineTo(x = 587.5f)
                    .moveTo(x = 662.5f, y = 750.0f)
                    .horizontalLineTo(x = 1162.5f)
                    .nodes

            val expectedResult: List<Measure> =
                listOf(
                    Measure(
                        null,
                        listOf(
                            Note.Pitched(
                                NoteDuration(NoteValue.WHOLE),
                                NoteAttributes(null, null, null, null),
                                Pitch(NoteName.C, Octave.unsafeCreate(4)),
                            ),
                        ),
                    ),
                    Measure(
                        null,
                        listOf(
                            Note.Pitched(
                                NoteDuration(NoteValue.WHOLE),
                                NoteAttributes(null, null, null, null),
                                Pitch(NoteName.B_FLAT, Octave.unsafeCreate(4)),
                            ),
                        ),
                    ),
                )

            val result: EitherNel<ValidationError, List<Measure>> =
                pathData.asAmiMeasures(graphicProperties)

            result shouldBeRight expectedResult
        }

        "asAmiMeasure should work for first 4 measures of example score Heigh Ho Nobody Home" {
            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = 87.5f, y = 650.0f)
                    .lineTo(x = 337.5f, y = 650.0f)
                    .moveTo(x = 337.5f, y = 700.0f)
                    .lineTo(x = 587.5f, y = 700.0f)
                    .moveTo(x = 662.5f, y = 650.0f)
                    .lineTo(x = 787.5f, y = 650.0f)
                    .moveTo(x = 787.5f, y = 650.0f)
                    .lineTo(x = 850.0f, y = 650.0f)
                    .moveTo(x = 850.0f, y = 650.0f)
                    .lineTo(x = 912.5f, y = 650.0f)
                    .moveTo(x = 912.5f, y = 775.0f)
                    .lineTo(x = 1162.5f, y = 775.0f)
                    .moveTo(x = 1237.5f, y = 650.0f)
                    .lineTo(x = 1362.5f, y = 650.0f)
                    .moveTo(x = 1362.5f, y = 650.0f)
                    .lineTo(x = 1487.5f, y = 650.0f)
                    .moveTo(x = 1487.5f, y = 600.0f)
                    .lineTo(x = 1612.5f, y = 600.0f)
                    .moveTo(x = 1612.5f, y = 600.0f)
                    .lineTo(x = 1737.5f, y = 600.0f)
                    .moveTo(x = 1812.5f, y = 575.0f)
                    .lineTo(x = 1875.0f, y = 575.0f)
                    .moveTo(x = 1875.0f, y = 575.0f)
                    .lineTo(x = 1937.5f, y = 575.0f)
                    .moveTo(x = 1937.5f, y = 575.0f)
                    .lineTo(x = 2000.0f, y = 575.0f)
                    .moveTo(x = 2000.0f, y = 575.0f)
                    .lineTo(x = 2062.5f, y = 575.0f)
                    .moveTo(x = 2062.5f, y = 600.0f)
                    .lineTo(x = 2312.5f, y = 600.0f)
                    .nodes

            val score = getExampleScoreHeighHoNobodyHome()
            val expectedMeasures: List<Measure> = score.parts[0].measures.take(4)

            val result: EitherNel<ValidationError, List<Measure>> =
                pathData.asAmiMeasures(graphicProperties)

            result shouldBeRight expectedMeasures
        }
    })
