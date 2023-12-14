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
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.ChildNavState
import com.arkivanov.decompose.router.children.ChildNavState.Status
import com.arkivanov.decompose.router.children.NavState
import com.arkivanov.decompose.router.children.SimpleChildNavState
import com.arkivanov.decompose.router.children.SimpleNavigation
import com.arkivanov.decompose.router.children.children
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.sparetimedevs.ami.app.graphicmusicnotation.GraphicMusicNotationMultiPaneComponent.Children
import com.sparetimedevs.ami.app.graphicmusicnotation.details.DefaultMusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.drawing.DefaultDrawingGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.drawing.DrawingGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.repository.PathDataRepositoryImpl
import com.sparetimedevs.ami.graphic.GraphicProperties

internal class DefaultGraphicMusicNotationMultiPaneComponent(componentContext: ComponentContext) :
    GraphicMusicNotationMultiPaneComponent, ComponentContext by componentContext {

    private val navigation = SimpleNavigation<(NavigationState) -> NavigationState>()
    private val navState = BehaviorSubject<NavigationState?>(null)

    private val graphicProperties =
        GraphicProperties(
            offsetX = 87.5,
            offsetY = 400.0,
            measureWidth = 500.0,
            spaceBetweenMeasures = 75.0,
            cutOffXToReflectNoteIsEnding = 0.0,
            wholeStepExpressedInY = 50.0
        )
    private val pathDataRepository = PathDataRepositoryImpl(graphicProperties)

    override val children: Value<Children> =
        children(
            source = navigation,
            key = "children",
            initialState = DefaultGraphicMusicNotationMultiPaneComponent::NavigationState,
            navTransformer = { navState, event -> event(navState) },
            stateMapper = { navState, children ->
                @Suppress("UNCHECKED_CAST")
                Children(
                    topAppBarDetailsChild =
                        children.find { it.instance is MusicScoreDetailsComponent }
                            as Child.Created<*, MusicScoreDetailsComponent>,
                    drawingAreaChild =
                        children.find { it.instance is DrawingGraphicMusicNotationComponent }
                            as Child.Created<*, DrawingGraphicMusicNotationComponent>,
                )
            },
            onStateChanged = { newState, _ -> navState.onNext(newState) },
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): Any =
        when (config) {
            is Config.TopAppBarDetailsPane -> topAppBarDetailsComponent(componentContext)
            is Config.DrawingPane -> drawingGraphicMusicNotationComponent(componentContext)
        }

    private fun topAppBarDetailsComponent(
        componentContext: ComponentContext
    ): MusicScoreDetailsComponent =
        DefaultMusicScoreDetailsComponent(
            componentContext = componentContext,
            pathDataRepository = pathDataRepository
        )

    private fun drawingGraphicMusicNotationComponent(
        componentContext: ComponentContext
    ): DrawingGraphicMusicNotationComponent =
        DefaultDrawingGraphicMusicNotationComponent(
            componentContext = componentContext,
            pathDataRepository = pathDataRepository
        )

    private sealed interface Config : Parcelable {

        @Parcelize object TopAppBarDetailsPane : Config

        @Parcelize object DrawingPane : Config
    }

    @Parcelize
    private data class NavigationState(val placeholder: String = "placeholder") :
        NavState<Config>, Parcelable {
        override val children: List<ChildNavState<Config>>
            get() =
                listOfNotNull(
                    SimpleChildNavState(Config.TopAppBarDetailsPane, Status.ACTIVE),
                    SimpleChildNavState(Config.DrawingPane, Status.ACTIVE)
                )
    }
}
