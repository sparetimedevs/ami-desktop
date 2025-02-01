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

package com.sparetimedevs.ami.music.core

import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiChannel
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiProgram
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrument
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrumentName
import com.sparetimedevs.ami.music.data.kotlin.part.PartName
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.example.getExampleScore
import com.sparetimedevs.ami.music.example.getExampleScoreHeighHoNobodyHome
import com.sparetimedevs.ami.test.data.createEmptyScore
import com.sparetimedevs.ami.test.data.createPitchedNote
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class ScoreReplaceMeasuresKtTest :
    StringSpec({
        "replaceMeasures should return the score with new measures first followed by original measures " +
            "when there are more original measures than measures to replace" {
                val score = getExampleScoreHeighHoNobodyHome()

                val newMeasures: List<Measure> =
                    listOf(
                        Measure(
                            null,
                            listOf(
                                createPitchedNote(noteName = NoteName.C, duration = NoteValue.WHOLE),
                            ),
                        ),
                        Measure(
                            null,
                            listOf(
                                createPitchedNote(noteName = NoteName.F, duration = NoteValue.WHOLE),
                            ),
                        ),
                    )

                val result = score.replaceMeasures(newMeasures)

                val expectedScore =
                    score.copy(
                        parts =
                            listOf(
                                Part(
                                    id = PartId.unsafeCreate("p-1"),
                                    name = PartName.unsafeCreate("Part one"),
                                    instrument =
                                        PartInstrument(
                                            name = PartInstrumentName.unsafeCreate("Grand Piano"),
                                            midiChannel = MidiChannel.unsafeCreate(0),
                                            midiProgram = MidiProgram.unsafeCreate(1),
                                        ),
                                    measures =
                                        newMeasures +
                                            getExampleScoreHeighHoNobodyHome()
                                                .parts[0]
                                                .measures
                                                .drop(2),
                                ),
                            ),
                    )

                result shouldBe expectedScore
            }

        "replaceMeasures should return the score with the new measures " +
            "when the measures to replace are more than the amount of original measures" {
                val score = getExampleScore()

                val newMeasures: List<Measure> = getExampleScoreHeighHoNobodyHome().parts[0].measures

                score.parts[0].measures.size shouldBeLessThan newMeasures.size

                val result = score.replaceMeasures(newMeasures)

                val expectedScore =
                    score.copy(
                        parts =
                            listOf(
                                Part(
                                    id = PartId.unsafeCreate("p-1"),
                                    name = PartName.unsafeCreate("Part one"),
                                    instrument =
                                        PartInstrument(
                                            name = PartInstrumentName.unsafeCreate("Grand Piano"),
                                            midiChannel = MidiChannel.unsafeCreate(0),
                                            midiProgram = MidiProgram.unsafeCreate(1),
                                        ),
                                    measures = newMeasures,
                                ),
                            ),
                    )

                result shouldBe expectedScore
            }

        "replaceMeasures should return the score with the new measures " +
            "when the measures to replace are the same amount as the amount of original measures" {
                val score = getExampleScoreHeighHoNobodyHome()

                val newMeasures: List<Measure> =
                    getExampleScoreHeighHoNobodyHome().parts[0].measures.dropLast(2) +
                        listOf(
                            Measure(
                                null,
                                listOf(
                                    createPitchedNote(noteName = NoteName.C, duration = NoteValue.WHOLE),
                                ),
                            ),
                            Measure(
                                null,
                                listOf(
                                    createPitchedNote(noteName = NoteName.F, duration = NoteValue.WHOLE),
                                ),
                            ),
                        )

                val result = score.replaceMeasures(newMeasures)

                val expectedScore =
                    score.copy(
                        parts =
                            listOf(
                                Part(
                                    id = PartId.unsafeCreate("p-1"),
                                    name = PartName.unsafeCreate("Part one"),
                                    instrument =
                                        PartInstrument(
                                            name = PartInstrumentName.unsafeCreate("Grand Piano"),
                                            midiChannel = MidiChannel.unsafeCreate(0),
                                            midiProgram = MidiProgram.unsafeCreate(1),
                                        ),
                                    measures = newMeasures,
                                ),
                            ),
                    )

                result shouldBe expectedScore
            }

        "replaceMeasures should return the original empty score " +
            "when the original score's measures is empty and the measures to replace is empty" {
                val score: Score = createEmptyScore()
                val newMeasures: List<Measure> = emptyList()

                val result: Score = score.replaceMeasures(newMeasures)

                result shouldBeSameInstanceAs score
            }

        "replaceMeasures should return the original score " +
            "when the original score's measures is not empty and the measures to replace is empty" {
                val score = getExampleScoreHeighHoNobodyHome()

                val newMeasures: List<Measure> = emptyList()

                val result = score.replaceMeasures(newMeasures)

                result shouldBeSameInstanceAs score
            }

        "replaceMeasures should return the score with new measures " +
            "when the measures to replace is not empty and the original measures is empty" {
                val score: Score = createEmptyScore()
                val newMeasures: List<Measure> =
                    listOf(
                        Measure(
                            null,
                            listOf(
                                createPitchedNote(noteName = NoteName.C, duration = NoteValue.WHOLE),
                            ),
                        ),
                        Measure(
                            null,
                            listOf(
                                createPitchedNote(noteName = NoteName.F, duration = NoteValue.WHOLE),
                            ),
                        ),
                    )

                val result: Score = score.replaceMeasures(newMeasures)

                val expectedScore =
                    score.copy(
                        parts =
                            listOf(
                                Part(
                                    id = PartId.unsafeCreate("p-1"),
                                    name = null,
                                    instrument = null,
                                    measures = newMeasures,
                                ),
                            ),
                    )

                result shouldBe expectedScore
            }
    })
