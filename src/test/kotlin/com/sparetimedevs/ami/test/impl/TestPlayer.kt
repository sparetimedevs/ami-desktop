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

package com.sparetimedevs.ami.test.impl

import androidx.compose.runtime.mutableStateListOf
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.player.Player

class TestDevice(val testString: String) {

    private val allTheThings = mutableStateListOf<String>()

    fun send(message: String): Unit {
        allTheThings.add(message)
    }

    fun notesPlayed(): List<String> = allTheThings.toList()
}

class TestPlayer : Player() {

    private val testDevice: TestDevice = TestDevice("test device")

    override fun playNote(note: Note, onChannelNumber: Int) {
        println("Playing $note on thread ${Thread.currentThread().name}")
        testDevice.send("Playing $note")
        //        val midinote = helperFunForPitchOfNoteToMidiNoteValue(note)
        //        //        val midiVel = (127f * note.amp).toInt() // TODO we don't have a volume
        // attribute
        //        // yet (or any indication of how loud a note should be played).
        //        val midiVel = (127f * 0.5f).toInt()
        //        val noteOnMsg = ShortMessage(ShortMessage.NOTE_ON, onChannelNumber, midinote,
        // midiVel)
        //        testDevice.send(noteOnMsg, -1)
    }

    override fun stopNote(note: Note, onChannelNumber: Int) {
        //        val midinote = helperFunForPitchOfNoteToMidiNoteValue(note)
        //        //        val midiVel = (127f * note.amp).toInt() // TODO we don't have a volume
        // attribute
        //        // yet (or any indication of how loud a note should be played).
        //        val midiVel = (127f * 0.5f).toInt()
        //        // val noteOffAt = playAt.plus((note.duration * metronome.millisPerBeat).toLong(),
        //        // ChronoUnit.MILLIS)
        //
        //        val noteOffMsg = ShortMessage(ShortMessage.NOTE_OFF, onMidiChannelNumber,
        // midinote, midiVel)
        //        receiver.send(noteOffMsg, -1)
    }

    override fun stopEverything() {
        TODO("Not yet implemented")
    }

    fun notesPlayed(): List<String> = testDevice.notesPlayed()
}
