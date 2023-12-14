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

package com.sparetimedevs.ami.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import arrow.core.right
import com.sparetimedevs.ami.music.example.getExampleScoreHeighHoNobodyHome

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ami",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
    ) {
        MaterialTheme {
            //            var displayOptions: OptionsToDisplay by remember {
            // mutableStateOf(OptionFirstScreen) }
            // TODO merge the MidiPlayer into the new app and the new main.
            var displayOptions: OptionsToDisplay by remember { mutableStateOf(OptionPlayer) }
            //            var displayOptions: OptionsToDisplay by remember {
            // mutableStateOf(OptionPiano) }
            WhatToDisplayChoserScreen(displayOptions, { displayOptions })
        }
    }
}

sealed interface OptionsToDisplay

object OptionFirstScreen : OptionsToDisplay

object OptionPlayer : OptionsToDisplay

object OptionPiano : OptionsToDisplay

@Composable
fun WhatToDisplayChoserScreen(
    displayOptions: OptionsToDisplay,
    onItemClick: (displayOptions: OptionsToDisplay) -> Unit
) {
    when (displayOptions) {
        is OptionFirstScreen -> {
            LazyColumn {
                item {
                    Text(
                        text = "Player",
                        modifier =
                            Modifier.clickable {
                                println("helloooooo")
                                onItemClick(OptionPlayer)
                            }
                    )
                }
                item {
                    Text(text = "Piano", modifier = Modifier.clickable { onItemClick(OptionPiano) })
                }
            }
        }
        is OptionPlayer -> PlayerWindow()
        is OptionPiano -> PianoUI()
    }
}

@Composable
fun PlayerWindow() {
    var playerContext by remember { mutableStateOf(PlayerContext()) }
    var deprecatedSettings by remember { mutableStateOf(DeprecatedSettings()) }

    Row(modifier = Modifier.padding(5.dp)) {
        MidiPlayerSidePanel(
            { getExampleScoreHeighHoNobodyHome().right() },
            playerContext,
            deprecatedSettings
        ) { context, deprecated ->
            playerContext = context
            deprecatedSettings = deprecated
        }
        Canvas(Modifier.fillMaxHeight().width(800.dp).background(Color.White)) {
            //                    if(settings.drawOuterOrbit){
            //                        outerOrbit(settings)
            //                    }
            //                    if (settings.drawInnerOrbit) {
            //                        innerOrbit(settings)
            //                    }
        }
    }
}
