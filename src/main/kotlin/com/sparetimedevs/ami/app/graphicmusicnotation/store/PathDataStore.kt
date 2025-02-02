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

package com.sparetimedevs.ami.app.graphicmusicnotation.store

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.vector.PathNode
import com.sparetimedevs.ami.graphic.GraphicProperties
import com.sparetimedevs.ami.music.data.kotlin.note.NoteDuration
import com.sparetimedevs.ami.music.data.kotlin.note.NoteValue

class PathDataStore(
    private val graphicProperties: GraphicProperties,
) {
    private val pathData = mutableStateListOf<PathNode>()

    private val errorMarkingPathData = mutableStateListOf<PathNode>()

    fun getPathData(): List<PathNode> = pathData

    fun getGraphicProperties(): GraphicProperties = graphicProperties

    fun addToPathData(nonnormalizedPathNode: PathNode): List<PathNode> {
        // Normalize the data before adding.
        // Might need to add more normalization to this function.
        // For instance making sure it is a straight line from start to end, from left to right.

        val pathNode = correctPathNodeOnXAxes(nonnormalizedPathNode)
        if (isStartOfNewNote(pathNode)) {
            if (pathData.isEmpty()) {
                pathData.add(pathNode)
            } else if (isStartOfNewNote(pathData.last())) {
                pathData.replaceLast(pathNode)
            } else {
                pathData.add(pathNode)
            }
        } else {
            if (pathData.isEmpty()) {
                // Do nothing in this case.
                // It is not a start of a new note, so should not add it as first item.
            } else if (isStartOfNewNote(pathData.last())) {
                pathData.add(pathNode)
            } else {
                pathData.replaceLast(pathNode)
            }
        }

        return pathData
    }

    private fun correctPathNodeOnXAxes(pathNode: PathNode): PathNode =
        when (pathNode) {
            is PathNode.ArcTo -> TODO()
            is PathNode.Close -> TODO()
            is PathNode.CurveTo -> TODO()
            is PathNode.HorizontalTo -> pathNode.copy(x = correctPathNodeOnXAxes(pathNode.x))
            is PathNode.LineTo -> TODO()
            is PathNode.MoveTo -> pathNode.copy(x = correctPathNodeOnXAxes(pathNode.x))
            is PathNode.QuadTo -> TODO()
            is PathNode.ReflectiveCurveTo -> TODO()
            is PathNode.ReflectiveQuadTo -> TODO()
            is PathNode.RelativeArcTo -> TODO()
            is PathNode.RelativeCurveTo -> TODO()
            is PathNode.RelativeHorizontalTo -> TODO()
            is PathNode.RelativeLineTo -> TODO()
            is PathNode.RelativeMoveTo -> TODO()
            is PathNode.RelativeQuadTo -> TODO()
            is PathNode.RelativeReflectiveCurveTo -> TODO()
            is PathNode.RelativeReflectiveQuadTo -> TODO()
            is PathNode.RelativeVerticalTo -> TODO()
            is PathNode.VerticalTo -> TODO()
        }

    private fun correctPathNodeOnXAxes(x: Float): Float =
        if (x <= graphicProperties.offsetX) {
            graphicProperties.offsetX.toFloat()
        } else if (
            x > graphicProperties.offsetX && x <= calculatePointOnXAxes(graphicProperties, 1, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 1, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 1, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 2, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 2, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 2, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 3, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 3, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 3, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 4, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 4, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 4, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 5, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 5, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 5, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 6, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 6, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 6, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 7, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 7, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 7, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 8, 0)
        ) {
            calculatePointOnXAxes(graphicProperties, 8, 0)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 8, 0) &&
            x <= calculatePointOnXAxes(graphicProperties, 8, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 8, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 8, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 9, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 9, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 9, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 10, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 10, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 10, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 11, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 11, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 11, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 12, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 12, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 12, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 13, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 13, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 13, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 14, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 14, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 14, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 15, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 15, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 15, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 16, 1)
        ) {
            calculatePointOnXAxes(graphicProperties, 16, 1)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 16, 1) &&
            x <= calculatePointOnXAxes(graphicProperties, 16, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 16, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 16, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 17, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 17, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 17, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 18, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 18, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 18, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 19, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 19, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 19, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 20, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 20, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 20, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 21, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 21, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 21, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 22, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 22, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 22, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 23, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 23, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 23, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 24, 2)
        ) {
            calculatePointOnXAxes(graphicProperties, 24, 2)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 24, 2) &&
            x <= calculatePointOnXAxes(graphicProperties, 24, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 24, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 24, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 25, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 25, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 25, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 26, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 26, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 26, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 27, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 27, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 27, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 28, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 28, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 28, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 29, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 29, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 29, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 30, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 30, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 30, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 31, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 31, 3)
        } else if (
            x > calculatePointOnXAxes(graphicProperties, 31, 3) &&
            x <= calculatePointOnXAxes(graphicProperties, 32, 3)
        ) {
            calculatePointOnXAxes(graphicProperties, 32, 3)
        } else {
            (
                graphicProperties.offsetX +
                    graphicProperties.measureWidth +
                    graphicProperties.spaceBetweenMeasures +
                    graphicProperties.measureWidth +
                    graphicProperties.spaceBetweenMeasures +
                    graphicProperties.measureWidth +
                    graphicProperties.spaceBetweenMeasures +
                    graphicProperties.measureWidth
            ).toFloat()
        }

    private fun calculatePointOnXAxes(
        graphicProperties: GraphicProperties,
        noteDurationMultiplier: Int,
        spaceBetweenMeasuresMultiplier: Int,
    ): Float =
        (
            (
                graphicProperties.offsetX +
                    graphicProperties.measureWidth *
                    NoteDuration(NoteValue._8TH).value *
                    noteDurationMultiplier
            ) +
                (graphicProperties.spaceBetweenMeasures * spaceBetweenMeasuresMultiplier)
        ).toFloat()

    private fun isStartOfNewNote(pathNode: PathNode): Boolean =
        when (pathNode) {
            is PathNode.MoveTo -> true
            else -> false
        }

    private fun SnapshotStateList<PathNode>.replaceLast(pathNode: PathNode): Boolean {
        this.removeLastIfExists()
        return this.add(pathNode)
    }

    private fun SnapshotStateList<PathNode>.removeLastIfExists() {
        if (this.isNotEmpty()) {
            this.removeAt(this.size - 1)
        }
    }

    fun undoLastCreatedLine(): List<PathNode> {
        // TODO is this correct? Or should it be something else?
        pathData.removeLastIfExists()
        pathData.removeLastIfExists()
        return pathData
    }

    fun replacePathData(data: List<PathNode>): List<PathNode> {
        pathData.clear()
        errorMarkingPathData.clear()
        pathData.addAll(data)
        return pathData
    }

    fun getErrorMarkingPathData(): List<PathNode> = errorMarkingPathData

    fun addToErrorMarkingPathData(pathData: List<PathNode>): List<PathNode> {
        errorMarkingPathData.addAll(pathData)
        return errorMarkingPathData
    }
}
