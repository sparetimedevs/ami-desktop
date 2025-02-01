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

package com.sparetimedevs.ami.app.graphicmusicnotation.store

import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathNode
import com.sparetimedevs.ami.graphic.GraphicProperties
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PathDataStoreTest :
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
        val pathDataStore = PathDataStore(graphicProperties)

        "addToPathData should use the closest suitable point on the X axes" {
            // A suitable point on the X axes means to have a correct note duration.
            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = 87.5f, y = 550.0f)
                    .horizontalLineTo(x = 587.5f)
                    .moveTo(x = 662.5f, y = 575.0f)
                    .nodes

            pathDataStore.replacePathData(pathData)

            val pathNode: PathNode = PathBuilder().horizontalLineTo(x = 1157.9323f).nodes.first()

            val result: List<PathNode> = pathDataStore.addToPathData(pathNode)

            result shouldBe
                PathBuilder()
                    .moveTo(x = 87.5f, y = 550.0f)
                    .horizontalLineTo(x = 587.5f)
                    .moveTo(x = 662.5f, y = 575.0f)
                    .horizontalLineTo(x = 1162.5f)
                    .nodes
        }

        "addToPathData should use only two points to describe one note" {
            val pathData: List<PathNode> =
                PathBuilder()
                    .moveTo(x = 87.5f, y = 550.0f)
                    .horizontalLineTo(x = 587.5f)
                    .moveTo(x = 662.5f, y = 575.0f)
                    .horizontalLineTo(x = 912.5f)
                    .nodes

            pathDataStore.replacePathData(pathData)

            val pathNode: PathNode = PathBuilder().horizontalLineTo(x = 1162.5f).nodes.first()

            val result: List<PathNode> = pathDataStore.addToPathData(pathNode)

            result shouldBe
                PathBuilder()
                    .moveTo(x = 87.5f, y = 550.0f)
                    .horizontalLineTo(x = 587.5f)
                    .moveTo(x = 662.5f, y = 575.0f)
                    // There is no `.horizontalLineTo(x = 912.5f)`
                    .horizontalLineTo(x = 1162.5f)
                    .nodes
        }
    })
