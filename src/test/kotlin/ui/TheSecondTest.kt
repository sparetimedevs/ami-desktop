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

package ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalTestApi::class)
class TheSecondTest :
    StringSpec({
        "The second test with compose should be true" {
            runComposeUiTest {
                // Declares a mock UI to demonstrate API calls
                //
                // Replace with your own declarations to test the code of your project
                setContent {
                    var text by remember { mutableStateOf("Hello World!") }
                    Text(text = text, modifier = Modifier.testTag("text"))
                    Button(
                        onClick = { text = "Hello Compose World!" },
                        modifier = Modifier.testTag("button"),
                    ) {
                        Text("Click me")
                    }
                }

                // Tests the declared UI with assertions and actions of the Compose Multiplatform
                // testing API
                onNodeWithTag("text").assertTextEquals("Hello World!")
                onNodeWithTag("button").performClick()
                onNodeWithTag("text").assertTextEquals("Hello Compose World!")
            }

            "true" shouldBe "true"
        }
    })
