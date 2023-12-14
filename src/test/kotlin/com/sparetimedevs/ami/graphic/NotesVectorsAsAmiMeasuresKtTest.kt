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

import arrow.core.NonEmptyList
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.graphic.vector.NoteVectors
import com.sparetimedevs.ami.graphic.vector.Vector
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteAttributes
import com.sparetimedevs.ami.music.data.kotlin.note.NoteDuration
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec

class NotesVectorsAsAmiMeasuresKtTest :
    StringSpec({
        "asAmiMeasures should return one measure with one note when NoteVectors for one note" {
            val notesVectorsPerMeasure =
                mapOf(
                    "measure_1" to
                        listOf<NoteVectors>(NoteVectors(Vector(0.0, 26.0), Vector(400.0, 26.0)))
                )
            val expectedResult: List<Measure> =
                listOf(
                    Measure(
                        null,
                        listOf(
                            Note.Pitched(
                                NoteDuration(NoteValue.WHOLE),
                                NoteAttributes(null, null, null, null),
                                Pitch(NoteName.C, Octave.unsafeCreate(4))
                            )
                        )
                    )
                )

            val result = asAmiMeasures(notesVectorsPerMeasure)

            result shouldBeRight expectedResult
        }

        "asAmiMeasures should return two measures with one all notes in C ionian mode (C major)" {
            val notesVectorsPerMeasure =
                mapOf(
                    "measure_1" to
                        listOf<NoteVectors>(
                            NoteVectors(Vector(0.0, 26.0), Vector(100.0, 26.0)),
                            NoteVectors(Vector(100.0, 27.0), Vector(200.0, 27.0)),
                            NoteVectors(Vector(200.0, 27.5), Vector(300.0, 27.5)),
                            NoteVectors(Vector(300.0, 28.5), Vector(400.0, 28.5))
                        ),
                    "measure_2" to
                        listOf<NoteVectors>(
                            NoteVectors(Vector(0.0, 29.5), Vector(100.0, 29.5)),
                            NoteVectors(Vector(100.0, 30.5), Vector(200.0, 30.5)),
                            NoteVectors(Vector(200.0, 31.0), Vector(300.0, 31.0)),
                            NoteVectors(Vector(300.0, 32.0), Vector(400.0, 32.0))
                        )
                )

            val result = asAmiMeasures(notesVectorsPerMeasure)

            result shouldBeRight ExpectedResult
        }

        "asAmiMeasures should return list of errors when there are errors in the list of NoteVectors" {
            val notesVectorsPerMeasure =
                mapOf(
                    "measure_1" to
                        listOf<NoteVectors>(
                            NoteVectors(Vector(0.0, 26.0), Vector(100.0, 26.0)),
                            NoteVectors(Vector(100.0, 27.0), Vector(200.0, 27.0)),
                            NoteVectors(Vector(200.0, 27.5), Vector(300.0, 27.5)),
                            NoteVectors(Vector(300.0, 28.5), Vector(400.0, 28.5))
                        ),
                    "measure_2" to
                        listOf<NoteVectors>(
                            NoteVectors(Vector(0.0, 29.34567), Vector(100.0, 29.34567)),
                            NoteVectors(Vector(100.0, 30.5), Vector(593.827156, 30.5)),
                            NoteVectors(
                                Vector(593.827156, 30.84567),
                                Vector(1087.654312, 30.84567)
                            ),
                            NoteVectors(Vector(1087.654312, 32.0), Vector(1187.654312, 32.0))
                        )
                )

            val result = asAmiMeasures(notesVectorsPerMeasure)

            result shouldBeLeft
                NonEmptyList(
                    ValidationError(message = "Unable to map height to NoteName"),
                    listOf(
                        ValidationError(
                            message =
                                "Input for note duration is not a valid value, the value is: 1.23456789"
                        ),
                        ValidationError(
                            message =
                                "Input for note duration is not a valid value, the value is: 1.23456789"
                        ),
                        ValidationError(message = "Unable to map height to NoteName")
                    )
                )
        }

        "asAmiMeasure should return measure with 4 notes when NoteVectors for 4 notes" {
            val notesVectorsForOneMeasure =
                listOf<NoteVectors>(
                    NoteVectors(Vector(0.0, 26.0), Vector(100.0, 26.0)),
                    NoteVectors(Vector(100.0, 28.0), Vector(200.0, 28.0)),
                    NoteVectors(Vector(200.0, 26.0), Vector(300.0, 26.0)),
                    NoteVectors(Vector(300.0, 30.0), Vector(400.0, 30.0)),
                )
            val expectedResult: Measure =
                Measure(
                    null,
                    listOf(
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.C, Octave.unsafeCreate(4))
                        ),
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.E, Octave.unsafeCreate(4))
                        ),
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.C, Octave.unsafeCreate(4))
                        ),
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.A_FLAT, Octave.unsafeCreate(5))
                        )
                    )
                )

            val result = asAmiMeasure(notesVectorsForOneMeasure)

            result shouldBeRight expectedResult
        }

        "asAmiMeasure should return measure with 4 notes in order when NoteVectors for 4 notes out of order" {
            val notesVectorsForOneMeasure =
                listOf<NoteVectors>(
                    NoteVectors(Vector(0.0, 26.0), Vector(100.0, 26.0)),
                    NoteVectors(Vector(300.0, 30.0), Vector(400.0, 30.0)),
                    NoteVectors(Vector(200.0, 26.0), Vector(300.0, 26.0)),
                    NoteVectors(Vector(100.0, 28.0), Vector(200.0, 28.0))
                )
            val expectedResult: Measure =
                Measure(
                    null,
                    listOf(
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.C, Octave.unsafeCreate(4))
                        ),
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.E, Octave.unsafeCreate(4))
                        ),
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.C, Octave.unsafeCreate(4))
                        ),
                        Note.Pitched(
                            NoteDuration(NoteValue.QUARTER),
                            NoteAttributes(null, null, null, null),
                            Pitch(NoteName.A_FLAT, Octave.unsafeCreate(5))
                        )
                    )
                )

            val result = asAmiMeasure(notesVectorsForOneMeasure)

            result shouldBeRight expectedResult
        }
    })

val ExpectedResult: List<Measure> =
    listOf(
        Measure(
            null,
            listOf(
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.C, Octave.unsafeCreate(4))
                ),
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.D, Octave.unsafeCreate(4))
                ),
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.E_FLAT, Octave.unsafeCreate(4))
                ),
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.F, Octave.unsafeCreate(4))
                ),
            )
        ),
        Measure(
            null,
            listOf(
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.G, Octave.unsafeCreate(4))
                ),
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.A, Octave.unsafeCreate(5))
                ),
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.B_FLAT, Octave.unsafeCreate(5))
                ),
                Note.Pitched(
                    NoteDuration(NoteValue.QUARTER),
                    NoteAttributes(null, null, null, null),
                    Pitch(NoteName.C, Octave.unsafeCreate(5))
                )
            )
        )
    )
