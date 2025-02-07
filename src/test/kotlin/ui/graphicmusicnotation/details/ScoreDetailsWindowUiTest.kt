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

package ui.graphicmusicnotation.details

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.details.DefaultScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.ScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.ScoreDetailsWindow
import com.sparetimedevs.ami.app.graphicmusicnotation.store.ScoreStore
import com.sparetimedevs.ami.getTestComponentContext
import io.kotest.core.spec.style.StringSpec

@OptIn(ExperimentalTestApi::class)
class ScoreDetailsWindowUiTest :
    StringSpec({

        "should work" {
            runComposeUiTest {
                val testComponentContext: ComponentContext = getTestComponentContext()
                val scoreStore = ScoreStore()
                val scoreDetailsComponent: ScoreDetailsComponent =
                    DefaultScoreDetailsComponent(testComponentContext, scoreStore)
                var selectedIndex: Int = -1

                setContent {
                    ScoreDetailsWindow(
                        scoreDetailsComponent,
                        com.sparetimedevs.ami.core.util
                            .randomUuidString(),
                    ) { newSelectedIndex ->
                        selectedIndex = newSelectedIndex
                    }
                }

                onNodeWithTag("score-details-window").assertExists()
                onNodeWithTag("score-dropdown-menu-load").assertDoesNotExist()
//                onNodeWithTag("score-dropdown-menu").performClick()
//                onNodeWithTag("score-dropdown-menu-load").assertExists()
//                onNodeWithTag("score-dropdown-menu-load").performClick()
//                onNodeWithText("Load Frère Jacques").assertExists()
//                onNodeWithText("Load Frère Jacques").performClick()
            }
        }
    })
