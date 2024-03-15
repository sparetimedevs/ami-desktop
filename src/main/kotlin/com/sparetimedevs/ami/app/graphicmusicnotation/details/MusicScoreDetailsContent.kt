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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sparetimedevs.ami.app.metronome.MetronomeButton
import com.sparetimedevs.ami.app.player.PlayPauseMidiPlayerButton
import com.sparetimedevs.ami.player.PlayerContext
import com.sparetimedevs.ami.player.PlayerSettings

@Composable
internal fun MusicScoreDetailsContent(component: MusicScoreDetailsComponent) {

    val score by component.scoreValue.subscribeAsState()

    val mode by component.modeValue.subscribeAsState()

    val scoreDisplayTitle = score.title?.value ?: score.id.value

    var playerContext by remember {
        mutableStateOf(PlayerContext(playerSettings = PlayerSettings()))
    }

    TopAppBar(
        title = {
            TopAppBarTitleDropDown(text = scoreDisplayTitle, component = component) { state ->
                playerContext = playerContext.copy(playerState = state)
            }
        },
        actions = {
            PlayPauseMidiPlayerButton({ component.updateAndGetScore() }, playerContext) { context ->
                playerContext = context
            }
            MetronomeButton(playerContext.playerSettings.isMetronomeEnabled) { isMetronomeEnabled ->
                playerContext =
                    playerContext.copy(
                        playerSettings =
                            playerContext.playerSettings.copy(
                                isMetronomeEnabled = isMetronomeEnabled
                            )
                    )
            }
            GraphicMusicNotationModeButton(mode, component::changeMode)
            Text(text = "More")
        }
    )
}
