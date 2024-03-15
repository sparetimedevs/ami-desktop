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

package com.sparetimedevs.ami.app.player

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.sparetimedevs.ami.core.DomainError
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.player.PlayerContext
import com.sparetimedevs.ami.player.midi.MidiPlayer
import com.sparetimedevs.ami.player.midi.openMidiDevice
import com.sparetimedevs.ami.player.play
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

@Preview
@Composable
fun PlayPauseMidiPlayerButton(
    getScore: suspend () -> Either<DomainError, Score>,
    playerContext: PlayerContext,
    onValueChange: (PlayerContext) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    var domainError: DomainError? by remember { mutableStateOf(null) }

    TextButton(
        modifier = Modifier.padding(10.dp).testTag("play-pause-midi-player-button"),
        onClick = {
            when (playerContext.playerState) {
                PlayerState.PAUSED,
                PlayerState.PAUSE ->
                    onValueChange(playerContext.copy(playerState = PlayerState.PLAY))
                PlayerState.PLAYING,
                PlayerState.PLAY ->
                    onValueChange(playerContext.copy(playerState = PlayerState.PAUSE))
            }
        },
        content = {
            when (playerContext.playerState) {
                PlayerState.PAUSED,
                PlayerState.PAUSE -> Text(text = "Play")
                PlayerState.PLAYING,
                PlayerState.PLAY -> Text(text = "Pause")
            }
        },
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.secondary),
        elevation = ButtonDefaults.elevation()
    )

    when (playerContext.playerState) {
        PlayerState.PLAY -> {
            // Open MIDI device
            val midiDevice = openMidiDevice()
            // Start playing midi
            val job: Job =
                coroutineScope.launch {
                    getScore()
                        .fold(
                            { error ->
                                println("Error: $error")
                                domainError = error
                            },
                            { score ->
                                val player =
                                    MidiPlayer(playerContext.playerSettings, midiDevice, this)
                                play(score, player) { midiDevice.close() }
                            }
                        )
                }
            onValueChange(playerContext.copy(playerJob = job, playerState = PlayerState.PLAYING))
        }
        PlayerState.PAUSE -> {
            // Stop playing midi
            coroutineScope.launch { playerContext.playerJob.cancelAndJoin() }
            onValueChange(playerContext.copy(playerState = PlayerState.PAUSED))
        }
        PlayerState.PLAYING,
        PlayerState.PAUSED -> Unit // Do nothing new when playing or paused.
    }

    if (domainError != null) {
        AlertDialog(
            onDismissRequest = {
                domainError = null
                onValueChange(playerContext.copy(playerState = PlayerState.PAUSE))
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            domainError = null
                            onValueChange(playerContext.copy(playerState = PlayerState.PAUSE))
                        }
                    ) {
                        Text("Ok")
                    }
                }
            },
            title = { Text(text = "An error occurred while trying to play the score.") },
            text = {
                Text(
                    text =
                        "You need to resolve the error before you can play the score. The error is: $domainError"
                )
            },
            modifier = Modifier.width(400.dp)
        )
    }
}

enum class PlayerState {
    PLAY,
    PLAYING,
    PAUSE,
    PAUSED
}
