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

import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch
import com.sparetimedevs.ami.player.Metronome
import com.sparetimedevs.ami.player.Player
import com.sparetimedevs.ami.player.PlayerSettings
import com.sparetimedevs.ami.player.midi.message.AllSoundOffMidiMessage
import java.time.Duration
import javax.sound.midi.MidiDevice
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage

class MidiPlayer(midiDevice: MidiDevice, playerSettings: PlayerSettings) : Player(playerSettings) {

    private val receiver: Receiver = midiDevice.receiver

    override fun playNote(note: Note, onChannelNumber: Int) {
        println("Playing $note on thread ${Thread.currentThread().name}")
        val midinote = helperFunForPitchOfNoteToMidiNoteValue(note)
        //        val midiVel = (127f * note.amp).toInt() // TODO we don't have a volume attribute
        // yet (or any indication of how loud a note should be played).
        val midiVel = (127f * 0.5f).toInt()
        val noteOnMsg = ShortMessage(ShortMessage.NOTE_ON, onChannelNumber, midinote, midiVel)
        receiver.send(noteOnMsg, -1)
    }

    override fun stopNote(note: Note, onChannelNumber: Int) {
        val midinote = helperFunForPitchOfNoteToMidiNoteValue(note)
        //        val midiVel = (127f * note.amp).toInt() // TODO we don't have a volume attribute
        // yet (or any indication of how loud a note should be played).
        val midiVel = (127f * 0.5f).toInt()
        // val noteOffAt = playAt.plus((note.duration * metronome.millisPerBeat).toLong(),
        // ChronoUnit.MILLIS)

        val noteOffMsg = ShortMessage(ShortMessage.NOTE_OFF, onChannelNumber, midinote, midiVel)
        receiver.send(noteOffMsg, -1)
    }

    override fun stopEverything() {
        val allSoundOffOnChannel1MidiMessage = AllSoundOffMidiMessage(1)
        receiver.send(allSoundOffOnChannel1MidiMessage, -1)
    }
}

fun helperFunForPitchOfNoteToMidiNoteValue(note: Note): Int =
    when (note) {
        is Note.Pitched -> helperFunForPitchToMidiNoteValue(note.pitch)
        else -> 0
    }

fun helperFunForPitchToMidiNoteValue(pitch: Pitch): Int {
    // TODO should also take into consideration the pitch.alter
    val octaveInByte: Byte = pitch.octave.value
    val y =
        when (octaveInByte) {
            0.toByte() -> 8
            1.toByte() -> 20
            2.toByte() -> 32
            3.toByte() -> 44
            4.toByte() -> 56
            5.toByte() -> 68
            6.toByte() -> 80
            7.toByte() -> 92
            8.toByte() -> 104
            9.toByte() -> 116
            10.toByte() -> 128
            11.toByte() -> 130
            12.toByte() -> 142
            else -> 56 // Default to octave 4
        }
    // The limit is the 128 notes Midi supports.
    return y + helperFunForPitchToMidiNoteValue(pitch.noteName) -
        56 // TODO this - 56 should be refactored away (deduct it from `NoteName.A_FLAT -> 56` etc.)
}

fun helperFunForPitchToMidiNoteValue(noteName: NoteName): Int =
    when (noteName) {
        NoteName.A_FLAT -> 56
        NoteName.A -> 57
        NoteName.A_SHARP -> 58
        NoteName.B_FLAT -> 58
        NoteName.B -> 59
        NoteName.C -> 60
        NoteName.C_SHARP -> 61
        NoteName.D_FLAT -> 61
        NoteName.D -> 62
        NoteName.D_SHARP -> 63
        NoteName.E_FLAT -> 63
        NoteName.E -> 64
        NoteName.F -> 65
        NoteName.F_SHARP -> 66
        NoteName.G_FLAT -> 66
        NoteName.G -> 67
        NoteName.G_SHARP -> 68
    }

fun helperFunForDurationOfNoteToJavaTimeDuration(note: Note, metronome: Metronome): Duration {
    // For 4/4 time signature
    val millis = (note.duration.value * 4 * metronome.millisPerBeat).toLong()

    return Duration.ofMillis(millis)
}
