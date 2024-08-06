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

import arrow.core.Either
import arrow.core.EitherNel
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.nel
import arrow.core.raise.either
import arrow.core.raise.mapOrAccumulate
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.sparetimedevs.ami.app.graphicmusicnotation.store.ScoreStore
import com.sparetimedevs.ami.app.utils.disposableScope
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiChannel
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiProgram
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrument
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrumentName
import com.sparetimedevs.ami.music.data.kotlin.part.PartName
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreTitle
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForPart
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForScore

internal class DefaultScoreDetailsComponent(
    componentContext: ComponentContext,
    private val scoreStore: ScoreStore,
) :
    ScoreDetailsComponent,
    ComponentContext by componentContext,
    DisposableScope by componentContext.disposableScope() {

    override val scoreValue: Value<Score> = scoreStore.scoreValue

    override fun createNewPart() {
        scoreStore.saveScore(
            scoreValue.value.copy(
                parts =
                    scoreValue.value.parts.plus(
                        Part(id = PartId(), name = null, instrument = null, measures = emptyList())
                    )
            )
        )
    }

    override fun saveScoreDetails(
        scoreId: String,
        scoreTitle: String?,
        partIds: Map<PartId, String>,
        partNames: Map<PartId, String?>,
        partInstrumentNames: Map<PartId, String?>,
        partInstrumentMidiChannels: Map<PartId, String?>,
        partInstrumentMidiPrograms: Map<PartId, String?>,
    ): EitherNel<ValidationError, Unit> {
        val validationIdentifierForScore =
            ValidationIdentifierForScore(scoreValue.value.id, NoValidationIdentifier)
        val validatedScoreId: EitherNel<ValidationError, ScoreId> =
            ScoreId.validate(scoreId, validationIdentifierForScore)
        val validatedScoreTitle: EitherNel<ValidationError, ScoreTitle?> =
            ScoreTitle.validate(scoreTitle, validationIdentifierForScore)

        val validatedParts: EitherNel<ValidationError, List<Part>> =
            validateParts(
                scoreValue.value.parts,
                partIds,
                partNames,
                partInstrumentNames,
                partInstrumentMidiChannels,
                partInstrumentMidiPrograms,
                validationIdentifierForScore,
            )

        val accumulatedValidatedFields: Either<NonEmptyList<ValidationError>, Score> =
            Either.zipOrAccumulate(validatedScoreId, validatedScoreTitle, validatedParts) {
                id: ScoreId,
                title: ScoreTitle?,
                parts: List<Part> ->
                scoreValue.value.copy(id = id, title = title, parts = parts)
            }
        return accumulatedValidatedFields.map { score: Score -> scoreStore.saveScore(score) }
    }

    private fun validateParts(
        existingParts: List<Part>,
        partIds: Map<PartId, String>,
        partNames: Map<PartId, String?>,
        partInstrumentNames: Map<PartId, String?>,
        partInstrumentMidiChannels: Map<PartId, String?>,
        partInstrumentMidiPrograms: Map<PartId, String?>,
        validationIdentifier: ValidationIdentifier,
    ): EitherNel<ValidationError, List<Part>> {
        val validatedPartIds: EitherNel<ValidationError, Map<PartId, PartId>> =
            partIds
                .map { (partId: PartId, s: String) ->
                    partId to
                        PartId.validate(
                            s,
                            ValidationIdentifierForPart(partId, validationIdentifier),
                        )
                }
                .toMap()
                .let { m -> either { mapOrAccumulate(m) { it.value.bindNel() } } }

        val validatedPartNames: EitherNel<ValidationError, Map<PartId, PartName?>> =
            partNames
                .map { (partId: PartId, s: String?) ->
                    partId to
                        PartName.validate(
                            s,
                            ValidationIdentifierForPart(partId, validationIdentifier),
                        )
                }
                .toMap()
                .let { m -> either { mapOrAccumulate(m) { it.value.bindNel() } } }

        val validatedPartInstrumentNames:
            EitherNel<ValidationError, Map<PartId, PartInstrumentName?>> =
            partInstrumentNames
                .map { (partId: PartId, s: String?) ->
                    partId to
                        PartInstrumentName.validate(
                            s,
                            ValidationIdentifierForPart(partId, validationIdentifier),
                        )
                }
                .toMap()
                .let { m -> either { mapOrAccumulate(m) { it.value.bindNel() } } }

        val validatedPartInstrumentMidiChannels =
            partInstrumentMidiChannels
                .map { (partId: PartId, s: String?) ->
                    Either.catch { s?.toByte() }
                        .mapLeft {
                            ValidationError(
                                    message = "MIDI channel should be a number but was $s",
                                    validationErrorForProperty =
                                        validationErrorForProperty<MidiChannel>(),
                                    validationIdentifier =
                                        ValidationIdentifierForPart(partId, validationIdentifier),
                                )
                                .nel()
                        }
                        .flatMap { byte ->
                            MidiChannel.validate(
                                    byte,
                                    ValidationIdentifierForPart(partId, validationIdentifier),
                                )
                                .map { midiChannel -> partId to midiChannel }
                        }
                }
                .let { l -> either { mapOrAccumulate(l) { it.bindNel() }.toMap() } }

        val validatedPartInstrumentMidiPrograms =
            partInstrumentMidiPrograms
                .map { (partId: PartId, s: String?) ->
                    Either.catch { s?.toByte() }
                        .mapLeft {
                            ValidationError(
                                    message = "MIDI program should be a number but was $s",
                                    validationErrorForProperty =
                                        validationErrorForProperty<MidiProgram>(),
                                    validationIdentifier =
                                        ValidationIdentifierForPart(partId, validationIdentifier),
                                )
                                .nel()
                        }
                        .flatMap { byte ->
                            MidiProgram.validate(
                                    byte,
                                    ValidationIdentifierForPart(partId, validationIdentifier),
                                )
                                .map { midiProgram -> partId to midiProgram }
                        }
                }
                .let { l -> either { mapOrAccumulate(l) { it.bindNel() }.toMap() } }

        return Either.zipOrAccumulate(
            validatedPartIds,
            validatedPartNames,
            validatedPartInstrumentNames,
            validatedPartInstrumentMidiChannels,
            validatedPartInstrumentMidiPrograms,
        ) {
            partIds,
            partNames,
            partInstrumentNames,
            partInstrumentMidiChannels,
            partInstrumentMidiPrograms ->
            existingParts
                .map { part: Part ->
                    if (partNames.containsKey(part.id)) {
                        part.copy(name = partNames.getValue(part.id))
                    } else {
                        part
                    }
                }
                .map { part: Part ->
                    if (partInstrumentNames.containsKey(part.id)) {
                        part.copy(
                            instrument =
                                part.instrument?.copy(name = partInstrumentNames.getValue(part.id))
                                    ?: PartInstrument(name = partInstrumentNames.getValue(part.id))
                        )
                    } else {
                        part
                    }
                }
                .map { part: Part ->
                    if (partInstrumentMidiChannels.containsKey(part.id)) {
                        part.copy(
                            instrument =
                                part.instrument?.copy(
                                    midiChannel = partInstrumentMidiChannels.getValue(part.id)
                                )
                                    ?: PartInstrument(
                                        midiChannel = partInstrumentMidiChannels.getValue(part.id)
                                    )
                        )
                    } else {
                        part
                    }
                }
                .map { part: Part ->
                    if (partInstrumentMidiPrograms.containsKey(part.id)) {
                        part.copy(
                            instrument =
                                part.instrument?.copy(
                                    midiProgram = partInstrumentMidiPrograms.getValue(part.id)
                                )
                                    ?: PartInstrument(
                                        midiProgram = partInstrumentMidiPrograms.getValue(part.id)
                                    )
                        )
                    } else {
                        part
                    }
                }
                .map { part: Part ->
                    // Changing the part ID if it was changed as last, else the other changes
                    // wouldn't work because the part ID is used as a key in the maps
                    if (partIds.containsKey(part.id)) {
                        part.copy(id = partIds.getValue(part.id))
                    } else {
                        part
                    }
                }
        }
    }
}
