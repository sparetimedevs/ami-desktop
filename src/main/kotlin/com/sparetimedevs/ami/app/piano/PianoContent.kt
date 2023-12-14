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

package com.sparetimedevs.ami.app.piano

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparetimedevs.ami.midi.MidiPlayerSettings
import com.sparetimedevs.ami.midi.player.MidiPlayer
import com.sparetimedevs.ami.midi.player.Player
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteAttributes
import com.sparetimedevs.ami.music.data.kotlin.note.NoteDuration
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch

@Composable
fun PianoContent() {
    val coroutineScope = rememberCoroutineScope()
    val playerSettings = MidiPlayerSettings()
    val player: Player = MidiPlayer(playerSettings, coroutineScope)
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier =
                Modifier.wrapContentSize()
                    .align(Alignment.BottomCenter)
                    .absolutePadding(10.dp, 10.dp, 10.dp, 10.dp)
        ) {
            PianoKeysForOctave(Octave.unsafeCreate(3), player, playerSettings)
            PianoKeysForOctave(Octave.unsafeCreate(4), player, playerSettings)
            PianoKeysForOctave(Octave.unsafeCreate(5), player, playerSettings)
        }
    }
}

@Composable
fun PianoKeysForOctave(octave: Octave, player: Player, playerSettings: MidiPlayerSettings): Unit {
    PianoKey(Pitch(noteName = NoteName.C, octave = octave), player, playerSettings)
    PianoKeyBlack(Pitch(noteName = NoteName.C_SHARP, octave = octave), player, playerSettings)
    PianoKey(Pitch(noteName = NoteName.D, octave = octave), player, playerSettings)
    PianoKeyBlack(Pitch(noteName = NoteName.D_SHARP, octave = octave), player, playerSettings)
    PianoKey(Pitch(noteName = NoteName.E, octave = octave), player, playerSettings)
    PianoKey(Pitch(noteName = NoteName.F, octave = octave), player, playerSettings)
    PianoKeyBlack(Pitch(noteName = NoteName.F_SHARP, octave = octave), player, playerSettings)
    PianoKey(Pitch(noteName = NoteName.G, octave = octave), player, playerSettings)
    PianoKeyBlack(Pitch(noteName = NoteName.G_SHARP, octave = octave), player, playerSettings)
    PianoKey(
        Pitch(noteName = NoteName.A, octave = Octave.unsafeCreate(octave.value.inc())),
        player,
        playerSettings
    )
    PianoKeyBlack(
        Pitch(noteName = NoteName.A_SHARP, octave = Octave.unsafeCreate(octave.value.inc())),
        player,
        playerSettings
    )
    PianoKey(
        Pitch(noteName = NoteName.B, octave = Octave.unsafeCreate(octave.value.inc())),
        player,
        playerSettings
    )
}

@Composable
fun PianoKey(pitch: Pitch, player: Player, playerSettings: MidiPlayerSettings): Unit {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val wrapInNoteUntilWeHaveSomethingBetter =
        Note.Pitched(NoteDuration(NoteValue.MAXIMA), NoteAttributes(null, null, null, null), pitch)
    if (isPressed) {
        println("Pressed : ${pitch.noteName}")
        player.playNote(wrapInNoteUntilWeHaveSomethingBetter, playerSettings.scoreMidiChannelNumber)
        // Use if + DisposableEffect to wait for the press action is completed
        DisposableEffect(Unit) {
            onDispose {
                println("released : ${pitch.noteName}")
                player.stopNote(
                    wrapInNoteUntilWeHaveSomethingBetter,
                    playerSettings.scoreMidiChannelNumber
                )
            }
        }
    }

    Button(
        onClick = {},
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFF0)),
        modifier = Modifier.height(100.dp).width(30.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        pitch.toText()
    }
}

@Composable
fun PianoKeyBlack(pitch: Pitch, player: Player, playerSettings: MidiPlayerSettings): Unit {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val wrapInNoteUntilWeHaveSomethingBetter =
        Note.Pitched(NoteDuration(NoteValue.MAXIMA), NoteAttributes(null, null, null, null), pitch)
    if (isPressed) {
        println("Pressed : ${pitch.noteName}")
        player.playNote(wrapInNoteUntilWeHaveSomethingBetter, playerSettings.scoreMidiChannelNumber)
        // Use if + DisposableEffect to wait for the press action is completed
        DisposableEffect(Unit) {
            onDispose {
                println("released : ${pitch.noteName}")
                player.stopNote(
                    wrapInNoteUntilWeHaveSomethingBetter,
                    playerSettings.scoreMidiChannelNumber
                )
            }
        }
    }

    Button(
        onClick = {},
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        modifier = Modifier.height(100.dp).width(30.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        pitch.toText()
    }
}

@Composable private fun Pitch.toText(): Unit = Text(text = this.noteName.name, fontSize = 3.sp)
