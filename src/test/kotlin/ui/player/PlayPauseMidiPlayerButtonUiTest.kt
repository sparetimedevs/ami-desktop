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

package ui.player

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import arrow.core.Either
import arrow.core.right
import com.sparetimedevs.ami.app.player.PlayPauseMidiPlayerButton
import com.sparetimedevs.ami.app.player.PlayerState
import com.sparetimedevs.ami.core.DomainError
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.example.getExampleScoreFrereJacques
import com.sparetimedevs.ami.test.helper.createTestPlayerContext
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalTestApi::class)
class PlayPauseMidiPlayerButtonUiTest :
    StringSpec({
        "TODO test case one12321213" {
            runComposeUiTest {
                val getScore: suspend () -> Either<DomainError, Score> = suspend {
                    getExampleScoreFrereJacques().right()
                }
                var playerContext = createTestPlayerContext()

                setContent {
                    PlayPauseMidiPlayerButton(getScore, playerContext) { context ->
                        playerContext = context
                    }
                }

                playerContext.playerState shouldBe PlayerState.PAUSED

                onNodeWithTag("play-pause-midi-player-button").performClick()

                playerContext.playerState shouldBe PlayerState.PLAY
            }
        }
    })
