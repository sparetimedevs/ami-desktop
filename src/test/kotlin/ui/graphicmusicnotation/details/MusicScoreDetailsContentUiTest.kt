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

package ui.graphicmusicnotation.details

import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.details.DefaultMusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.DefaultScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MarkInvalidInput
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsContent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.ScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.store.PathDataStore
import com.sparetimedevs.ami.app.graphicmusicnotation.store.ScoreStore
import com.sparetimedevs.ami.app.graphicmusicnotation.vector.asPathData
import com.sparetimedevs.ami.getTestComponentContext
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.example.getExampleScoreFrereJacques
import com.sparetimedevs.ami.player.PlayerSettings
import com.sparetimedevs.ami.test.impl.TestPlayer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalTestApi::class)
class MusicScoreDetailsContentUiTest :
    StringSpec({
        val player = TestPlayer(PlayerSettings())

        "Loading an existing score should set PathData" {
            runComposeUiTest {
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
                val musicScoreDetailsComponent: MusicScoreDetailsComponent =
                    DefaultMusicScoreDetailsComponent(
                        testComponentContext,
                        scoreStore,
                        pathDataStore,
                        markInvalidInput,
                    )
                val scoreDetailsComponent: ScoreDetailsComponent =
                    DefaultScoreDetailsComponent(testComponentContext, scoreStore)

                setContent {
                    MusicScoreDetailsContent(
                        musicScoreDetailsComponent,
                        scoreDetailsComponent,
                        player,
                    )
                }

                val initialPathData = pathDataStore.getPathData()
                initialPathData.shouldBeEmpty()

                onNodeWithTag("score-dropdown-menu-load").assertDoesNotExist()
                onNodeWithTag("score-dropdown-menu").performClick()
                onNodeWithTag("score-dropdown-menu-load").assertExists()
                onNodeWithTag("score-dropdown-menu-load").performClick()
                onNodeWithText("Load Frère Jacques").assertExists()
                onNodeWithText("Load Frère Jacques").performClick()

                val resultPathData = pathDataStore.getPathData()

                resultPathData shouldBe
                    getExampleScoreFrereJacques()
                        .parts[0]
                        .measures
                        .asPathData(pathDataStore.getGraphicProperties())
            }
        }

        "Loading an existing score while a score was already in progress should set new PathData" {
            runComposeUiTest {
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
                val pathData: List<PathNode> =
                    PathBuilder().moveTo(x = 87.5f, y = 550.0f).horizontalLineTo(x = 587.5f).nodes
                pathDataStore.replacePathData(pathData)
                val musicScoreDetailsComponent: MusicScoreDetailsComponent =
                    DefaultMusicScoreDetailsComponent(
                        testComponentContext,
                        scoreStore,
                        pathDataStore,
                        markInvalidInput,
                    )
                val scoreDetailsComponent: ScoreDetailsComponent =
                    DefaultScoreDetailsComponent(testComponentContext, scoreStore)

                setContent {
                    MusicScoreDetailsContent(
                        musicScoreDetailsComponent,
                        scoreDetailsComponent,
                        player,
                    )
                }

                val initialPathData = pathDataStore.getPathData()
                initialPathData shouldBe
                    PathBuilder().moveTo(x = 87.5f, y = 550.0f).horizontalLineTo(x = 587.5f).nodes

                onNodeWithTag("score-dropdown-menu-load").assertDoesNotExist()
                onNodeWithTag("score-dropdown-menu").performClick()
                onNodeWithTag("score-dropdown-menu-load").assertExists()
                onNodeWithTag("score-dropdown-menu-load").performClick()
                onNodeWithText("Load Frère Jacques").assertExists()
                onNodeWithText("Load Frère Jacques").performClick()

                val resultPathData = pathDataStore.getPathData()

                resultPathData shouldBe
                    getExampleScoreFrereJacques()
                        .parts[0]
                        .measures
                        .asPathData(pathDataStore.getGraphicProperties())
            }
        }

        "Creating a new score while a score was already in progress should set empty PathData" {
            runComposeUiTest {
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
                val pathData: List<PathNode> =
                    PathBuilder().moveTo(x = 87.5f, y = 550.0f).horizontalLineTo(x = 587.5f).nodes
                pathDataStore.replacePathData(pathData)
                val musicScoreDetailsComponent: MusicScoreDetailsComponent =
                    DefaultMusicScoreDetailsComponent(
                        testComponentContext,
                        scoreStore,
                        pathDataStore,
                        markInvalidInput,
                    )
                val scoreDetailsComponent: ScoreDetailsComponent =
                    DefaultScoreDetailsComponent(testComponentContext, scoreStore)

                setContent {
                    MusicScoreDetailsContent(
                        musicScoreDetailsComponent,
                        scoreDetailsComponent,
                        player,
                    )
                }

                val initialPathData = pathDataStore.getPathData()
                initialPathData shouldBe
                    PathBuilder().moveTo(x = 87.5f, y = 550.0f).horizontalLineTo(x = 587.5f).nodes

                onNodeWithTag("score-dropdown-menu-new").assertDoesNotExist()
                onNodeWithTag("score-dropdown-menu").performClick()
                onNodeWithTag("score-dropdown-menu-new").performClick()
                onNodeWithTag("create-new-score").performClick()

                val resultPathData = pathDataStore.getPathData()

                resultPathData.shouldBeEmpty()
            }
        }
    })
