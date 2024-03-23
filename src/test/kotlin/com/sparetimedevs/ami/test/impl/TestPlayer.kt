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
import com.sparetimedevs.ami.player.PlayerSettings

class TestDevice {

    private val allTheThings = mutableStateListOf<String>()

    fun send(message: String): Unit {
        allTheThings.add(message)
    }

    fun notesPlayed(): List<String> = allTheThings.toList()
}

class TestPlayer(settings: PlayerSettings) : Player(settings) {

    private val testDevice: TestDevice = TestDevice()

    override fun playNote(note: Note, onChannelNumber: Int) {
        testDevice.send("Playing $note")
    }

    override fun stopNote(note: Note, onChannelNumber: Int) {
        // No test implementation yet.
    }

    override fun stopEverything() {
        // No test implementation yet.
    }

    fun notesPlayed(): List<String> = testDevice.notesPlayed()
}
