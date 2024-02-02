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

package com.sparetimedevs.ami.app.graphicmusicnotation.draw

import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepositoryImpl
import com.sparetimedevs.ami.getTestComponentContext
import com.sparetimedevs.ami.graphic.GraphicProperties
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DefaultDrawGraphicMusicNotationComponentTest :
    StringSpec({
        val testComponentContext: ComponentContext = getTestComponentContext()
        val graphicProperties =
            GraphicProperties(
                offsetX = 87.5,
                offsetY = 400.0,
                measureWidth = 500.0,
                spaceBetweenMeasures = 75.0,
                cutOffXToReflectNoteIsEnding = 0.0,
                wholeStepExpressedInY = 50.0
            )
        val pathDataRepository = PathDataRepositoryImpl(graphicProperties)
        val component: DrawGraphicMusicNotationComponent =
            DefaultDrawGraphicMusicNotationComponent(testComponentContext, pathDataRepository)

        "getLineThickness" { component.getLineThickness() shouldBe 25f }

        "getPathData" { component.getPathData() shouldBe mutableListOf() }

        "addToPathData" {
            val pathNode: PathNode = PathBuilder().moveTo(212.5f, 450f).getNodes().first()

            val result = component.addToPathData(pathNode)

            result shouldBe listOf(pathNode)
        }

        "undoLastCreatedLine" {
            val pathNode: PathNode = PathBuilder().moveTo(212.5f, 450f).getNodes().first()

            val stage1 = component.addToPathData(pathNode)

            val thisIsNoResult = component.undoLastCreatedLine()

            val result = component.getPathData()

            // assert something

        }

        "getBackdrop" {
            val result: List<TheLineThatRepresentsAPitch> = component.getBackdrop()

            result shouldBe result
        }
    })
