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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepository
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForMeasure
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForNote

class MarkInvalidThings(private val pathDataRepository: PathDataRepository) {

    fun markInvalidNotesAndMeasuresRed(validationIdentifier: ValidationIdentifier): Unit {
        when (validationIdentifier) {
            is ValidationIdentifierForNote -> markNoteRed(validationIdentifier)
            is ValidationIdentifierForMeasure -> markMeasureRed(validationIdentifier)
        }
    }

    private fun markNoteRed(validationIdentifierForNote: ValidationIdentifierForNote): Unit {
        val validationIdentifierForMeasure =
            returnFirstScoreValidationIdentifier(
                validationIdentifierForNote.validationIdentifierParent
            )
        val measureIndex = validationIdentifierForMeasure.measureIndex
        val noteIndex = validationIdentifierForNote.noteIndex
        pathDataRepository.getPathData()
        // Here we could add some logic to get the invalid note from the measure in which it
        // resides.
        // Then we can also mark that note specifically. (Change color, draw circle?)
        markInvalidNotesAndMeasuresRed(validationIdentifierForNote.validationIdentifierParent)
    }

    private tailrec fun returnFirstScoreValidationIdentifier(
        validationIdentifier: ValidationIdentifier
    ): ValidationIdentifierForMeasure =
        if (validationIdentifier is ValidationIdentifierForMeasure) validationIdentifier
        else {
            returnFirstScoreValidationIdentifier(validationIdentifier.validationIdentifierParent)
        }

    private fun markMeasureRed(
        validationIdentifierForMeasure: ValidationIdentifierForMeasure
    ): Unit {
        val xStart = validationIdentifierForMeasure.measureIndex * 575.0f + 87.5f

        val pathData: List<PathNode> =
            PathBuilder().moveTo(x = xStart, y = 400.0f).horizontalLineTo(x = xStart + 500).nodes
        pathDataRepository.addToErrorMarkingPathData(
            pathData
        ) // TODO just start with a line on top. Later on we can extend it to a complete border
        // around a measure.
    }
}
