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

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.webhistory.WebHistoryController
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.sparetimedevs.ami.app.graphicmusicnotation.DefaultGraphicMusicNotationMultiPaneComponent
import com.sparetimedevs.ami.app.piano.DefaultPianoComponent
import com.sparetimedevs.ami.app.root.RootComponent.Child

// https://github.com/arkivanov/Decompose/blob/9afbe647a7345e458a7e39c5c198a6e9e19fb9ca/sample/shared/shared/src/commonMain/kotlin/com/arkivanov/sample/shared/root/DefaultRootComponent.kt#L25
@OptIn(ExperimentalDecomposeApi::class)
class DefaultRootComponent(
    componentContext: ComponentContext,
    deepLink: DeepLink = DeepLink.None,
    webHistoryController: WebHistoryController? = null,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val stack =
        childStack(
            source = navigation,
            initialStack = { getInitialStack(deepLink) },
            childFactory = ::child,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    init {
        webHistoryController?.attach(
            navigator = navigation,
            stack = stack,
            getPath = ::getPathForConfig,
            getConfiguration = ::getConfigForPath,
        )
    }

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Piano -> Child.PianoChild(DefaultPianoComponent(componentContext))
            is Config.GraphicMusicNotationMultiPane ->
                Child.GraphicMusicNotationMultiPaneChild(
                    DefaultGraphicMusicNotationMultiPaneComponent(componentContext)
                )
        }

    override fun onPianoTabClicked() {
        navigation.bringToFront(Config.Piano)
    }

    override fun onGraphicMusicNotationTabClicked() {
        navigation.bringToFront(Config.GraphicMusicNotationMultiPane)
    }

    private companion object {
        private const val WEB_PATH_PIANO = "piano"
        private const val WEB_PATH_GRAPHIC_MUSIC_NOTATION = "graphic-music-notation"

        private fun getInitialStack(deepLink: DeepLink): List<Config> =
            when (deepLink) {
                is DeepLink.None -> listOf(Config.Piano)
                is DeepLink.Web -> listOf(getConfigForPath(deepLink.path))
            }

        private fun getPathForConfig(config: Config): String =
            when (config) {
                Config.Piano -> "/$WEB_PATH_PIANO"
                Config.GraphicMusicNotationMultiPane -> "/$WEB_PATH_GRAPHIC_MUSIC_NOTATION"
            }

        private fun getConfigForPath(path: String): Config =
            when (path.removePrefix("/")) {
                WEB_PATH_PIANO -> Config.Piano
                WEB_PATH_GRAPHIC_MUSIC_NOTATION -> Config.GraphicMusicNotationMultiPane
                else -> Config.Piano
            }
    }

    private sealed interface Config : Parcelable {

        @Parcelize
        object Piano : Config {
            /**
             * Only required for state preservation on JVM/desktop via StateKeeper, as it uses
             * Serializable. Temporary workaround for https://youtrack.jetbrains.com/issue/KT-40218.
             */
            @Suppress("unused") private fun readResolve(): Any = Piano
        }

        @Parcelize
        object GraphicMusicNotationMultiPane : Config {
            /**
             * Only required for state preservation on JVM/desktop via StateKeeper, as it uses
             * Serializable. Temporary workaround for https://youtrack.jetbrains.com/issue/KT-40218.
             */
            @Suppress("unused") private fun readResolve(): Any = GraphicMusicNotationMultiPane
        }
    }

    sealed interface DeepLink {
        object None : DeepLink

        class Web(val path: String) : DeepLink
    }
}
