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

package com.sparetimedevs.ami.midi

import com.sparetimedevs.ami.midi.player.MidiPlayer
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.example.getExampleScore
import java.time.LocalDateTime
import javax.sound.midi.MidiDevice
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

fun main() {

    // On MacOS, this is the name I gave to the "Device Name" property in "MIDI Studio" in "Audio
    // MIDI Setup"
    // This is also the MIDI device I've selected as MIDI input in Surge XT.
    val midiDeviceDescription = "MidiOnMac"

    //    val midiDevice = openMidiDevice(midiDeviceDescription)
    val midiDevice = openMidiDevice() // Use this if you want to use the default synthesizer.

    val beats = 4

    val score = getExampleScore()
    val metronome = Metronome(beatsPerBar = beats, bpm = 120)

    val metronomeMidiChannelNumber = 0
    val scoreMidiChannelNumber = 1

    val playerSettings =
        MidiPlayerSettings(
            midiDevice,
            metronomeMidiChannelNumber,
            scoreMidiChannelNumber,
            metronome,
            true,
        )

    runBlocking { play(score, playerSettings) }
}

data class MidiPlayerSettings(
    val midiDevice: MidiDevice = openMidiDevice(),
    val metronomeMidiChannelNumber: Int = 0,
    val scoreMidiChannelNumber: Int = 1,
    val metronome: Metronome = Metronome(beatsPerBar = 4, bpm = 120),
    val isMetronomeEnabled: Boolean = false
)

suspend fun play(score: Score, playerSettings: MidiPlayerSettings): Unit = coroutineScope {
    val player = MidiPlayer(playerSettings, this)

    try {
        // score.parts[0] can throw `java.lang.IndexOutOfBoundsException: Empty list doesn't contain
        // element at index 0`
        // It would be good to avoid it.
        player.play(score.parts[0].measures, LocalDateTime.now())
        while (this.isActive) {
            // Do nothing, just keep going while the coroutine is active.
            // Do add a delay, else this while loo[ keeps on going too fast.
            delay(500L)
        }
    } finally {
        println("Stopping player...")
        runBlocking {
            player.stop()
            playerSettings.midiDevice.close()
        }
    }
}
