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
import androidx.compose.ui.test.assertTextEquals
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
        "Clicking the play button should play the score" {
            runComposeUiTest {
                val getScore: suspend () -> Either<DomainError, Score> = suspend {
                    getExampleScore().right()
                }

                val job = SupervisorJob()
                val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
                val playerSettings = PlayerSettings()
                val player = TestPlayer(playerSettings)

                setContent {
                    var playerContext by remember {
                        mutableStateOf(PlayerContext(playerCoroutineScope = coroutineScope))
                    }

                    PlayPauseMidiPlayerButton(getScore, playerContext, player) { context ->
                        playerContext = context
                    }
                }

                onNodeWithTag("play-pause-midi-player-button").assertTextEquals("Play")
                onNodeWithTag("play-pause-midi-player-button").performClick()
                onNodeWithTag("play-pause-midi-player-button").assertTextEquals("Pause")

                // The TestPlayer plays music way faster than a real player, still, we need to wait
                // a bit before the TestPlayer is done "playing".
                runBlocking { delay(5000L) }

                val isCompletedJob = job.complete()
                isCompletedJob shouldBe true

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
            }
        }
    })
