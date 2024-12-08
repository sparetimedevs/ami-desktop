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
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MarkInvalidThings
import com.sparetimedevs.ami.app.graphicmusicnotation.details.MusicScoreDetailsComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.DefaultDrawGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.draw.DrawGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.place.DefaultPlaceGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.place.PlaceGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.read.DefaultReadGraphicMusicNotationComponent
import com.sparetimedevs.ami.app.graphicmusicnotation.read.ReadGraphicMusicNotationComponent
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
    private val markInvalidThings = MarkInvalidThings()

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
                    drawAreaChild =
                        children.find { it.instance is DrawGraphicMusicNotationComponent }
                            as Child.Created<*, DrawGraphicMusicNotationComponent>,
                    readAreaChild =
                        children.find { it.instance is ReadGraphicMusicNotationComponent }
                            as Child.Created<*, ReadGraphicMusicNotationComponent>,
                    placeAreaChild =
                        children.find { it.instance is PlaceGraphicMusicNotationComponent }
                            as Child.Created<*, PlaceGraphicMusicNotationComponent>,
                )
            },
            onStateChanged = { newState, _ -> navState.onNext(newState) },
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): Any =
        when (config) {
            is Config.TopAppBarDetailsPane -> topAppBarDetailsComponent(componentContext)
            is Config.DrawPane -> drawingGraphicMusicNotationComponent(componentContext)
            is Config.ReadPane -> readGraphicMusicNotationComponent(componentContext)
            is Config.PlacePane -> placeGraphicMusicNotationComponent(componentContext)
        }

    private fun topAppBarDetailsComponent(
        componentContext: ComponentContext
    ): MusicScoreDetailsComponent =
        DefaultMusicScoreDetailsComponent(
            componentContext = componentContext,
            pathDataRepository = pathDataRepository,
            markInvalidThings = markInvalidThings
        )

    private fun drawingGraphicMusicNotationComponent(
        componentContext: ComponentContext
    ): DrawGraphicMusicNotationComponent =
        DefaultDrawGraphicMusicNotationComponent(
            componentContext = componentContext,
            pathDataRepository = pathDataRepository
        )

    private fun readGraphicMusicNotationComponent(
        componentContext: ComponentContext
    ): ReadGraphicMusicNotationComponent =
        DefaultReadGraphicMusicNotationComponent(
            componentContext = componentContext,
            pathDataRepository = pathDataRepository
        )

    private fun placeGraphicMusicNotationComponent(
        componentContext: ComponentContext
    ): PlaceGraphicMusicNotationComponent =
        DefaultPlaceGraphicMusicNotationComponent(
            componentContext = componentContext,
            pathDataRepository = pathDataRepository
        )

    private sealed interface Config : Parcelable {

        @Parcelize object TopAppBarDetailsPane : Config

        @Parcelize object DrawPane : Config

        @Parcelize object ReadPane : Config

        @Parcelize object PlacePane : Config
    }

    @Parcelize
    private data class NavigationState(val placeholder: String = "placeholder") :
        NavState<Config>, Parcelable {
        override val children: List<ChildNavState<Config>>
            get() =
                listOfNotNull(
                    SimpleChildNavState(Config.TopAppBarDetailsPane, Status.ACTIVE),
                    SimpleChildNavState(Config.DrawPane, Status.ACTIVE),
                    SimpleChildNavState(Config.ReadPane, Status.ACTIVE),
                    SimpleChildNavState(Config.PlacePane, Status.ACTIVE)
                )
    }
}
