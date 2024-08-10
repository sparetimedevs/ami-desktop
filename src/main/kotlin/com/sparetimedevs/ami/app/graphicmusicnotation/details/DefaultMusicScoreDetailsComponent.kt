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
import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.right
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.decompose.value.updateAndGet
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepository
import com.sparetimedevs.ami.app.graphicmusicnotation.vector.asAmiMeasures
import com.sparetimedevs.ami.app.graphicmusicnotation.vector.asPathData
import com.sparetimedevs.ami.app.utils.disposableScope
import com.sparetimedevs.ami.core.DomainError
import com.sparetimedevs.ami.core.asEitherWithAccumulatedValidationErrors
import com.sparetimedevs.ami.core.validation.MeasureIndex
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationErrorFor
import com.sparetimedevs.ami.core.validation.ValidationErrorForMeasure
import com.sparetimedevs.ami.core.validation.ValidationErrorForNote
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationIdentifierForMeasure
import com.sparetimedevs.ami.core.validation.ValidationIdentifierForNote
import com.sparetimedevs.ami.music.core.replaceMeasures
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreTitle

const val Toggle = true

internal class DefaultMusicScoreDetailsComponent(
    componentContext: ComponentContext,
    private val pathDataRepository: PathDataRepository
) :
    MusicScoreDetailsComponent,
    ComponentContext by componentContext,
    DisposableScope by componentContext.disposableScope() {

    private val _modeValue = MutableValue(GraphicMusicNotationMode.DRAWING)
    override val modeValue: Value<GraphicMusicNotationMode> = _modeValue

    private val _scoreValue = MutableValue(emptyScore())
    override val scoreValue: Value<Score> = _scoreValue

    override suspend fun changeMode(newValue: GraphicMusicNotationMode): Unit {
        // When changing modes, it makes sense to make sure the score is up-to-date.
        updateAndGetScore()
        _modeValue.update { newValue }
    }

    override fun onMenuClicked() {
        println("now show the menu")
    }

    override fun onNewScoreClicked() {
        println("now show pop up screen to start to create new score")
        _scoreValue.update { emptyScore() }
        pathDataRepository.replacePathData(emptyList())
    }

    override fun onLoadScoreClicked(score: Score) {
        println("now load the score $score")
        _scoreValue.update { score }
        val pathData: List<PathNode> =
            if (score.parts.isNotEmpty()) {
                score.parts[0].measures.asPathData(pathDataRepository.getGraphicProperties())
            } else {
                emptyList()
            }
        pathDataRepository.replacePathData(pathData)
    }

    override fun onSaveScoreClicked() {
        println("now show pop up screen to confirm saving? Or just save")
    }

    override suspend fun updateAndGetScore(): Either<DomainError, Score> =
        when (_modeValue.value) {
            GraphicMusicNotationMode.DRAWING,
            GraphicMusicNotationMode.PLACING ->
                pathDataRepository
                    .getPathData()
                    .asAmiMeasures(pathDataRepository.getGraphicProperties())
                    .map { measures ->
                        if (measures.isEmpty()) {
                            _scoreValue.value
                        } else {
                            _scoreValue.updateAndGet { it.replaceMeasures(measures) }
                        }
                    }
                    .mapLeft { validationErrors: NonEmptyList<ValidationError>
                        -> /* TODO use this to mark offending things red. */
                        validationErrors.forEach { validationError: ValidationError ->
                            if (Toggle) {
                                doTheThingToMarkItRedFor(validationError.validationIdentifier)
                            } else {
                                doTheOtherThingToMarkItRedFor(validationError.validationErrorFor)
                            }
                        }
                        validationErrors
                    }
                    .asEitherWithAccumulatedValidationErrors()
            GraphicMusicNotationMode.READING -> _scoreValue.value.right()
        }

    override fun updateScoreWith(id: ScoreId, title: ScoreTitle?) {
        println("In the core, the scoreId is ${id.value}")
        println("In the core, the scoreTitle is ${title?.value}")
        //TODO updateTheScore!
    }

    private fun doTheThingToMarkItRedFor(validationIdentifier: ValidationIdentifier): Unit {
        when (validationIdentifier) {
            is ValidationIdentifierForNote -> doTheThingToMarkNoteRed(validationIdentifier)
            is ValidationIdentifierForMeasure -> doTheThingToMarkMeasureRed(validationIdentifier)
        }
    }

    private fun doTheThingToMarkNoteRed(
        validationIdentifierForNote: ValidationIdentifierForNote
    ): Unit {
        val validationIdentifierForMeasure =
            returnFirstScoreValidationIdentifier(
                validationIdentifierForNote.validationIdentifierParent
            )
        val measureIndex = validationIdentifierForMeasure.measureIndex
        val noteIndex = validationIdentifierForNote.noteIndex
        pathDataRepository.getPathData()
        // TODO do some calculations to get the right note from the right measure.
        doTheThingToMarkItRedFor(validationIdentifierForNote.validationIdentifierParent)
    }

    private tailrec fun returnFirstScoreValidationIdentifier(
        validationIdentifier: ValidationIdentifier
    ): ValidationIdentifierForMeasure =
        if (validationIdentifier is ValidationIdentifierForMeasure) validationIdentifier
        else {
            returnFirstScoreValidationIdentifier(validationIdentifier.validationIdentifierParent)
        }
    // TODO what happens if there is no ValidationIdentifierForMeasure in the hierarchy? Write unit
    // test for it.

    private fun doTheThingToMarkMeasureRed(
        validationIdentifierForMeasure: ValidationIdentifierForMeasure
    ): Unit {
        val xStart = validationIdentifierForMeasure.measureIndex * 575.0f + 87.5f

        val pathData: List<PathNode> =
            PathBuilder().moveTo(x = xStart, y = 400.0f).horizontalLineTo(x = xStart + 500).nodes
        pathDataRepository.addToErrorMarkingPathData(
            pathData
        ) // TODO just start with a line on top. Later on we can extend it to a complete border
        // around a measure.
    }

    private fun doTheOtherThingToMarkItRedFor(validationErrorFor: ValidationErrorFor?): Unit {
        when (validationErrorFor) {
            is ValidationErrorForNote -> doTheOtherThingToMarkNoteRed(validationErrorFor)
            is ValidationErrorForMeasure -> doTheOtherThingToMarkMeasureRed(validationErrorFor)
        }
    }

    private fun doTheOtherThingToMarkNoteRed(validationErrorForNote: ValidationErrorForNote): Unit {
        val measureIndex = validationErrorForNote.measureIndex
        val noteIndex = validationErrorForNote.noteIndex
        pathDataRepository.getPathData()
        // TODO do some calculations to get the right note from the right measure.

        // And also mark the measure red

        if (measureIndex != null) doTheOtherThingToMarkMeasureRed(measureIndex)
    }

    private fun doTheOtherThingToMarkMeasureRed(
        validationErrorForMeasure: ValidationErrorForMeasure
    ): Unit {
        val maybeMeasureIndex = validationErrorForMeasure.measureIndex
        if (maybeMeasureIndex != null) doTheOtherThingToMarkMeasureRed(maybeMeasureIndex)
    }

    private fun doTheOtherThingToMarkMeasureRed(measureIndex: MeasureIndex): Unit {
        val xStart = measureIndex.value * 575.0f + 87.5f

        val pathData: List<PathNode> =
            PathBuilder().moveTo(x = xStart, y = 400.0f).horizontalLineTo(x = xStart + 500).nodes
        pathDataRepository.addToErrorMarkingPathData(
            pathData
        ) // TODO just start with a line on top. Later on we can extend it to a complete border
        // around a measure.
    }

    private fun emptyScore(): Score {
        val parts = emptyList<Part>()

        return Score(ScoreId(), null, parts)
    }
}
