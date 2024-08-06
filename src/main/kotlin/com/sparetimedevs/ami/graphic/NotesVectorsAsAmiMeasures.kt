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

import arrow.core.Either
import arrow.core.EitherNel
import arrow.core.flattenOrAccumulate
import arrow.core.left
import arrow.core.nel
import arrow.core.right
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationErrorForProperty
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.core.validation.combineAllValidationErrors
import com.sparetimedevs.ami.graphic.vector.NoteVectors
import com.sparetimedevs.ami.graphic.vector.WHOLE_NOTE_WIDTH
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.note.Note
import com.sparetimedevs.ami.music.data.kotlin.note.NoteAttributes
import com.sparetimedevs.ami.music.data.kotlin.note.NoteDuration
import com.sparetimedevs.ami.music.data.kotlin.note.NoteName
import com.sparetimedevs.ami.music.data.kotlin.note.Octave
import com.sparetimedevs.ami.music.data.kotlin.note.Pitch
import com.sparetimedevs.ami.music.data.kotlin.note.Semitones
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForMeasure
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForNote

fun asAmiMeasures(
    notesVectorsPerMeasure: Map<Int, List<NoteVectors>>
): EitherNel<ValidationError, List<Measure>> =
    notesVectorsPerMeasure
        .map { notesVectorsForMeasure: Map.Entry<Int, List<NoteVectors>> ->
            val validationIdentifier: ValidationIdentifier =
                ValidationIdentifierForMeasure(
                    notesVectorsForMeasure.key,
                    NoValidationIdentifier /* could add part and score context */
                )
            asAmiMeasure(notesVectorsForMeasure.value, validationIdentifier)
        }
        .flattenOrAccumulate()

fun asAmiMeasure(
    notesVectors: List<NoteVectors>,
    validationIdentifier: ValidationIdentifier
): EitherNel<ValidationError, Measure> {
    val measureOrErrors =
        asAmiNotes(validationIdentifier, notesVectors).combineAllValidationErrors().map { notes ->
            Measure(null, notes)
        }
    return measureOrErrors
}

private tailrec fun asAmiNotes(
    validationIdentifier: ValidationIdentifier,
    notesVectors: List<NoteVectors>,
    acc: List<EitherNel<ValidationError, Note>> = emptyList()
): List<EitherNel<ValidationError, Note>> =
    if (notesVectors.isEmpty()) acc
    else {
        val validationIdentifierForNote =
            ValidationIdentifierForNote(acc.size, validationIdentifier)

        val operatingFromOctave = Octave.unsafeCreate(0) // Randomly chosen

        // Do not get first, but first to process (basically the lowest start x value)
        // Later we need to add check if chord is happening or maybe a pause.
        val noteVectorsIndexed: IndexedValue<NoteVectors> =
            notesVectors.withIndex().minBy { (_, f) -> f.start.x }
        val notesVectorsRemaining =
            notesVectors.filterIndexed { index, _ -> index != noteVectorsIndexed.index }
        val noteVectors: NoteVectors = noteVectorsIndexed.value

        val startX: Double = noteVectors.start.x
        val startY: Double = noteVectors.start.y
        val endX: Double = noteVectors.end.x
        val width = endX - startX

        val noteOrError: EitherNel<ValidationError, Note> =
            Either.zipOrAccumulate(
                noteDuration(width, validationIdentifierForNote),
                pitch(startY, operatingFromOctave, validationIdentifierForNote)
            ) { noteDuration, pitch ->
                Note.Pitched(noteDuration, NoteAttributes(null, null, null, null), pitch)
            }

        asAmiNotes(validationIdentifier, notesVectorsRemaining, acc.plus(noteOrError))
    }

// Before we reach this method, we actually need to make sure that width is snapped to the nearest
// valid value. This is currently done in
// `PathDataRepositoryImpl#addToPathData(nonnormalizedPathNode: PathNode): List<PathNode>`
private fun noteDuration(
    width: Double,
    validationIdentifier: ValidationIdentifier
): EitherNel<ValidationError, NoteDuration> =
    NoteDuration.validate(
        input = width / WHOLE_NOTE_WIDTH,
        validationIdentifier = validationIdentifier
    )

private fun pitch(
    height: Double,
    operatingFromOctave: Octave,
    validationIdentifier: ValidationIdentifier
): EitherNel<ValidationError, Pitch> {
    val remainderHeight = height % 6
    return Either.zipOrAccumulate(
        noteName(remainderHeight, validationIdentifier),
        Octave.validate(
            (operatingFromOctave.value + ((height - remainderHeight) / 6).toInt()).toByte(),
            validationIdentifier
        ),
        Semitones.validate(0.0f) // Not yet implemented.
    ) { noteName, octave, alter ->
        Pitch(noteName, octave, alter)
    }
}

private fun noteName(
    height: Double,
    validationIdentifier: ValidationIdentifier
): EitherNel<ValidationError, NoteName> {

    val noteName =
        when (height) {
            0.0 -> NoteName.A_FLAT.right() // or G_SHARP from one lower octave
            0.5 -> NoteName.A.right()
            1.0 -> NoteName.B_FLAT.right() // or A_SHARP
            1.5 -> NoteName.B.right()
            2.0 -> NoteName.C.right()
            2.5 -> NoteName.D_FLAT.right() // or C_SHARP
            3.0 -> NoteName.D.right()
            3.5 -> NoteName.E_FLAT.right() // or D_SHARP
            4.0 -> NoteName.E.right()
            4.5 -> NoteName.F.right()
            5.0 -> NoteName.G_FLAT.right() // or F_SHARP
            5.5 -> NoteName.G.right()
            else ->
                ValidationError(
                        "Unable to map height to NoteName",
                        ValidationErrorForProperty("height"),
                        validationIdentifier
                    )
                    .nel()
                    .left()
        }

    return noteName
}
