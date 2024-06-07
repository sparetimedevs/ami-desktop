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

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sparetimedevs.ami.app.graphicmusicnotation.DefaultGraphicMusicNotationMultiPaneComponent
import com.sparetimedevs.ami.app.root.RootComponent

internal class TestPreviewRootComponent : RootComponent {

    private val testComponentContext: ComponentContext = getTestComponentContext()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = Unit,
                instance =
                    RootComponent.Child.GraphicMusicNotationMultiPaneChild(
                        component =
                            DefaultGraphicMusicNotationMultiPaneComponent(
                                componentContext = testComponentContext
                            )
                    )
            )
        )

    override fun onPianoTabClicked() {}

    override fun onGraphicMusicNotationTabClicked() {}
}
