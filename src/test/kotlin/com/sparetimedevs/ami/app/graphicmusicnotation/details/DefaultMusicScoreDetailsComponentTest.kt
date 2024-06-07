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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepositoryImpl
import com.sparetimedevs.ami.getTestComponentContext
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.core.replaceMeasures
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteAttributes
import com.sparetimedevs.ami.music.data.kotlin.note.NoteDuration
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch
import com.sparetimedevs.ami.music.example.getExampleScoreHeighHoNobodyHome
import com.sparetimedevs.ami.test.data.createEmptyScore
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec

class DefaultMusicScoreDetailsComponentTest :
    StringSpec({
        val testComponentContext: ComponentContext = getTestComponentContext()
        val graphicProperties =
            GraphicProperties(
                offsetX = 87.5,
                offsetY = 400.0,
                measureWidth = 500.0,
                spaceBetweenMeasures = 75.0,
                cutOffXToReflectNoteIsEnding = 0.0,
                wholeStepExpressedInY = 50.0
            )
        val pathDataRepository = PathDataRepositoryImpl(graphicProperties)
        val component: MusicScoreDetailsComponent =
            DefaultMusicScoreDetailsComponent(testComponentContext, pathDataRepository)

        "updateAndGetScore should return score when there are no changes made to it in reading mode" {
            val score = getExampleScoreHeighHoNobodyHome()

            component.changeMode(GraphicMusicNotationMode.READING)
            component.onLoadScoreClicked(score)

            val result = component.updateAndGetScore()

            result shouldBeRight score
        }

        "updateAndGetScore should return score when there are no changes made to it in drawing mode" {
            val score = getExampleScoreHeighHoNobodyHome()

            component.changeMode(GraphicMusicNotationMode.DRAWING)
            component.onLoadScoreClicked(score)

            val result = component.updateAndGetScore()

            result shouldBeRight score
        }

        "updateAndGetScore should return updated score when there are changes made to it in drawing mode" {
            val score = getExampleScoreHeighHoNobodyHome()

            component.changeMode(GraphicMusicNotationMode.DRAWING)
            component.onLoadScoreClicked(score)

            val pathData: List<PathNode> =
                PathBuilder().moveTo(x = 87.5f, y = 700.0f).horizontalLineTo(x = 587.5f).nodes
            pathDataRepository.replacePathData(pathData)

            val result = component.updateAndGetScore()

            val expectedMeasures: List<Measure> =
                listOf(
                    Measure(
                        null,
                        listOf(
                            Note.Pitched(
                                NoteDuration(NoteValue.WHOLE),
                                NoteAttributes(null, null, null, null),
                                Pitch(NoteName.C, Octave.unsafeCreate(4))
                            )
                        )
                    )
                )
            val expectedScore = score.replaceMeasures(expectedMeasures)

            result shouldBeRight expectedScore
        }

        "updateAndGetScore should return updated score when there are changes made to it in drawing mode and Score part was empty" {
            val score = createEmptyScore()

            component.changeMode(GraphicMusicNotationMode.DRAWING)
            component.onLoadScoreClicked(score)

            val pathData: List<PathNode> =
                PathBuilder().moveTo(x = 87.5f, y = 700.0f).horizontalLineTo(x = 587.5f).nodes
            pathDataRepository.replacePathData(pathData)

            val result = component.updateAndGetScore()

            val expectedMeasures: List<Measure> =
                listOf(
                    Measure(
                        null,
                        listOf(
                            Note.Pitched(
                                NoteDuration(NoteValue.WHOLE),
                                NoteAttributes(null, null, null, null),
                                Pitch(NoteName.C, Octave.unsafeCreate(4))
                            )
                        )
                    )
                )
            val expectedScore = score.replaceMeasures(expectedMeasures)

            result shouldBeRight expectedScore
        }
    })
