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
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForMeasure
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForNote
import com.sparetimedevs.ami.test.impl.ValidationIdentifierForTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

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
        val markInvalidThings = MarkInvalidThings()

        "markInvalidNotesAndMeasuresRed should return error marking path data" {
            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = 87.5f, y = 650.0f)
                    .lineTo(x = 337.5f, y = 650.0f)
                    .moveTo(x = 337.5f, y = 700.0f)
                    .lineTo(x = 587.5f, y = 700.0f)
                    .moveTo(x = 662.5f, y = 650.0f)
                    .lineTo(x = 787.5f, y = 650.0f)
                    .moveTo(x = 787.5f, y = 650.0f)
                    .lineTo(x = 850.0f, y = 650.0f)
                    .moveTo(x = 850.0f, y = 650.0f)
                    .lineTo(x = 912.5f, y = 650.0f)
                    .moveTo(x = 912.5f, y = 775.0f)
                    .lineTo(x = 1162.5f, y = 775.0f)
                    .moveTo(x = 1237.5f, y = 650.0f)
                    .lineTo(x = 1362.5f, y = 650.0f)
                    .moveTo(x = 1362.5f, y = 650.0f)
                    .lineTo(x = 1487.5f, y = 650.0f)
                    .moveTo(x = 1487.5f, y = 600.0f)
                    .lineTo(x = 1612.5f, y = 600.0f)
                    .moveTo(x = 1612.5f, y = 600.0f)
                    .lineTo(x = 2312.5f, y = 600.0f) // This note duration is too long.
                    .moveTo(x = 1812.5f, y = 575.0f)
                    .lineTo(x = 1875.0f, y = 575.0f)
                    .moveTo(x = 1875.0f, y = 575.0f)
                    .lineTo(x = 1937.5f, y = 575.0f)
                    .moveTo(x = 1937.5f, y = 575.0f)
                    .lineTo(x = 2000.0f, y = 575.0f)
                    .moveTo(x = 2000.0f, y = 575.0f)
                    .lineTo(x = 2062.5f, y = 575.0f)
                    .moveTo(x = 2062.5f, y = 600.0f)
                    .lineTo(x = 2312.5f, y = 600.0f)
                    .nodes

            val validationIdentifierForMeasure =
                ValidationIdentifierForMeasure(2, ValidationIdentifierForTest(this))
            val validationIdentifierForNote =
                ValidationIdentifierForNote(3, validationIdentifierForMeasure)

            val result =
                markInvalidThings.markInvalidNotesAndMeasuresRed(
                    validationIdentifierForNote,
                    graphicProperties,
                    pathData,
                )

            val expectedErrorMarkingPathData: List<PathNode> =
                PathBuilder().moveTo(x = 1237.5f, y = 400.0f).horizontalLineTo(x = 1737.5f).nodes

            result shouldBe expectedErrorMarkingPathData
        }

        "markInvalidNotesAndMeasuresRed should return something when no ValidationIdentifierForMeasure in the hierarchy" {
            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = 87.5f, y = 650.0f)
                    .lineTo(x = 337.5f, y = 650.0f)
                    .moveTo(x = 337.5f, y = 700.0f)
                    .lineTo(x = 587.5f, y = 700.0f)
                    .moveTo(x = 662.5f, y = 650.0f)
                    .lineTo(x = 787.5f, y = 650.0f)
                    .moveTo(x = 787.5f, y = 650.0f)
                    .lineTo(x = 850.0f, y = 650.0f)
                    .moveTo(x = 850.0f, y = 650.0f)
                    .lineTo(x = 912.5f, y = 650.0f)
                    .moveTo(x = 912.5f, y = 775.0f)
                    .lineTo(x = 1162.5f, y = 775.0f)
                    .moveTo(x = 1237.5f, y = 650.0f)
                    .lineTo(x = 1362.5f, y = 650.0f)
                    .moveTo(x = 1362.5f, y = 650.0f)
                    .lineTo(x = 1487.5f, y = 650.0f)
                    .moveTo(x = 1487.5f, y = 600.0f)
                    .lineTo(x = 1612.5f, y = 600.0f)
                    .moveTo(x = 1612.5f, y = 600.0f)
                    .lineTo(x = 2312.5f, y = 600.0f) // This note duration is too long.
                    .moveTo(x = 1812.5f, y = 575.0f)
                    .lineTo(x = 1875.0f, y = 575.0f)
                    .moveTo(x = 1875.0f, y = 575.0f)
                    .lineTo(x = 1937.5f, y = 575.0f)
                    .moveTo(x = 1937.5f, y = 575.0f)
                    .lineTo(x = 2000.0f, y = 575.0f)
                    .moveTo(x = 2000.0f, y = 575.0f)
                    .lineTo(x = 2062.5f, y = 575.0f)
                    .moveTo(x = 2062.5f, y = 600.0f)
                    .lineTo(x = 2312.5f, y = 600.0f)
                    .nodes

            val validationIdentifierForNote =
                ValidationIdentifierForNote(3, ValidationIdentifierForTest(this))

            val result =
                markInvalidThings.markInvalidNotesAndMeasuresRed(
                    validationIdentifierForNote,
                    graphicProperties,
                    pathData,
                )

            result.shouldBeEmpty()
        }
    })
