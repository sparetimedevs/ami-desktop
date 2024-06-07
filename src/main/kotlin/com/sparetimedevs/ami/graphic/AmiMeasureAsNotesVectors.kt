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

package com.sparetimedevs.ami.graphic

import arrow.core.tail
import com.sparetimedevs.ami.graphic.vector.NoteVectors
import com.sparetimedevs.ami.graphic.vector.Vector
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch

// This assumes an ordered list of Measure and NoteVectors
// Or not? Showcase it with a test!
fun asNotesVectors(measure: Measure): List<NoteVectors> = vectors(notes = measure.notes)

private tailrec fun vectors(
    acc: List<NoteVectors> = emptyList(),
    notes: List<Note>
): List<NoteVectors> =
    if (notes.isEmpty()) acc
    else {
        val note = notes.first()
        val (startX: Double, endX: Double) =
            if (acc.isEmpty()) {
                0.0 to vectorComponentXAtEnd(note, 0.0)
            } else {
                acc.last().end.x to vectorComponentXAtEnd(note, acc.last().end.x)
            }

        val ys: List<Double> =
            when (note) {
                is Note.Pitched -> listOf(vectorComponentY(note.pitch))
                is Note.Chord ->
                    listOf(vectorComponentY(note.rootNote))
                        .plus(note.pitches.map { vectorComponentY(it) })
                is Note.Rest ->
                    listOf(
                        -7.5
                    ) // Probably want to reevaluate if this is a good value for a rest note.
                is Note.Unpitched ->
                    listOf(
                        570.0
                    ) // Probably want to reevaluate if this is a good value for unpitched note.
            }

        val newAcc = acc.plus(ys.map { y -> NoteVectors(Vector(startX, y), Vector(endX, y)) })

        vectors(newAcc, notes.tail())
    }

private fun vectorComponentXAtEnd(note: Note, currentX: Double): Double =
    currentX + (note.duration.value * 400)

private fun vectorComponentY(pitch: Pitch): Double =
    vectorComponentY(pitch.noteName) + (pitch.octave.value * 6)

private fun vectorComponentY(noteName: NoteName): Double =
    when (noteName) {
        NoteName.A_FLAT -> 0.0
        NoteName.A -> 0.5
        NoteName.A_SHARP,
        NoteName.B_FLAT -> 1.0
        NoteName.B -> 1.5
        NoteName.C -> 2.0
        NoteName.C_SHARP,
        NoteName.D_FLAT -> 2.5
        NoteName.D -> 3.0
        NoteName.D_SHARP,
        NoteName.E_FLAT -> 3.5
        NoteName.E -> 4.0
        NoteName.F -> 4.5
        NoteName.F_SHARP,
        NoteName.G_FLAT -> 5.0
        NoteName.G -> 5.5
        NoteName.G_SHARP -> 6.0
    }
