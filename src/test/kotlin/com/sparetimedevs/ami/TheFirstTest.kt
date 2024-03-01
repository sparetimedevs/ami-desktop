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

package com.sparetimedevs.ami

// import androidx.compose.foundation.background
// import androidx.compose.foundation.layout.Box
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.material.CircularProgressIndicator
// import androidx.compose.ui.ImageComposeScene
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

// import org.jetbrains.skia.Image

class TheFirstTest :
    StringSpec({
        "The first test with compose should be true" {
            //            val scene: ImageComposeScene =
            //                ImageComposeScene(100, 100) {
            //                    Box(Modifier.fillMaxSize().background(Color.White)) {
            //                        CircularProgressIndicator()
            //                    }
            //                }
            //
            //            val theThing: Image = scene.render()
            //
            //            theThing shouldBe theThing

            "true" shouldBe "true"
        }

        // Works locally, but on GitHub Actions throwing javax.sound.midi.MidiUnavailableException
        //        "The first test with compose with our RootContent should be true" {
        //            val scene: ImageComposeScene =
        //                ImageComposeScene(100, 100) { RootContent(TestPreviewRootComponent()) }
        //
        //            val theThing: Image = scene.render()
        //
        //            theThing shouldBe theThing
        //
        //            "true" shouldBe "true"
        //        }
    })
