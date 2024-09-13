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
import arrow.core.toEitherNel
import arrow.core.traverse
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.sparetimedevs.ami.app.utils.disposableScope
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationErrorForProperty
import com.sparetimedevs.ami.core.validation.ValidationErrorForUnknown
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

    private val _partMidiChannelsValue = MutableValue(emptyList<String>())
    override val partMidiChannelsValue: Value<List<String>> = _partMidiChannelsValue

    private val _partMidiProgramsValue = MutableValue(emptyList<String>())
    override val partMidiProgramsValue: Value<List<String>> = _partMidiProgramsValue

    private val _mappedValidationErrors =
        MutableValue(emptyMap<ValidationErrorForProperty, String>())
    override val mappedValidationErrorsValue: Value<Map<ValidationErrorForProperty, String>> =
        _mappedValidationErrors

    override fun updateScoreId(newValue: String) {
        _scoreIdValue.update { newValue }
    }

    override fun updateScoreTitle(newValue: String) {
        _scoreTitleValue.update { newValue }
    }

    override fun updatePartInstrumentName(partId: PartId, newValue: String) {
        _partInstrumentNamesValue.update { a -> a.plus(partId to newValue) }
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

        //        val validatedParts: EitherNel<ValidationError, Map<PartId, Part>> =
        // validateParts(_partInstrumentNamesValue.value)
        val validatedParts: EitherNel<ValidationError, Map<PartId, PartInstrumentName?>> =
            validateParts(_partInstrumentNamesValue.value)

        val accumulatedValidatedFields: Either<NonEmptyList<ValidationError>, Score> =
            Either.zipOrAccumulate(validatedScoreId, validatedScoreTitle, validatedParts) {
                id: ScoreId,
                title: ScoreTitle?,
                partInstrumentNames: Map<PartId, PartInstrumentName?> ->
                val parts =
                    scoreCoreComponent.scoreValue.value.parts.map { part: Part ->
                        if (partInstrumentNames.containsKey(part.id)) {
                            part.copy(
                                instrument =
                                    part.instrument?.copy(
                                        name = partInstrumentNames.getValue(part.id)
                                    )
                            )
                        } else {
                            part
                        }
                    }
                scoreCoreComponent.scoreValue.value.copy(id = id, title = title, parts = parts)
            }
        accumulatedValidatedFields.fold(
            { validationErrors ->
                val mappedValidationErrors: Map<ValidationErrorForProperty, String> =
                    validationErrors
                        .map { validationError ->
                            validationError.validationErrorForProperty to validationError.message
                        }
                        .toMap()
                _mappedValidationErrors.update { mappedValidationErrors }
            },
            { score: Score ->
                _mappedValidationErrors.update { emptyMap() }
                scoreCoreComponent.updateScore(score)
            },
        )
    }

    private fun validateParts(
        partInstrumentNamesValue: Map<PartId, String>
    ): EitherNel<ValidationError, Map<PartId, PartInstrumentName?>> {

        val xxx: Either<NonEmptyList<ValidationError>, Map<PartId, PartInstrumentName?>> =
            partInstrumentNamesValue
                .map { (t: PartId, u: String) ->
                    t to
                        PartInstrumentName.validate(
                                u,
                                ValidationErrorForUnknown,
                                NoValidationIdentifier,
                            )
                            .toEitherNel()
                }
                .toMap()
                .traverse { it }

        return xxx
    }
}
