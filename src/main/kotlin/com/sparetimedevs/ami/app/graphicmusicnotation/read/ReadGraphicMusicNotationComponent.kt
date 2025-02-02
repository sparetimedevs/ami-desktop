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

package com.sparetimedevs.ami.app.graphicmusicnotation.read

import androidx.compose.ui.graphics.vector.PathNode
import com.arkivanov.decompose.ComponentContext
import com.sparetimedevs.ami.app.graphicmusicnotation.store.PathDataStore

interface ReadGraphicMusicNotationComponent {
    fun getLineThickness(): Float

    fun getPathData(): List<PathNode>
}

class DefaultReadGraphicMusicNotationComponent(
    componentContext: ComponentContext,
    private val pathDataStore: PathDataStore,
) : ReadGraphicMusicNotationComponent,
    ComponentContext by componentContext {
    override fun getLineThickness(): Float = THICKNESS_OF_LINES

    override fun getPathData(): List<PathNode> = pathDataStore.getPathData()

    companion object {
        private const val THICKNESS_OF_LINES = 25.0f
    }
}
