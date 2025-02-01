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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import arrow.core.EitherNel
import com.arkivanov.decompose.value.Value
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.score.Score

interface ScoreDetailsComponent {
    val scoreValue: Value<Score>

    fun createNewPart()

    fun saveScoreDetails(
        scoreId: String,
        scoreTitle: String?,
        partIds: Map<PartId, String>,
        partNames: Map<PartId, String?>,
        partInstrumentNames: Map<PartId, String?>,
        partInstrumentMidiChannels: Map<PartId, String?>,
        partInstrumentMidiPrograms: Map<PartId, String?>,
    ): EitherNel<ValidationError, Unit>
}
