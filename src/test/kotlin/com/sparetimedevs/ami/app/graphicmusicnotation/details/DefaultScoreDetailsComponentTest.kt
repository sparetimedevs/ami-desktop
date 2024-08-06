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

import arrow.core.NonEmptyList
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.store.PathDataStore
import com.sparetimedevs.ami.app.graphicmusicnotation.store.ScoreStore
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import com.sparetimedevs.ami.getTestComponentContext
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrumentName
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.example.getExampleScoreHeighHoNobodyHome
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForPart
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForScore
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import javax.sound.midi.MidiChannel

class DefaultScoreDetailsComponentTest :
    StringSpec({
        val testComponentContext: ComponentContext = getTestComponentContext()
        val scoreStore = ScoreStore()
        val graphicProperties =
            GraphicProperties(
                offsetX = 87.5,
                offsetY = 400.0,
                measureWidth = 500.0,
                spaceBetweenMeasures = 75.0,
                cutOffXToReflectNoteIsEnding = 0.0,
                wholeStepExpressedInY = 50.0,
            )
        val pathDataStore = PathDataStore(graphicProperties)
        val markInvalidInput = MarkInvalidInput()
        val scoreCoreComponent: MusicScoreDetailsComponent =
            DefaultMusicScoreDetailsComponent(
                testComponentContext,
                scoreStore,
                pathDataStore,
                markInvalidInput,
            )

        val component: ScoreDetailsComponent =
            DefaultScoreDetailsComponent(testComponentContext, scoreStore)

        "saveScoreDetails should return Right when there are no validation errors" {
            val score = getExampleScoreHeighHoNobodyHome()

            scoreCoreComponent.onLoadScoreClicked(score)

            val result =
                component.saveScoreDetails(
                    scoreId = "new-score-id",
                    scoreTitle = "The new title!",
                    partIds = emptyMap(),
                    partNames = emptyMap(),
                    partInstrumentNames =
                        score.parts.associate { it.id to "The new instrument name" },
                    partInstrumentMidiChannels = score.parts.associate { it.id to "6" },
                    partInstrumentMidiPrograms = emptyMap(),
                )

            result shouldBeRight Unit

            // The saved score should be unchanged.
            scoreCoreComponent.scoreValue.value shouldNotBe score
        }

        "saveScoreDetails should return all validation errors when there are multiple validation errors" {
            val score = getExampleScoreHeighHoNobodyHome()

            scoreCoreComponent.onLoadScoreClicked(score)

            val superLongString =
                "Some super long string, some super long string, Some super long string, Some super long string, Some super long string, some super long string, Some super long string, Some super long string, Some super long string, some super long string, Some super long string, Some super long string, Some super long string, some super long string, Some super long string, Some super long string, Some super long string, some super long string, Some super long string, Some super long string, Some super long string, some super long string, Some super long string, Some super long string, Some super long string."

            val result =
                component.saveScoreDetails(
                    scoreId = "",
                    scoreTitle = "",
                    partIds = emptyMap(),
                    partNames = emptyMap(),
                    partInstrumentNames = score.parts.associate { it.id to superLongString },
                    partInstrumentMidiChannels = score.parts.associate { it.id to "not a number!" },
                    partInstrumentMidiPrograms = emptyMap(),
                )

            result shouldBeLeft
                NonEmptyList(
                    ValidationError(
                        message = "Score ID can't be empty, the input was ",
                        validationErrorForProperty = validationErrorForProperty<ScoreId>(),
                        validationIdentifier =
                            ValidationIdentifierForScore(
                                identifier =
                                    ScoreId.unsafeCreate(
                                        input = "1064db99-3726-43d7-b0ed-3fc0281bfc02"
                                    ),
                                validationIdentifierParent = NoValidationIdentifier,
                            ),
                    ),
                    listOf(
                        ValidationError(
                            message =
                                "Part instrument name can't be longer than 512 characters, the input was $superLongString",
                            validationErrorForProperty =
                                validationErrorForProperty<PartInstrumentName>(),
                            validationIdentifier =
                                ValidationIdentifierForPart(
                                    identifier = PartId.unsafeCreate(input = "p-1"),
                                    validationIdentifierParent =
                                        ValidationIdentifierForScore(
                                            identifier =
                                                ScoreId.unsafeCreate(
                                                    input = "1064db99-3726-43d7-b0ed-3fc0281bfc02"
                                                ),
                                            validationIdentifierParent = NoValidationIdentifier,
                                        ),
                                ),
                        ),
                        ValidationError(
                            message = "MIDI channel should be a number but was not a number!",
                            validationErrorForProperty = validationErrorForProperty<MidiChannel>(),
                            validationIdentifier =
                                ValidationIdentifierForPart(
                                    identifier = PartId.unsafeCreate(input = "p-1"),
                                    validationIdentifierParent =
                                        ValidationIdentifierForScore(
                                            identifier =
                                                ScoreId.unsafeCreate(
                                                    input = "1064db99-3726-43d7-b0ed-3fc0281bfc02"
                                                ),
                                            validationIdentifierParent = NoValidationIdentifier,
                                        ),
                                ),
                        ),
                    ),
                )

            // The saved score should be unchanged.
            scoreCoreComponent.scoreValue.value shouldBe score
        }
    })
