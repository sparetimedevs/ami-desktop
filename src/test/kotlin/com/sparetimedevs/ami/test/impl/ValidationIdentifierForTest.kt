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

package com.sparetimedevs.ami.test.impl

import arrow.core.EitherNel
import arrow.core.nel
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.sparetimedevs.ami.core.Id
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.core.validation.getOrThrowFirstValidationError
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import io.kotest.core.test.TestScope

@JvmInline
value class TestId private constructor(val value: String) : Id {
    companion object {

        fun validate(
            input: String,
            validationIdentifier: ValidationIdentifier = NoValidationIdentifier,
        ): EitherNel<ValidationError, TestId> = either {
            ensure(input.isNotEmpty()) {
                ValidationError(
                        "Test ID can't be empty, the input was $input",
                        validationErrorForProperty<TestId>(),
                        validationIdentifier,
                    )
                    .nel()
            }
            TestId(input)
        }

        fun unsafeCreate(input: String): TestId =
            validate(input, NoValidationIdentifier).getOrThrowFirstValidationError()
    }
}

data class ValidationIdentifierForTest(val testScope: TestScope) : ValidationIdentifier {
    override val identifier: Id = TestId.unsafeCreate(testScope.testCase.name.originalName)
    override val validationIdentifierParent: ValidationIdentifier = NoValidationIdentifier
}