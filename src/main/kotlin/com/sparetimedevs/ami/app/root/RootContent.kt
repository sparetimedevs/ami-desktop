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

package com.sparetimedevs.ami.app.root

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.isEnter
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.ParcelableContainer
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.sparetimedevs.ami.app.graphicmusicnotation.DefaultGraphicMusicNotationMultiPaneComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.GraphicMusicNotationMultiPaneComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.GraphicMusicNotationMultiPaneContent
import com.sparetimedevs.ami.app.icon.AmiDesktopAppIcons
import com.sparetimedevs.ami.app.icon.Pencil
import com.sparetimedevs.ami.app.icon.Piano
import com.sparetimedevs.ami.app.piano.PianoComponent
import com.sparetimedevs.ami.app.piano.PianoContent
import com.sparetimedevs.ami.app.root.RootComponent.Child
import com.sparetimedevs.ami.app.root.RootComponent.Child.GraphicMusicNotationMultiPaneChild
import com.sparetimedevs.ami.app.root.RootComponent.Child.PianoChild

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Column(modifier = modifier) {
        Children(
            stack = childStack,
            modifier = Modifier.weight(weight = 1F),
            animation = tabAnimation(),
        ) {
            when (val child = it.instance) {
                is PianoChild -> PianoContent()
                is GraphicMusicNotationMultiPaneChild ->
                    GraphicMusicNotationMultiPaneContent(
                        component = child.component,
                        modifier = modifier.fillMaxSize(),
                    )
            }
        }

        BottomNavigation(modifier = Modifier.fillMaxWidth()) {
            BottomNavigationItem(
                selected = activeComponent is PianoComponent,
                onClick = component::onPianoTabClicked,
                icon = {
                    Icon(
                        imageVector = AmiDesktopAppIcons.Piano,
                        contentDescription = "Piano",
                    )
                },
            )

            BottomNavigationItem(
                selected = activeComponent is GraphicMusicNotationMultiPaneComponent,
                onClick = component::onGraphicMusicNotationTabClicked,
                icon = {
                    Icon(
                        imageVector = AmiDesktopAppIcons.Pencil,
                        contentDescription = "Graphic music notation",
                    )
                },
            )
        }
    }
}

@OptIn(com.arkivanov.decompose.FaultyDecomposeApi::class)
@Composable
private fun tabAnimation(): StackAnimation<Any, Child> =
    stackAnimation { child, otherChild, direction ->
        val index = child.instance.index
        val otherIndex = otherChild.instance.index
        val anim = slide()
        if ((index > otherIndex) == direction.isEnter) anim else anim.flipSide()
    }

private val Child.index: Int
    get() =
        when (this) {
            is PianoChild -> 4
            is GraphicMusicNotationMultiPaneChild -> 5
        }

private fun StackAnimator.flipSide(): StackAnimator =
    StackAnimator { direction, isInitial, onFinished, content ->
        invoke(
            direction = direction.flipSide(),
            isInitial = isInitial,
            onFinished = onFinished,
            content = content,
        )
    }

@Suppress("OPT_IN_USAGE")
private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }

@Preview
@Composable
internal fun RootContentPreview() {
    RootContent(PreviewRootComponent())
}

internal class PreviewRootComponent : RootComponent {
    val lifecycle = LifecycleRegistry()
    val stateKeeper = StateKeeperDispatcher(tryRestoreStateFromFile())

    private fun tryRestoreStateFromFile(): ParcelableContainer? = null

    override val childStack: Value<ChildStack<*, Child>> =
        MutableValue(
            ChildStack(
                configuration = Unit,
                instance =
                    GraphicMusicNotationMultiPaneChild(
                        component =
                            DefaultGraphicMusicNotationMultiPaneComponent(
                                componentContext =
                                    DefaultComponentContext(
                                        lifecycle = lifecycle,
                                        stateKeeper = stateKeeper,
                                    ),
                            ),
                    ),
            ),
        )

    override fun onPianoTabClicked() {}

    override fun onGraphicMusicNotationTabClicked() {}
}
