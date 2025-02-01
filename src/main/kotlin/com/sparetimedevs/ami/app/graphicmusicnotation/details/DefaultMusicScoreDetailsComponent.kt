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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import androidx.compose.ui.graphics.vector.PathNode
import arrow.core.Either
import arrow.core.right
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.sparetimedevs.ami.app.graphicmusicnotation.store.PathDataStore
import com.sparetimedevs.ami.app.graphicmusicnotation.store.ScoreStore
import com.sparetimedevs.ami.app.graphicmusicnotation.vector.asAmiMeasures
import com.sparetimedevs.ami.app.graphicmusicnotation.vector.asPathData
import com.sparetimedevs.ami.app.utils.disposableScope
import com.sparetimedevs.ami.core.DomainError
import com.sparetimedevs.ami.core.asEitherWithAccumulatedValidationErrors
import com.sparetimedevs.ami.music.core.replaceMeasures
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId

internal class DefaultMusicScoreDetailsComponent(
    componentContext: ComponentContext,
    private val scoreStore: ScoreStore,
    private val pathDataStore: PathDataStore,
    private val markInvalidInput: MarkInvalidInput,
) : MusicScoreDetailsComponent,
    ComponentContext by componentContext,
    DisposableScope by componentContext.disposableScope() {
    private val _modeValue = MutableValue(GraphicMusicNotationMode.DRAWING)
    override val modeValue: Value<GraphicMusicNotationMode> = _modeValue

    override val scoreValue: Value<Score> = scoreStore.scoreValue

    override suspend fun changeMode(newValue: GraphicMusicNotationMode) {
        // When changing modes, it makes sense to make sure the score is up-to-date.
        updateAndGetScore()
        _modeValue.update { newValue }
    }

    override fun onMenuClicked() {
        println("now show the menu")
    }

    override fun onNewScoreClicked() {
        println("now show pop up screen to start to create new score")
        scoreStore.saveScore(emptyScore())
        pathDataStore.replacePathData(emptyList())
    }

    override fun onLoadScoreClicked(score: Score) {
        println("now load the score $score")
        scoreStore.saveScore(score)
        val pathData: List<PathNode> =
            if (score.parts.isNotEmpty()) {
                score.parts[0].measures.asPathData(pathDataStore.getGraphicProperties())
            } else {
                emptyList()
            }
        pathDataStore.replacePathData(pathData)
    }

    override fun onSaveScoreClicked() {
        println("now show pop up screen to confirm saving? Or just save")
    }

    override suspend fun updateAndGetScore(): Either<DomainError, Score> =
        when (_modeValue.value) {
            GraphicMusicNotationMode.DRAWING,
            GraphicMusicNotationMode.PLACING,
            ->
                pathDataStore
                    .getPathData()
                    .asAmiMeasures(pathDataStore.getGraphicProperties())
                    .map { measures ->
                        if (measures.isEmpty()) {
                            scoreValue.value
                        } else {
                            scoreStore.saveScore(scoreValue.value.replaceMeasures(measures))
                        }
                    }.mapLeft { validationErrors ->
                        validationErrors.forEach { validationError ->
                            val errorMarkingPathData =
                                markInvalidInput.markInvalidNotesAndMeasuresRed(
                                    validationError.validationIdentifier,
                                    pathDataStore.getGraphicProperties(),
                                    pathDataStore.getPathData(),
                                )
                            pathDataStore.addToErrorMarkingPathData(errorMarkingPathData)
                        }
                        validationErrors
                    }.asEitherWithAccumulatedValidationErrors()
            GraphicMusicNotationMode.READING -> scoreValue.value.right()
        }

    private fun emptyScore(): Score {
        val parts = emptyList<Part>()

        return Score(ScoreId(), null, parts)
    }
}
