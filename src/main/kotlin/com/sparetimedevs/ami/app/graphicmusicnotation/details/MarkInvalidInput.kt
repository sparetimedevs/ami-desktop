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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForMeasure
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForNote

class MarkInvalidInput {
    fun markInvalidNotesAndMeasuresRed(
        validationIdentifier: ValidationIdentifier,
        graphicProperties: GraphicProperties,
        pathData: List<PathNode>,
        accumulatedErrorMarkingPathData: List<PathNode> = emptyList(),
    ): List<PathNode> =
        when (validationIdentifier) {
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

    private fun markNoteRed(
        validationIdentifierForNote: ValidationIdentifierForNote,
        graphicProperties: GraphicProperties,
        pathData: List<PathNode>,
        accumulatedErrorMarkingPathData: List<PathNode>,
    ): List<PathNode> {
        val validationIdentifierForMeasure =
            returnFirstMeasureValidationIdentifier(
                validationIdentifierForNote.validationIdentifierParent,
            )
        val measureIndex = validationIdentifierForMeasure?.measureIndex
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

    private tailrec fun returnFirstMeasureValidationIdentifier(
        validationIdentifier: ValidationIdentifier,
    ): ValidationIdentifierForMeasure? =
        if (validationIdentifier is ValidationIdentifierForMeasure) {
            validationIdentifier
        } else if (validationIdentifier is NoValidationIdentifier) {
            null
        } else {
            returnFirstMeasureValidationIdentifier(validationIdentifier.validationIdentifierParent)
        }

    private fun markMeasureRed(
        validationIdentifierForMeasure: ValidationIdentifierForMeasure,
        graphicProperties: GraphicProperties,
        accumulatedErrorMarkingPathData: List<PathNode>,
    ): List<PathNode> {
        val xStart: Float =
            (
                validationIdentifierForMeasure.measureIndex *
                    (graphicProperties.measureWidth + graphicProperties.spaceBetweenMeasures) +
                    graphicProperties.offsetX
            ).toFloat()

        val errorMarkingPathData: List<PathNode> =
            PathBuilder()
                .moveTo(x = xStart, y = graphicProperties.offsetY.toFloat())
                .horizontalLineTo(x = xStart + graphicProperties.measureWidth.toFloat())
                .nodes

        return accumulatedErrorMarkingPathData + errorMarkingPathData
    }
}
