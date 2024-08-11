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
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreTitle

internal class DefaultScoreDetailsComponent(
    componentContext: ComponentContext,
    private val scoreCoreComponent: MusicScoreDetailsComponent
) :
    ScoreDetailsComponent,
    ComponentContext by componentContext,
    DisposableScope by componentContext.disposableScope() {

    private val _scoreIdValue = MutableValue("")
    override val scoreIdValue: Value<String> = _scoreIdValue

    private val _scoreTitleValue = MutableValue("")
    override val scoreTitleValue: Value<String> = _scoreTitleValue

    // TODO add one more property, but from a nested structure. Like PartId
    private val _partIdsValue = MutableValue(emptyList<String>())
    override val partIdsValue: Value<List<String>> = _partIdsValue

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

    override fun saveScoreDetails(): Unit {
        // TODO actually some other method should be invoked.
        // First do some validations.
        println("The scoreId is ${_scoreIdValue.value}")
        println("The scoreTitle is ${_scoreTitleValue.value}")
        val validatedScoreId: EitherNel<ValidationError, ScoreId> =
            ScoreId.validate(_scoreIdValue.value, ValidationErrorForUnknown, NoValidationIdentifier)
                .toEitherNel()
        // TODO does need to account for nullability and being able to have empty string.
        val validatedScoreTitle: EitherNel<ValidationError, ScoreTitle> =
            ScoreTitle.validate(
                    _scoreTitleValue.value,
                    ValidationErrorForUnknown,
                    NoValidationIdentifier
                )
                .toEitherNel()

        val accumulatedValidatedFields:
            Either<NonEmptyList<ValidationError>, Pair<ScoreId, ScoreTitle?>> =
            Either.zipOrAccumulate(validatedScoreId, validatedScoreTitle) {
                id: ScoreId,
                title: ScoreTitle? ->
                id to title
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
            { (id: ScoreId, title: ScoreTitle?) ->
                _mappedValidationErrors.update { emptyMap() }
                scoreCoreComponent.updateScoreWith(id, title)
            }
        )
    }
}
