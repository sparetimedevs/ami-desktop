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

package com.sparetimedevs.ami.app.graphicmusicnotation

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.value.Value
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.ScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.DrawGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.place.PlaceGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.read.ReadGraphicMusicNotationComponent

interface GraphicMusicNotationMultiPaneComponent {

    val children: Value<Children>

    data class Children(
        val topAppBarDetailsChild: Child.Created<*, ScoreComponents>,
        val drawAreaChild: Child.Created<*, DrawGraphicMusicNotationComponent>,
        val readAreaChild: Child.Created<*, ReadGraphicMusicNotationComponent>,
        val placeAreaChild: Child.Created<*, PlaceGraphicMusicNotationComponent>,
    )
}

data class ScoreComponents(
    val scoreCoreComponent: MusicScoreDetailsComponent,
    val scoreDetailsComponent: ScoreDetailsComponent
)
