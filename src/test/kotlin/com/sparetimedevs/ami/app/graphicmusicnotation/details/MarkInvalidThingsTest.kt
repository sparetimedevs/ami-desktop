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
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepositoryImpl
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForNote
import com.sparetimedevs.ami.test.impl.ValidationIdentifierForTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty

class MarkInvalidThingsTest :
    StringSpec({
        val graphicProperties =
            GraphicProperties(
                offsetX = 87.5,
                offsetY = 400.0,
                measureWidth = 500.0,
                spaceBetweenMeasures = 75.0,
                cutOffXToReflectNoteIsEnding = 0.0,
                wholeStepExpressedInY = 50.0,
            )
        val pathDataRepository = PathDataRepositoryImpl(graphicProperties)
        val markInvalidThings = MarkInvalidThings(pathDataRepository)

        "markInvalidNotesAndMeasuresRed should return something when no ValidationIdentifierForMeasure in the hierarchy" {
            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = 555.5f, y = 700.0f)
                    .horizontalLineTo(x = 55.5f)
                    .moveTo(x = 87.5f, y = 9999.0f)
                    .horizontalLineTo(x = 587.5f)
                    .nodes

            pathDataRepository.replacePathData(pathData)

            val validationIdentifierForNote =
                ValidationIdentifierForNote(3, ValidationIdentifierForTest(this))

            pathDataRepository.getErrorMarkingPathData().shouldBeEmpty()

            //            val result =
            //
            // markInvalidThings.markInvalidNotesAndMeasuresRed(validationIdentifierForNote)

            //            result shouldBeLeft expectedDomainError

            // TODO here we need to check repository
            // Or better, return something from the function and at the usage site add stuff to
            // repo.
            //            pathDataRepository.getErrorMarkingPathData().shouldNotBeEmpty()
        }
    })
