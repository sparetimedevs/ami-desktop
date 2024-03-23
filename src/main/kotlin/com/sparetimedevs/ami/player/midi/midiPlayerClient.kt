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

package com.sparetimedevs.ami.player.midi

import com.sparetimedevs.ami.music.example.getExampleScore
import com.sparetimedevs.ami.player.Metronome
import com.sparetimedevs.ami.player.PlayerSettings
import com.sparetimedevs.ami.player.play
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
        PlayerSettings(
            metronomeMidiChannelNumber,
            scoreMidiChannelNumber,
            metronome,
            true,
        )

    runBlocking {
        val player = MidiPlayer(midiDevice)
        play(score, player, playerSettings)
    }
}
