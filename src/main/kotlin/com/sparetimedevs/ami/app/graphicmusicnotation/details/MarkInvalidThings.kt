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
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForMeasure
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForNote

class MarkInvalidThings {

    fun markInvalidNotesAndMeasuresRed(
        validationIdentifier: ValidationIdentifier,
        graphicProperties: GraphicProperties,
        pathData: List<PathNode>,
        accumulatedErrorMarkingPathData: List<PathNode> = emptyList(),
    ): List<PathNode> {
        return when (validationIdentifier) {
            is ValidationIdentifierForNote ->
                markNoteRed(
                    validationIdentifier,
                    graphicProperties,
                    pathData,
                    accumulatedErrorMarkingPathData,
                )
            is ValidationIdentifierForMeasure ->
                markMeasureRed(
                    validationIdentifier,
                    graphicProperties,
                    accumulatedErrorMarkingPathData,
                )
            else -> accumulatedErrorMarkingPathData
        }
    }

    private fun markNoteRed(
        validationIdentifierForNote: ValidationIdentifierForNote,
        graphicProperties: GraphicProperties,
        pathData: List<PathNode>,
        accumulatedErrorMarkingPathData: List<PathNode>,
    ): List<PathNode> {
        val validationIdentifierForMeasure =
            returnFirstScoreValidationIdentifier(
                validationIdentifierForNote.validationIdentifierParent
            )
        val measureIndex = validationIdentifierForMeasure.measureIndex
        val noteIndex = validationIdentifierForNote.noteIndex

        pathData
        // Here we could add some logic to get the invalid note from the measure in which it
        // resides.
        // Then we can also mark that note specifically. (Change color, draw circle?)
        return markInvalidNotesAndMeasuresRed(
            validationIdentifierForNote.validationIdentifierParent,
            graphicProperties,
            pathData,
            accumulatedErrorMarkingPathData,
        )
    }

    private tailrec fun returnFirstScoreValidationIdentifier(
        validationIdentifier: ValidationIdentifier
    ): ValidationIdentifierForMeasure =
        if (validationIdentifier is ValidationIdentifierForMeasure) validationIdentifier
        else {
            returnFirstScoreValidationIdentifier(validationIdentifier.validationIdentifierParent)
        }

    private fun markMeasureRed(
        validationIdentifierForMeasure: ValidationIdentifierForMeasure,
        graphicProperties: GraphicProperties,
        accumulatedErrorMarkingPathData: List<PathNode>,
    ): List<PathNode> {
        val xStart = validationIdentifierForMeasure.measureIndex * 575.0f + 87.5f

        val errorMarkingPathData: List<PathNode> =
            PathBuilder().moveTo(x = xStart, y = 400.0f).horizontalLineTo(x = xStart + 500).nodes

        // TODO just start with a line on top. Later on we can extend it to a complete border
        // around a measure.

        return accumulatedErrorMarkingPathData + errorMarkingPathData
    }
}
