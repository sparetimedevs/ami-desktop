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

package ui.details

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.details.DefaultMusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsContent
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepositoryImpl
import com.sparetimedevs.ami.getTestComponentContext
import com.sparetimedevs.ami.graphic.GraphicProperties
import io.kotest.core.spec.style.StringSpec

@OptIn(ExperimentalTestApi::class)
class MusicScoreDetailsContentTest :
    StringSpec({
        "The second test with compose should be true" {
            runComposeUiTest {
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
                val musicScoreDetailsComponent: MusicScoreDetailsComponent =
                    DefaultMusicScoreDetailsComponent(testComponentContext, pathDataRepository)

                setContent { MusicScoreDetailsContent(musicScoreDetailsComponent) }

                onNodeWithText("More").assertTextEquals("More")
                onNodeWithText("Load").assertDoesNotExist()
                onRoot().onChild().onChildAt(0).assertHasClickAction()
                onRoot().onChild().onChildAt(0).performClick()
                onNodeWithText("Load").assertExists()
            }
        }
    })
