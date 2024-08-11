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

import com.arkivanov.decompose.value.Value
import com.sparetimedevs.ami.core.validation.ValidationErrorForProperty

interface ScoreDetailsComponent {

    val scoreIdValue: Value<String>

    val scoreTitleValue: Value<String>

    val partIdsValue: Value<List<String>>

    val mappedValidationErrorsValue: Value<Map<ValidationErrorForProperty, String>>

    fun updateScoreId(newValue: String)

    fun updateScoreTitle(newValue: String)

    fun saveScoreDetails(): Unit
}
