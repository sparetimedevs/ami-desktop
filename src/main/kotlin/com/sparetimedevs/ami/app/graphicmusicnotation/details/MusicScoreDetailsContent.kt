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
import com.sparetimedevs.ami.app.PlayerContext
import com.sparetimedevs.ami.app.player.PlayPauseMidiPlayerButton

@Composable
internal fun MusicScoreDetailsContent(component: MusicScoreDetailsComponent) {

    val score by component.scoreValue.subscribeAsState()

    val scoreDisplayTitle = score.title?.value ?: score.id.value

    var playerContext by remember { mutableStateOf(PlayerContext()) }

    TopAppBar(
        title = {
            TopAppBarTitleDropDown(text = scoreDisplayTitle, component = component) { state ->
                playerContext = playerContext.copy(playerState = state)
            }
        },
        actions = {
            PlayPauseMidiPlayerButton(
                { component.getUpdatedScoreAccordingToCurrentPathData() },
                playerContext
            ) { context ->
                playerContext = context
            }
            Text(text = "More")
        }
    )
}
