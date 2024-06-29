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

package com.sparetimedevs.ami.app.graphicmusicnotation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sparetimedevs.ami.app.graphicmusicnotation.details.GraphicMusicNotationMode
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsContent
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.DrawGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.DrawGraphicMusicNotationContent
import com.sparetimedevs.ami.app.graphicmusicnotation.place.PlaceGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.place.PlaceGraphicMusicNotationContent
import com.sparetimedevs.ami.app.graphicmusicnotation.read.ReadGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.read.ReadGraphicMusicNotationContent

@Composable
internal fun GraphicMusicNotationMultiPaneContent(
    component: GraphicMusicNotationMultiPaneComponent,
    modifier: Modifier = Modifier
) {
    val children by component.children.subscribeAsState()

    val saveableStateHolder = rememberSaveableStateHolder()

    val topAppBarDetailsPane: @Composable (Child.Created<*, MusicScoreDetailsComponent>) -> Unit =
        remember {
            movableContentOf { (config, component) ->
                saveableStateHolder.SaveableStateProvider(key = config.javaClass.simpleName) {
                    MusicScoreDetailsContent(component = component)
                }
            }
        }

    val drawPane: @Composable (Child.Created<*, DrawGraphicMusicNotationComponent>) -> Unit =
        remember {
            movableContentOf { (config, component) ->
                saveableStateHolder.SaveableStateProvider(key = config.javaClass.simpleName) {
                    DrawGraphicMusicNotationContent(
                        component = component,
                        modifier = modifier.fillMaxSize()
                    )
                }
            }
        }

    val readPane: @Composable (Child.Created<*, ReadGraphicMusicNotationComponent>) -> Unit =
        remember {
            movableContentOf { (config, component) ->
                saveableStateHolder.SaveableStateProvider(key = config.javaClass.simpleName) {
                    ReadGraphicMusicNotationContent(
                        component = component,
                        modifier = modifier.fillMaxSize()
                    )
                }
            }
        }

    val placePane: @Composable (Child.Created<*, PlaceGraphicMusicNotationComponent>) -> Unit =
        remember {
            movableContentOf { (config, component) ->
                saveableStateHolder.SaveableStateProvider(key = config.javaClass.simpleName) {
                    PlaceGraphicMusicNotationContent(
                        component = component,
                        modifier = modifier.fillMaxSize()
                    )
                }
            }
        }

    saveableStateHolder.OldDetailsKeyRemoved(
        selectedDetailsKey = children.topAppBarDetailsChild.configuration.javaClass.simpleName
    )

    val mode by children.topAppBarDetailsChild.instance.modeValue.subscribeAsState()

    Column(modifier = modifier) {
        topAppBarDetailsPane(children.topAppBarDetailsChild)
        when (mode) {
            GraphicMusicNotationMode.DRAWING -> drawPane(children.drawAreaChild)
            GraphicMusicNotationMode.READING -> readPane(children.readAreaChild)
            GraphicMusicNotationMode.PLACING -> placePane(children.placeAreaChild)
        }
    }
}

@Composable
private fun SaveableStateHolder.OldDetailsKeyRemoved(selectedDetailsKey: Any?) {
    var lastDetailsKey by remember { mutableStateOf(selectedDetailsKey) }

    if (selectedDetailsKey == lastDetailsKey) {
        return
    }

    val keyToRemove = lastDetailsKey
    lastDetailsKey = selectedDetailsKey

    if (keyToRemove == null) {
        return
    }

    DisposableEffect(keyToRemove) {
        removeState(key = keyToRemove)
        onDispose {}
    }
}
