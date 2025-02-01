/*
 * Copyright (c) 2023-2025 sparetimedevs and respective authors and developers.
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

import com.sparetimedevs.ami.graphic.vector.NoteVectors
import com.sparetimedevs.ami.graphic.vector.Vector
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch
import com.sparetimedevs.ami.test.data.createChord
import com.sparetimedevs.ami.test.data.createPitchedNote
import com.sparetimedevs.ami.test.data.createRestNote
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AmiMeasureAsNotesVectorsKtTest :
    StringSpec({
        "asNotesVectors should create NoteVectors for one note" {
            val note: List<Note> =
                listOf(createPitchedNote(noteName = NoteName.C, duration = NoteValue.WHOLE))
            val measure: Measure = Measure(null, note)

            val result: List<NoteVectors> = asNotesVectors(measure)

            result shouldBe listOf(NoteVectors(Vector(0.0, 26.0), Vector(400.0, 26.0)))
        }

        "asNotesVectors should create NoteVectors for two notes" {
            val notes: List<Note> =
                listOf(
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue.HALF),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue.HALF),
                )
            val measure: Measure = Measure(null, notes)

            val result: List<NoteVectors> = asNotesVectors(measure)

            result shouldBe
                listOf(
                    NoteVectors(Vector(0.0, 26.0), Vector(200.0, 26.0)),
                    NoteVectors(Vector(200.0, 24.5), Vector(400.0, 24.5)),
                )
        }

        "asNotesVectors should create NoteVectors for rests" {
            val notes: List<Note> =
                listOf(
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue.QUARTER),
                    createRestNote(duration = NoteValue.QUARTER),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue.QUARTER),
                    createRestNote(duration = NoteValue.QUARTER),
                )
            val measure: Measure = Measure(null, notes)

            val result: List<NoteVectors> = asNotesVectors(measure)

            result shouldBe
                listOf(
                    NoteVectors(Vector(0.0, 29.5), Vector(100.0, 29.5)),
                    NoteVectors(Vector(100.0, -7.5), Vector(200.0, -7.5)),
                    NoteVectors(Vector(200.0, 27.0), Vector(300.0, 27.0)),
                    NoteVectors(Vector(300.0, -7.5), Vector(400.0, -7.5)),
                )
        }

        "asNotesVectors should create NoteVectors for all note steps" {
            val notes: List<Note> =
                listOf(
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.A, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.B, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._1024TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._1024TH),
                    createRestNote(duration = NoteValue._16TH),
                    createRestNote(duration = NoteValue._8TH),
                    createRestNote(duration = NoteValue.QUARTER),
                    createRestNote(duration = NoteValue.HALF),
                )

            val measure: Measure = Measure(null, notes)

            val result: List<NoteVectors> = asNotesVectors(measure)

            result shouldBe
                listOf(
                    NoteVectors(Vector(0.0, 24.5), Vector(0.390625, 24.5)),
                    NoteVectors(Vector(0.390625, 25.5), Vector(0.78125, 25.5)),
                    NoteVectors(Vector(0.78125, 26.0), Vector(1.171875, 26.0)),
                    NoteVectors(Vector(1.171875, 27.0), Vector(1.5625, 27.0)),
                    NoteVectors(Vector(1.5625, 28.0), Vector(1.953125, 28.0)),
                    NoteVectors(Vector(1.953125, 28.5), Vector(2.34375, 28.5)),
                    NoteVectors(Vector(2.34375, 29.5), Vector(2.734375, 29.5)),
                    NoteVectors(Vector(2.734375, -7.5), Vector(3.125, -7.5)),
                    NoteVectors(Vector(3.125, 24.5), Vector(3.515625, 24.5)),
                    NoteVectors(Vector(3.515625, 25.5), Vector(3.90625, 25.5)),
                    NoteVectors(Vector(3.90625, 26.0), Vector(4.296875, 26.0)),
                    NoteVectors(Vector(4.296875, 27.0), Vector(4.6875, 27.0)),
                    NoteVectors(Vector(4.6875, 28.0), Vector(5.078125, 28.0)),
                    NoteVectors(Vector(5.078125, 28.5), Vector(5.46875, 28.5)),
                    NoteVectors(Vector(5.46875, 29.5), Vector(5.859375, 29.5)),
                    NoteVectors(Vector(5.859375, -7.5), Vector(6.25, -7.5)),
                    NoteVectors(Vector(6.25, 24.5), Vector(6.640625, 24.5)),
                    NoteVectors(Vector(6.640625, 25.5), Vector(7.03125, 25.5)),
                    NoteVectors(Vector(7.03125, 26.0), Vector(7.421875, 26.0)),
                    NoteVectors(Vector(7.421875, 27.0), Vector(7.8125, 27.0)),
                    NoteVectors(Vector(7.8125, 28.0), Vector(8.203125, 28.0)),
                    NoteVectors(Vector(8.203125, 28.5), Vector(8.59375, 28.5)),
                    NoteVectors(Vector(8.59375, 29.5), Vector(8.984375, 29.5)),
                    NoteVectors(Vector(8.984375, -7.5), Vector(9.375, -7.5)),
                    NoteVectors(Vector(9.375, 24.5), Vector(9.765625, 24.5)),
                    NoteVectors(Vector(9.765625, 25.5), Vector(10.15625, 25.5)),
                    NoteVectors(Vector(10.15625, 26.0), Vector(10.546875, 26.0)),
                    NoteVectors(Vector(10.546875, 27.0), Vector(10.9375, 27.0)),
                    NoteVectors(Vector(10.9375, 28.0), Vector(11.328125, 28.0)),
                    NoteVectors(Vector(11.328125, 28.5), Vector(11.71875, 28.5)),
                    NoteVectors(Vector(11.71875, 29.5), Vector(12.109375, 29.5)),
                    NoteVectors(Vector(12.109375, -7.5), Vector(12.5, -7.5)),
                    NoteVectors(Vector(12.5, 24.5), Vector(12.890625, 24.5)),
                    NoteVectors(Vector(12.890625, 25.5), Vector(13.28125, 25.5)),
                    NoteVectors(Vector(13.28125, 26.0), Vector(13.671875, 26.0)),
                    NoteVectors(Vector(13.671875, 27.0), Vector(14.0625, 27.0)),
                    NoteVectors(Vector(14.0625, 28.0), Vector(14.453125, 28.0)),
                    NoteVectors(Vector(14.453125, 28.5), Vector(14.84375, 28.5)),
                    NoteVectors(Vector(14.84375, 29.5), Vector(15.234375, 29.5)),
                    NoteVectors(Vector(15.234375, -7.5), Vector(15.625, -7.5)),
                    NoteVectors(Vector(15.625, 24.5), Vector(16.015625, 24.5)),
                    NoteVectors(Vector(16.015625, 25.5), Vector(16.40625, 25.5)),
                    NoteVectors(Vector(16.40625, 26.0), Vector(16.796875, 26.0)),
                    NoteVectors(Vector(16.796875, 27.0), Vector(17.1875, 27.0)),
                    NoteVectors(Vector(17.1875, 28.0), Vector(17.578125, 28.0)),
                    NoteVectors(Vector(17.578125, 28.5), Vector(17.96875, 28.5)),
                    NoteVectors(Vector(17.96875, 29.5), Vector(18.359375, 29.5)),
                    NoteVectors(Vector(18.359375, -7.5), Vector(18.75, -7.5)),
                    NoteVectors(Vector(18.75, 24.5), Vector(19.140625, 24.5)),
                    NoteVectors(Vector(19.140625, 25.5), Vector(19.53125, 25.5)),
                    NoteVectors(Vector(19.53125, 26.0), Vector(19.921875, 26.0)),
                    NoteVectors(Vector(19.921875, 27.0), Vector(20.3125, 27.0)),
                    NoteVectors(Vector(20.3125, 28.0), Vector(20.703125, 28.0)),
                    NoteVectors(Vector(20.703125, 28.5), Vector(21.09375, 28.5)),
                    NoteVectors(Vector(21.09375, 29.5), Vector(21.484375, 29.5)),
                    NoteVectors(Vector(21.484375, -7.5), Vector(21.875, -7.5)),
                    NoteVectors(Vector(21.875, 24.5), Vector(22.265625, 24.5)),
                    NoteVectors(Vector(22.265625, 25.5), Vector(22.65625, 25.5)),
                    NoteVectors(Vector(22.65625, 26.0), Vector(23.046875, 26.0)),
                    NoteVectors(Vector(23.046875, 27.0), Vector(23.4375, 27.0)),
                    NoteVectors(Vector(23.4375, 28.0), Vector(23.828125, 28.0)),
                    NoteVectors(Vector(23.828125, 28.5), Vector(24.21875, 28.5)),
                    NoteVectors(Vector(24.21875, 29.5), Vector(24.609375, 29.5)),
                    NoteVectors(Vector(24.609375, -7.5), Vector(25.0, -7.5)),
                    NoteVectors(Vector(25.0, -7.5), Vector(50.0, -7.5)),
                    NoteVectors(Vector(50.0, -7.5), Vector(100.0, -7.5)),
                    NoteVectors(Vector(100.0, -7.5), Vector(200.0, -7.5)),
                    NoteVectors(Vector(200.0, -7.5), Vector(400.0, -7.5)),
                )
        }

        "asNotesVectors should create NoteVectors for all note steps in C ionian mode (C major)" {
            val notes: List<Note> =
                listOf(
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.E, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._8TH),
                    createPitchedNote(
                        noteName = NoteName.A,
                        octave = Octave.unsafeCreate(5),
                        duration = NoteValue._8TH,
                    ),
                    createPitchedNote(
                        noteName = NoteName.B,
                        octave = Octave.unsafeCreate(5),
                        duration = NoteValue._8TH,
                    ),
                    createPitchedNote(
                        noteName = NoteName.C,
                        octave = Octave.unsafeCreate(5),
                        duration = NoteValue._8TH,
                    ),
                )

            val measure: Measure = Measure(null, notes)

            val result: List<NoteVectors> = asNotesVectors(measure)

            result shouldBe
                listOf(
                    NoteVectors(Vector(0.0, 26.0), Vector(50.0, 26.0)),
                    NoteVectors(Vector(50.0, 27.0), Vector(100.0, 27.0)),
                    NoteVectors(Vector(100.0, 28.0), Vector(150.0, 28.0)),
                    NoteVectors(Vector(150.0, 28.5), Vector(200.0, 28.5)),
                    NoteVectors(Vector(200.0, 29.5), Vector(250.0, 29.5)),
                    NoteVectors(Vector(250.0, 30.5), Vector(300.0, 30.5)),
                    NoteVectors(Vector(300.0, 31.5), Vector(350.0, 31.5)),
                    NoteVectors(Vector(350.0, 32.0), Vector(400.0, 32.0)),
                )
        }

        "asNotesVectors should create NoteVectors for all note steps in C dorian mode" {
            val notes: List<Note> =
                listOf(
                    createPitchedNote(noteName = NoteName.C, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.D, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.E_FLAT, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.F, duration = NoteValue._8TH),
                    createPitchedNote(noteName = NoteName.G, duration = NoteValue._8TH),
                    createPitchedNote(
                        noteName = NoteName.A,
                        octave = Octave.unsafeCreate(5),
                        duration = NoteValue._8TH,
                    ),
                    createPitchedNote(
                        noteName = NoteName.B_FLAT,
                        octave = Octave.unsafeCreate(5),
                        duration = NoteValue._8TH,
                    ),
                    createPitchedNote(
                        noteName = NoteName.C,
                        octave = Octave.unsafeCreate(5),
                        duration = NoteValue._8TH,
                    ),
                )

            val measure: Measure = Measure(null, notes)

            val result: List<NoteVectors> = asNotesVectors(measure)

            result shouldBe
                listOf(
                    NoteVectors(Vector(0.0, 26.0), Vector(50.0, 26.0)),
                    NoteVectors(Vector(50.0, 27.0), Vector(100.0, 27.0)),
                    NoteVectors(Vector(100.0, 27.5), Vector(150.0, 27.5)),
                    NoteVectors(Vector(150.0, 28.5), Vector(200.0, 28.5)),
                    NoteVectors(Vector(200.0, 29.5), Vector(250.0, 29.5)),
                    NoteVectors(Vector(250.0, 30.5), Vector(300.0, 30.5)),
                    NoteVectors(Vector(300.0, 31.0), Vector(350.0, 31.0)),
                    NoteVectors(Vector(350.0, 32.0), Vector(400.0, 32.0)),
                )
        }

        "asNotesVectors should create NoteVectors for chords" {
            val notes: List<Note> =
                listOf(
                    // G major
                    createChord(
                        rootNote = Pitch(noteName = NoteName.G, octave = Octave.unsafeCreate(4)),
                        pitches =
                            listOf(
                                Pitch(noteName = NoteName.B, octave = Octave.unsafeCreate(4)),
                                Pitch(noteName = NoteName.D, octave = Octave.unsafeCreate(4)),
                            ),
                        duration = NoteValue.QUARTER,
                    ),
                    createRestNote(duration = NoteValue.QUARTER),
                    // D major
                    createChord(
                        rootNote = Pitch(noteName = NoteName.D, octave = Octave.unsafeCreate(4)),
                        pitches =
                            listOf(
                                Pitch(noteName = NoteName.F_SHARP, octave = Octave.unsafeCreate(4)),
                                Pitch(noteName = NoteName.A, octave = Octave.unsafeCreate(4)),
                            ),
                        duration = NoteValue.QUARTER,
                    ),
                    createRestNote(duration = NoteValue.QUARTER),
                )

            val measure: Measure = Measure(null, notes)

            val result: List<NoteVectors> = asNotesVectors(measure)

            result shouldBe
                listOf(
                    NoteVectors(Vector(0.0, 29.5), Vector(100.0, 29.5)),
                    NoteVectors(Vector(0.0, 25.5), Vector(100.0, 25.5)),
                    NoteVectors(Vector(0.0, 27.0), Vector(100.0, 27.0)),
                    NoteVectors(Vector(100.0, -7.5), Vector(200.0, -7.5)),
                    NoteVectors(Vector(200.0, 27.0), Vector(300.0, 27.0)),
                    NoteVectors(Vector(200.0, 29.0), Vector(300.0, 29.0)),
                    NoteVectors(Vector(200.0, 24.5), Vector(300.0, 24.5)),
                    NoteVectors(Vector(300.0, -7.5), Vector(400.0, -7.5)),
                )
        }
    })
