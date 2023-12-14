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
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId

internal class DefaultMusicScoreDetailsComponent(
    componentContext: ComponentContext,
    private val pathDataRepository: PathDataRepository
) :
    MusicScoreDetailsComponent,
    ComponentContext by componentContext,
    DisposableScope by componentContext.disposableScope() {

    private val _scoreValue = MutableValue(emptyScore())

    override val scoreValue: Value<Score> = _scoreValue

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
        pathDataRepository.replacePathData(
            score.parts[0].measures.asPathData(pathDataRepository.getGraphicProperties())
        )
    }

    override fun onSaveScoreClicked() {
        println("now show pop up screen to confirm saving? Or just save")
    }

    override suspend fun getUpdatedScoreAccordingToCurrentPathData(): Either<DomainError, Score> =
        pathDataRepository
            .getPathData()
            .asAmiMeasures(pathDataRepository.getGraphicProperties())
            .map { measures -> replaceMeasuresInScore(measures) }
            .asEitherWithAccumulatedValidationErrors()

    private fun replaceMeasuresInScore(measures: List<Measure>): Score {
        val parts = listOf(Part(PartId(), measures))

        return _scoreValue.updateAndGet { it.copy(parts = parts) }
    }

    private fun emptyScore(): Score {
        val parts = emptyList<Part>()

        return Score(ScoreId(), null, parts)
    }
}
