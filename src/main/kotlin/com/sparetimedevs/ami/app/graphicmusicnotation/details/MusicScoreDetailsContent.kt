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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sparetimedevs.ami.app.dependencyModule
import com.sparetimedevs.ami.app.metronome.MetronomeButton
import com.sparetimedevs.ami.app.player.PlayPauseMidiPlayerButton
import com.sparetimedevs.ami.player.Player
import com.sparetimedevs.ami.player.PlayerContext

@Composable
internal fun MusicScoreDetailsContent(
    component: MusicScoreDetailsComponent,
    scoreDetailsComponent: ScoreDetailsComponent,
    player: Player = dependencyModule.player,
) {
    val score by component.scoreValue.subscribeAsState()

    val mode by component.modeValue.subscribeAsState()

    val scoreDisplayTitle = score.title?.value ?: score.id.value

    val coroutineScope = rememberCoroutineScope()
    var playerContext by remember {
        mutableStateOf(PlayerContext(playerCoroutineScope = coroutineScope))
    }
    // This remember of playerSettings is needed to trigger recomposition of UI content.
    var playerSettings by remember { mutableStateOf(player.getPlayerSettings()) }

    TopAppBar(
        title = {
            TopAppBarTitleDropDown(
                text = scoreDisplayTitle,
                scoreCoreComponent = component,
                scoreDetailsComponent = scoreDetailsComponent,
            ) { state ->
                playerContext = playerContext.copy(playerState = state)
            }
        },
        actions = {
            PlayPauseMidiPlayerButton({ component.updateAndGetScore() }, playerContext, player) { context ->
                playerContext = context
            }
            MetronomeButton(playerSettings.isMetronomeEnabled) { isMetronomeEnabled ->
                player.setMetronomeEnabled(isMetronomeEnabled)
                playerSettings = player.getPlayerSettings()
            }
            GraphicMusicNotationModeButton(mode, component::changeMode)
            Text(text = "More")
        },
    )
}
