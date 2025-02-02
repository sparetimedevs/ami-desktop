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

import com.sparetimedevs.ami.core.Id
import com.sparetimedevs.ami.core.IdOrIndex
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForMeasure
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForNote
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForPart
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForScore
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValidationIdentifierUtilsTest :
    StringSpec({
        "containsValidationIdentifierForPart should return true " +
            "when there is a ValidationIdentifierForPart with the given partId in the hierarchy" {
                val scoreId = ScoreId.unsafeCreate("s-1")
                val partId = PartId.unsafeCreate("p-1")
                val validationIdentifierForScore =
                    ValidationIdentifierForScore(scoreId, NoValidationIdentifier)
                val validationIdentifierForPart =
                    ValidationIdentifierForPart(partId, validationIdentifierForScore)
                val validationIdentifierForMeasure =
                    ValidationIdentifierForMeasure(2, validationIdentifierForPart)
                val validationIdentifierForNote =
                    ValidationIdentifierForNote(3, validationIdentifierForMeasure)

                val result = containsValidationIdentifierForPart(validationIdentifierForNote, partId)

                result shouldBe true
            }

        "containsValidationIdentifierForPart should return false " +
            "when there is no ValidationIdentifierForPart with the given partId in the hierarchy" {
                val scoreId = ScoreId.unsafeCreate("s-1")
                val partId = PartId.unsafeCreate("p-1")
                val validationIdentifierForScore =
                    ValidationIdentifierForScore(scoreId, NoValidationIdentifier)
                val validationIdentifierForPart =
                    ValidationIdentifierForPart(partId, validationIdentifierForScore)
                val validationIdentifierForMeasure =
                    ValidationIdentifierForMeasure(2, validationIdentifierForPart)
                val validationIdentifierForNote =
                    ValidationIdentifierForNote(3, validationIdentifierForMeasure)

                val result =
                    containsValidationIdentifierForPart(
                        validationIdentifierForNote,
                        PartId.unsafeCreate("p-2"),
                    )

                result shouldBe false
            }

        "containsValidationIdentifierForPart should return false " +
            "when there is no ValidationIdentifierForPart and there is no NoValidationIdentifier in the hierarchy" {
                val validationIdentifierForMeasure =
                    ValidationIdentifierForMeasure(2, NoValidationIdentifierForTest)
                val validationIdentifierForNote =
                    ValidationIdentifierForNote(3, validationIdentifierForMeasure)

                val result =
                    containsValidationIdentifierForPart(
                        validationIdentifierForNote,
                        PartId.unsafeCreate("p-1"),
                    )

                result shouldBe false
            }
    })

object NoValidationIdentifierForTest : ValidationIdentifier {
    override val identifier: IdOrIndex = NoIdForTest
    override val validationIdentifierParent: ValidationIdentifier = this
}

data object NoIdForTest : Id
