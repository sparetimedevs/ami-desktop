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

package com.sparetimedevs.ami.music.core

import com.sparetimedevs.ami.core.replace
import com.sparetimedevs.ami.music.data.kotlin.measure.Measure
import com.sparetimedevs.ami.music.data.kotlin.score.Score

fun replaceMeasuresInScore(measures: List<Measure>, score: Score): Score {
    if (measures.isEmpty()) return score
    val parts = score.parts
    val newPlusOriginal = parts[0].measures.replace(measures)
    val newPart = parts[0].copy(measures = newPlusOriginal)
    val newParts = parts.replace(listOf(newPart))
    return score.copy(parts = newParts)
}
