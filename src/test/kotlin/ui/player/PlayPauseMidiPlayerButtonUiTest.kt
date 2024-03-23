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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import arrow.core.Either
import arrow.core.right
import com.sparetimedevs.ami.app.player.PlayPauseMidiPlayerButton
import com.sparetimedevs.ami.core.DomainError
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import com.sparetimedevs.ami.music.example.getExampleScore
import com.sparetimedevs.ami.player.PlayerContext
import com.sparetimedevs.ami.player.PlayerSettings
import com.sparetimedevs.ami.test.impl.TestPlayer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
class PlayPauseMidiPlayerButtonUiTest :
    StringSpec({
        "TODO test case one12321213" {
            runComposeUiTest {
                val getScore: suspend () -> Either<DomainError, Score> = suspend {
                    getExampleScore().right()
                }
                //                var getPlayerContext = createTestPlayerContext()

                val job = SupervisorJob()
                val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
                val playerSettings = PlayerSettings()
                val player = TestPlayer()

                setContent {
                    var playerContext by remember {
                        mutableStateOf(
                            PlayerContext(
                                playerCoroutineScope = coroutineScope,
                                playerSettings = playerSettings
                            )
                        )
                    }
                    //                    getPlayerContext = playerContext

                    PlayPauseMidiPlayerButton(getScore, playerContext, player) { context ->
                        playerContext = context
                    }
                }

                //                getPlayerContext.playerState shouldBe PlayerState.PAUSED
                // TODO we can read text on the button! it should read start when paused and pause
                // when playing.
                runBlocking { delay(5000L) }
                onNodeWithTag("play-pause-midi-player-button").performClick()
                runBlocking { delay(5000L) }
                onNodeWithTag("play-pause-midi-player-button").performClick()

                //                getPlayerContext.playerState shouldBe PlayerState.PLAY

                runBlocking { delay(20000L) }
                // stop the thingy
                val f = job.complete()
                f shouldBe true
                //                onNodeWithTag("play-pause-midi-player-button").performClick()
                //                getPlayerContext.playerState shouldBe PlayerState.PLAY
                val expectedNotesPlayed =
                    listOf(
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=C, octave=Octave(value=4), alter=Semitones(value=0.0)))",
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=D, octave=Octave(value=4), alter=Semitones(value=0.0)))",
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=E, octave=Octave(value=4), alter=Semitones(value=0.0)))",
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=F, octave=Octave(value=4), alter=Semitones(value=0.0)))",
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=G, octave=Octave(value=4), alter=Semitones(value=0.0)))",
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=A, octave=Octave(value=5), alter=Semitones(value=0.0)))",
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=B, octave=Octave(value=5), alter=Semitones(value=0.0)))",
                        "Playing Pitched(duration=NoteDuration(noteValue=QUARTER, modifier=NONE), noteAttributes=NoteAttributes(attack=null, dynamics=null, endDynamics=null, release=null), pitch=Pitch(noteName=C, octave=Octave(value=5), alter=Semitones(value=0.0)))"
                    )
                player.notesPlayed() shouldBe expectedNotesPlayed

                //                    eventually(5.seconds) { player.notesPlayed() shouldBe
                // expectedNotesPlayed }
            }
        }
    })
