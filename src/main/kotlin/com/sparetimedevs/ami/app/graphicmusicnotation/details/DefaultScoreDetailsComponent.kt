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

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.sparetimedevs.ami.app.utils.disposableScope

internal class DefaultScoreDetailsComponent(
    componentContext: ComponentContext,
    private val scoreCoreComponent: MusicScoreDetailsComponent
) :
    ScoreDetailsComponent,
    ComponentContext by componentContext,
    DisposableScope by componentContext.disposableScope() {

    private val _scoreIdValue = MutableValue("")
    override val scoreIdValue: Value<String> = _scoreIdValue

    private val _scoreTitleValue = MutableValue("")
    override val scoreTitleValue: Value<String> = _scoreTitleValue

    override fun updateScoreId(newValue: String) {
        _scoreIdValue.update { newValue }
    }

    override fun updateScoreTitle(newValue: String) {
        _scoreTitleValue.update { newValue }
    }

    override fun saveScoreDetails(): Unit {
        // TODO actually some other method should be invoked.
        scoreCoreComponent.onSaveScoreClicked()
    }
}
