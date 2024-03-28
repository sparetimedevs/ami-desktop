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

package com.sparetimedevs.ami.player

import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteAttributes
import com.sparetimedevs.ami.music.data.kotlin.note.NoteDuration
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch
import com.sparetimedevs.ami.player.midi.helperFunForDurationOfNoteToJavaTimeDuration
import java.time.Duration
import java.time.LocalDateTime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class Player(playerSettings: PlayerSettings) {

    private var playerSettings: PlayerSettings

    init {
        this.playerSettings = playerSettings
    }

    fun setPlayerSettings(playerSettings: PlayerSettings) {
        this.playerSettings = playerSettings
    }

    fun getPlayerSettings(): PlayerSettings = playerSettings

    fun setMetronomeEnabled(isMetronomeEnabled: Boolean) {
        playerSettings = playerSettings.copy(isMetronomeEnabled = isMetronomeEnabled)
    }

    fun isMetronomeEnabled(): Boolean = playerSettings.isMetronomeEnabled

    private val metronomeNotes: List<Note> =
        listOf(
            Note.Pitched(
                NoteDuration(NoteValue.QUARTER),
                NoteAttributes(null, null, null, null),
                Pitch(NoteName.G, Octave.unsafeCreate(4))
            ),
            Note.Pitched(
                NoteDuration(NoteValue.QUARTER),
                NoteAttributes(null, null, null, null),
                Pitch(NoteName.E, Octave.unsafeCreate(4))
            ),
            Note.Pitched(
                NoteDuration(NoteValue.QUARTER),
                NoteAttributes(null, null, null, null),
                Pitch(NoteName.E, Octave.unsafeCreate(4))
            ),
            Note.Pitched(
                NoteDuration(NoteValue.QUARTER),
                NoteAttributes(null, null, null, null),
                Pitch(NoteName.E, Octave.unsafeCreate(4))
            )
        )

    open suspend fun stop() {
        stopEverything()
    }

    suspend fun play(measures: List<Measure>, at: LocalDateTime) {
        coroutineScope {
            println("Playing measures $measures")
            launch {
                play(
                    measures.flatMap { measure -> measure.notes },
                    at,
                    playerSettings.scorePlayerChannelNumber
                )
            }
            launch {
                if (playerSettings.isMetronomeEnabled) {
                    val metronomeNotesForAllMeasures = measures.indices.flatMap { metronomeNotes }
                    play(
                        metronomeNotesForAllMeasures,
                        at,
                        playerSettings.metronomePlayerChannelNumber
                    )
                }
            }
        }
    }

    private suspend fun play(notes: List<Note>, at: LocalDateTime, onChannelNumber: Int) {
        val listOfNotesWithTheTotalDurationBeforeThatNoteIsPlayed =
            notes
                .mapIndexed { index, note ->
                    val durationOfPreviousNote: Duration =
                        if (index < 1) {
                            Duration.ZERO
                        } else {
                            val previousNote: Note = notes[index - 1]
                            helperFunForDurationOfNoteToJavaTimeDuration(
                                previousNote,
                                playerSettings.metronome
                            )
                        }
                    durationOfPreviousNote to note
                }
                .runningReduce {
                    (accDuration: Duration, _: Note),
                    (nextNoteDuration: Duration, nextNote: Note) ->
                    accDuration.plus(nextNoteDuration) to nextNote
                }

        listOfNotesWithTheTotalDurationBeforeThatNoteIsPlayed.forEach { (duration, note) ->
            val playAt = at.plus(duration)
            schedule(playAt) { playNote(note, onChannelNumber) }
            val noteOffAt =
                playAt.plus(
                    helperFunForDurationOfNoteToJavaTimeDuration(note, playerSettings.metronome)
                )
            schedule(noteOffAt) { stopNote(note, onChannelNumber) }
        }
    }

    abstract fun playNote(note: Note, onChannelNumber: Int)

    abstract fun stopNote(note: Note, onChannelNumber: Int)

    abstract fun stopEverything()

    private suspend fun schedule(time: LocalDateTime, function: () -> Unit) = coroutineScope {
        launch {
            delay(Duration.between(LocalDateTime.now(), time).toMillis())
            function()
        }
    }
}
