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

package com.sparetimedevs.ami.player

import com.sparetimedevs.ami.music.data.kotlin.score.Score
import java.time.LocalDateTime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

suspend fun play(score: Score, player: Player): Unit = coroutineScope {
    try {
        // score.parts[0] can throw `java.lang.IndexOutOfBoundsException: Empty list doesn't
        // contain
        // element at index 0`
        // It would be good to avoid it.
        player.play(score.parts[0].measures, LocalDateTime.now())
        while (this.isActive) {
            // Do nothing, just keep going while the coroutine is active.
            // Do add a delay, else this while loop keeps on going too fast.
            delay(500L)
        }
    } finally {
        println("Stopping player...")
        runBlocking { player.stop() }
    }
}
