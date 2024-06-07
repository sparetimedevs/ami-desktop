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

package com.sparetimedevs.ami.app.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sparetimedevs.ami.app.graphicmusicnotation.GraphicMusicNotationMultiPaneComponent
import com.sparetimedevs.ami.app.piano.PianoComponent

// https://github.com/arkivanov/Decompose/blob/9afbe647a7345e458a7e39c5c198a6e9e19fb9ca/sample/shared/shared/src/commonMain/kotlin/com/arkivanov/sample/shared/root/RootComponent.kt#L10
interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onPianoTabClicked()

    fun onGraphicMusicNotationTabClicked()

    sealed class Child {

        class PianoChild(val component: PianoComponent) : Child()

        class GraphicMusicNotationMultiPaneChild(
            val component: GraphicMusicNotationMultiPaneComponent
        ) : Child()
    }
}
