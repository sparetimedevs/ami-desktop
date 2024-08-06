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

package com.sparetimedevs.ami.app.graphicmusicnotation.place

import androidx.compose.ui.graphics.vector.PathNode
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.store.PathDataStore

interface PlaceGraphicMusicNotationComponent {

    fun getLineThickness(): Float

    fun getPathData(): List<PathNode>
}

class DefaultPlaceGraphicMusicNotationComponent(
    componentContext: ComponentContext,
    private val pathDataStore: PathDataStore,
) : PlaceGraphicMusicNotationComponent, ComponentContext by componentContext {

    override fun getLineThickness(): Float = THICKNESS_OF_LINES

    override fun getPathData(): List<PathNode> = pathDataStore.getPathData()

    companion object {
        private const val THICKNESS_OF_LINES = 1.0f
    }
}
