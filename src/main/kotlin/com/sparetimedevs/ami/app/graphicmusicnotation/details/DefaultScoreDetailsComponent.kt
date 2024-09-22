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
import arrow.core.toEitherNel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.sparetimedevs.ami.app.utils.disposableScope
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationErrorFor
import com.sparetimedevs.ami.core.validation.ValidationErrorForPart
import com.sparetimedevs.ami.core.validation.ValidationErrorForUnknown
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationIdentifierForPart
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiChannel
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrumentName
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreTitle

internal class DefaultScoreDetailsComponent(
    componentContext: ComponentContext,
    private val scoreCoreComponent: MusicScoreDetailsComponent,
) :
    ScoreDetailsComponent,
    ComponentContext by componentContext,
    DisposableScope by componentContext.disposableScope() {

    override val scoreValue: Value<Score> = scoreCoreComponent.scoreValue

    private val _scoreIdValue = MutableValue("")
    override val scoreIdValue: Value<String> = _scoreIdValue

    private val _scoreTitleValue = MutableValue("")
    override val scoreTitleValue: Value<String> = _scoreTitleValue

    private val _partIdsValue = MutableValue(emptyList<String>())
    override val partIdsValue: Value<List<String>> = _partIdsValue
    // TODO add one more property. PartName
    private val _partNamesValue = MutableValue(emptyMap<PartId, String>())
    override val partNamesValue: Value<Map<PartId, String>> = _partNamesValue
    // TODO add one more property, but from a nested structure.
    // PartInstrument(name, midi-channel, midi-program)
    private val _partInstrumentNamesValue = MutableValue(emptyMap<PartId, String>())
    override val partInstrumentNamesValue: Value<Map<PartId, String>> = _partInstrumentNamesValue

    private val _partMidiChannelsValue = MutableValue(emptyMap<PartId, String>())
    override val partMidiChannelsValue: Value<Map<PartId, String>> = _partMidiChannelsValue

    private val _partMidiProgramsValue = MutableValue(emptyMap<PartId, String>())
    override val partMidiProgramsValue: Value<Map<PartId, String>> = _partMidiProgramsValue

    private val _mappedValidationErrors = MutableValue(emptyList<ValidationError>())
    override val mappedValidationErrorsValue: Value<List<ValidationError>> = _mappedValidationErrors

    override fun updateScoreId(newValue: String) {
        _scoreIdValue.update { newValue }
    }

    override fun updateScoreTitle(newValue: String) {
        _scoreTitleValue.update { newValue }
    }

    override fun updatePartInstrumentName(partId: PartId, newValue: String) {
        _partInstrumentNamesValue.update { a -> a.plus(partId to newValue) }
    }

    override fun createNewPart() {
        scoreCoreComponent.updateScore(
            scoreValue.value.copy(
                parts =
                    scoreValue.value.parts.plus(
                        Part(id = PartId(), name = null, instrument = null, measures = emptyList())
                    )
            )
        )
    }

    override fun updatePartMidiChannel(partId: PartId, newValue: String) {
        _partMidiChannelsValue.update { a -> a.plus(partId to newValue) }
    }

    override fun saveScoreDetails(): Unit {
        // TODO actually some other method should be invoked.
        // First do some validations.
        println("The scoreId is ${_scoreIdValue.value}")
        println("The scoreTitle is ${_scoreTitleValue.value}")
        println("The partIds is ${_partIdsValue.value}")
        // TODO and everything in between these two new values
        println("The partInstrumentNames is ${_partInstrumentNamesValue.value}")
        // TODO and these
        println("The partInstrumentMidiChannel is ${_partMidiChannelsValue.value}")
        println("The partInstrumentMidiProgram is ${_partMidiProgramsValue.value}")

        val validationErrorForScore = ValidationErrorForUnknown // TODO
        val validationIdentifierForScore = NoValidationIdentifier // TODO
        val validatedScoreId: EitherNel<ValidationError, ScoreId> =
            ScoreId.validate(_scoreIdValue.value, ValidationErrorForUnknown, NoValidationIdentifier)
                .toEitherNel()
        // TODO does need to account for nullability and being able to have empty string.
        val validatedScoreTitle: EitherNel<ValidationError, ScoreTitle> =
            ScoreTitle.validate(
                    _scoreTitleValue.value,
                    ValidationErrorForUnknown,
                    NoValidationIdentifier,
                )
                .toEitherNel()

        val validatedParts: EitherNel<ValidationError, List<Part>> =
            validateParts(
                scoreCoreComponent.scoreValue.value.parts,
                _partInstrumentNamesValue.value,
                _partMidiChannelsValue.value,
                validationErrorForScore,
                validationIdentifierForScore,
            )

        val accumulatedValidatedFields: Either<NonEmptyList<ValidationError>, Score> =
            Either.zipOrAccumulate(validatedScoreId, validatedScoreTitle, validatedParts) {
                id: ScoreId,
                title: ScoreTitle?,
                parts: List<Part> ->
                scoreCoreComponent.scoreValue.value.copy(id = id, title = title, parts = parts)
            }
        accumulatedValidatedFields.fold(
            { validationErrors -> _mappedValidationErrors.update { validationErrors } },
            { score: Score ->
                _mappedValidationErrors.update { emptyList() }
                scoreCoreComponent.updateScore(score)
            },
        )
    }

    private fun validateParts(
        existingParts: List<Part>,
        partInstrumentNamesValue: Map<PartId, String>,
        partMidiChannelsValue: Map<PartId, String>,
        validationErrorFor: ValidationErrorFor,
        validationIdentifier: ValidationIdentifier,
    ): EitherNel<ValidationError, List<Part>> {
        val validatedPartInstrumentNames:
            Either<NonEmptyList<ValidationError>, Map<PartId, PartInstrumentName?>> =
            partInstrumentNamesValue
                .map { (partId: PartId, s: String) ->
                    partId to
                        PartInstrumentName.validate(
                                s,
                                ValidationErrorForPart.fromParent(validationErrorFor, partId.value),
                                ValidationIdentifierForPart(partId.value, validationIdentifier),
                            )
                            .toEitherNel()
                }
                .toMap()
                .let { m -> either { mapOrAccumulate(m) { it.value.bindNel() } } }

        val validatedPartMidiChannels =
            partMidiChannelsValue
                .map { (partId: PartId, s: String) ->
                    Either.catch { s.toByte() }
                        .mapLeft {
                            ValidationError(
                                    message = "MIDI channel should be a number but was $s",
                                    validationErrorForProperty =
                                        validationErrorForProperty<MidiChannel>(),
                                    validationIdentifier =
                                        ValidationIdentifierForPart(
                                            partId.value,
                                            validationIdentifier,
                                        ),
                                    validationErrorFor =
                                        ValidationErrorForPart.fromParent(
                                            validationErrorFor,
                                            partId.value,
                                        ),
                                )
                                .nel()
                        }
                        .flatMap { byte ->
                            MidiChannel.validate(
                                    byte,
                                    ValidationErrorForUnknown,
                                    NoValidationIdentifier,
                                )
                                .map { midiChannel -> partId to midiChannel }
                                .mapLeft { it.nel() }
                        }
                }
                .let { l -> either { mapOrAccumulate(l) { it.bindNel() }.toMap() } }

        return Either.zipOrAccumulate(validatedPartInstrumentNames, validatedPartMidiChannels) {
            partInstrumentNames,
            partMidiChannels ->
            existingParts
                .map { part: Part ->
                    if (partInstrumentNames.containsKey(part.id)) {
                        part.copy(
                            instrument =
                                part.instrument?.copy(name = partInstrumentNames.getValue(part.id))
                        )
                    } else {
                        part
                    }
                }
                .map { part: Part ->
                    if (partMidiChannels.containsKey(part.id)) {
                        part.copy(
                            instrument =
                                part.instrument?.copy(
                                    midiChannel = partMidiChannels.getValue(part.id)
                                )
                        )
                    } else {
                        part
                    }
                }
        }
    }
}
