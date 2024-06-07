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

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiSystem
import javax.sound.midi.Synthesizer

fun openMidiDevice(midiDeviceDescription: String? = null): MidiDevice =
    midiDeviceDescription?.let { findMidiDevice(it) } ?: getDefaultSynthesizer()

private fun findMidiDevice(desc: String): MidiDevice? =
    MidiSystem.getMidiDeviceInfo()
        .toList()
        .map { MidiSystem.getMidiDevice(it) }
        .first { it.deviceInfo.description.startsWith(desc) }
        .apply { open() }

private fun getDefaultSynthesizer(): Synthesizer {
    val defaultSynthesizer: Synthesizer = MidiSystem.getSynthesizer()

    defaultSynthesizer.open()

    val metronomeMidiInstr =
        116 // Woodblock (maybe want to change this into drums? Or dynamically configurable)
    val midiInstr = 30 // Overdriven Guitar

    val metronomeInstrument = defaultSynthesizer.availableInstruments[metronomeMidiInstr]
    defaultSynthesizer.loadInstrument(metronomeInstrument)

    val currentInstrument = defaultSynthesizer.availableInstruments[midiInstr]
    defaultSynthesizer.loadInstrument(currentInstrument)
    // Metronome channel
    defaultSynthesizer.channels[0].programChange(
        metronomeInstrument.patch.bank,
        metronomeInstrument.patch.program
    )
    // Score channel
    defaultSynthesizer.channels[1].programChange(
        currentInstrument.patch.bank,
        currentInstrument.patch.program
    )

    return defaultSynthesizer
}
