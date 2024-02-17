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

package com.sparetimedevs.ami.core

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

class ListReplaceKtTest :
    StringSpec({
        "replace should return a new list with items to replace first followed by original items when there are more original items than items to replace" {
            val original: List<String> = listOf("a1", "b1", "c1", "d1", "e1", "f1")
            val newItems: List<String> = listOf("a2", "b2", "c2")

            val result: List<String> = original.replace(newItems)

            result shouldBe listOf("a2", "b2", "c2", "d1", "e1", "f1")
        }

        "replace should return the new items list when the items to replace are more than the amount of items in original list" {
            val original: List<String> = listOf("a1", "b1", "c1")
            val newItems: List<String> = listOf("a2", "b2", "c2", "d2", "e2", "f2")

            val result: List<String> = original.replace(newItems)

            result shouldBeSameInstanceAs newItems
        }

        "replace should return the new items list when the items to replace are the same amount as the amount of items in original list" {
            val original: List<String> = listOf("a1", "b1", "c1")
            val newItems: List<String> = listOf("a2", "b2", "c2")

            val result: List<String> = original.replace(newItems)

            result shouldBeSameInstanceAs newItems
        }

        "replace should return the empty new items list when the original list is empty and the items to replace is empty" {
            val original: List<String> = emptyList()
            val newItems: List<String> = emptyList()

            val result: List<String> = original.replace(newItems)

            result shouldBeSameInstanceAs newItems
        }

        "replace should return the original list when the original list is not empty and the items to replace is empty" {
            val original: List<String> = listOf("a1", "b1", "c1", "d1", "e1", "f1")
            val newItems: List<String> = emptyList()

            val result: List<String> = original.replace(newItems)

            result shouldBe original
            // Not the same instance as the original list, because the implementation joins the
            // original list with an empty list.
            result shouldNotBeSameInstanceAs original
        }

        "replace should return the new items list when the items to replace is not empty and the original list is empty" {
            val original: List<String> = emptyList()
            val newItems: List<String> = listOf("a2", "b2", "c2", "d2", "e2", "f2")

            val result: List<String> = original.replace(newItems)

            result shouldBeSameInstanceAs newItems
        }
    })
