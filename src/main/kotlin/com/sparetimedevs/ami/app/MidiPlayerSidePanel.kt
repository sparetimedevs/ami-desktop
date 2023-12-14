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

package com.sparetimedevs.ami.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Either
import com.sparetimedevs.ami.app.player.PlayPauseMidiPlayerButton
import com.sparetimedevs.ami.app.player.PlayerState
import com.sparetimedevs.ami.core.DomainError
import com.sparetimedevs.ami.midi.MidiPlayerSettings
import com.sparetimedevs.ami.music.data.kotlin.score.Score
import kotlinx.coroutines.Job

@Composable
fun StepSlider(
    initialValue: Int,
    minValue: Int,
    maxValue: Int,
    label: String,
    onChange: (Int) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(modifier = Modifier.padding(10.dp)) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = "%d".format(value.toInt()), fontSize = 14.sp)
        }
        Slider(
            value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = {
                value = it
                onChange(value.toInt())
            },
            onValueChangeFinished = { onChange(value.toInt()) }
        )
    }
}

val padding = Modifier.padding(10.dp)

@Composable
fun PropertySlider(
    initialValue: Double,
    minValue: Double,
    maxValue: Double,
    label: String,
    onChange: (Double) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(modifier = padding) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = "%.2f".format(value), fontSize = 14.sp)
        }
        Slider(
            value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = {
                value = it
                onChange(value.toDouble())
            },
            onValueChangeFinished = { onChange(value.toDouble()) }
        )
    }
}

const val MIN_RANDOM = 0.1
const val MAX_RANDOM = 50.0
const val MIN_ORBIT_RADIUS = 100.00
const val MAX_ORBIT_RADIUS = 600.00
const val MIN_INNER_RADIUS = 100.00
const val MAX_INNER_RADIUS = 200.00
const val MIN_OUTER_RADIUS = 200.00
const val MAX_OUTER_RADIUS = 300.00
const val MIN_STEPS = 1
const val MAX_STEPS = 360

data class DeprecatedSettings(
    val orbitRadius: Double = 300.0,
    val innerCirclesRadius: Double = MIN_INNER_RADIUS,
    val outerCirclesRadius: Double = MIN_OUTER_RADIUS,
    val randomCoefficient: Double = MIN_RANDOM,
    val stepsInnerOrbit: Int = 3,
    val stepsOuterOrbit: Int = 5,
    val drawInnerOrbit: Boolean = true,
    val drawOuterOrbit: Boolean = true,
)

data class PlayerContext(
    val playerJob: Job = Job(),
    val playerState: PlayerState = PlayerState.PAUSED,
    val playerSettings: MidiPlayerSettings = MidiPlayerSettings()
)

@Preview
@Composable
fun MidiPlayerSidePanel(
    getScore: suspend () -> Either<DomainError, Score>,
    playerContext: PlayerContext,
    deprecatedSettings: DeprecatedSettings,
    onValueChange: (PlayerContext, DeprecatedSettings) -> Unit
) {
    LazyColumn(modifier = Modifier.width(400.dp)) {
        item {
            Text(
                "Settings",
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            )
        }
        item {
            PlayPauseMidiPlayerButton(getScore, playerContext) { context ->
                onValueChange(context, deprecatedSettings)
            }
        }
        item {
            Text(modifier = padding, text = "Metronome")
            Switch(
                modifier = padding,
                checked = playerContext.playerSettings.enableMetronome,
                onCheckedChange = { newValue ->
                    onValueChange(
                        playerContext.copy(
                            playerSettings =
                                playerContext.playerSettings.copy(enableMetronome = newValue)
                        ),
                        deprecatedSettings
                    )
                }
            )
        }
        item {
            Text(modifier = padding, text = "Draw inner orbit")
            Switch(
                modifier = padding,
                checked = deprecatedSettings.drawInnerOrbit,
                onCheckedChange = { newValue ->
                    onValueChange(playerContext, deprecatedSettings.copy(drawInnerOrbit = newValue))
                }
            )
        }
        item {
            Text(modifier = padding, text = "Draw outer orbit")
            Switch(
                modifier = padding,
                checked = deprecatedSettings.drawOuterOrbit,
                onCheckedChange = { newValue ->
                    onValueChange(playerContext, deprecatedSettings.copy(drawOuterOrbit = newValue))
                }
            )
        }
        item {
            PropertySlider(
                deprecatedSettings.orbitRadius,
                MIN_ORBIT_RADIUS,
                MAX_ORBIT_RADIUS,
                "Orbits radius"
            ) { newValue ->
                onValueChange(playerContext, deprecatedSettings.copy(orbitRadius = newValue))
            }
        }
        item {
            StepSlider(
                deprecatedSettings.stepsInnerOrbit,
                MIN_STEPS,
                MAX_STEPS,
                "Steps for inner orbit"
            ) { newValue ->
                onValueChange(playerContext, deprecatedSettings.copy(stepsInnerOrbit = newValue))
            }
        }
        item {
            StepSlider(
                deprecatedSettings.stepsOuterOrbit,
                MIN_STEPS,
                MAX_STEPS,
                "Steps for outer orbit"
            ) { newValue ->
                onValueChange(playerContext, deprecatedSettings.copy(stepsOuterOrbit = newValue))
            }
        }
        item {
            PropertySlider(
                deprecatedSettings.innerCirclesRadius,
                MIN_INNER_RADIUS,
                MAX_INNER_RADIUS,
                "Inner orbit circles radius"
            ) { newValue ->
                onValueChange(playerContext, deprecatedSettings.copy(innerCirclesRadius = newValue))
            }
        }
        item {
            PropertySlider(
                deprecatedSettings.outerCirclesRadius,
                MIN_OUTER_RADIUS,
                MAX_OUTER_RADIUS,
                "Outer orbit circles radius"
            ) { newValue ->
                onValueChange(playerContext, deprecatedSettings.copy(outerCirclesRadius = newValue))
            }
        }
        item {
            PropertySlider(
                deprecatedSettings.randomCoefficient,
                MIN_RANDOM,
                MAX_RANDOM,
                "Random"
            ) { newValue ->
                onValueChange(playerContext, deprecatedSettings.copy(randomCoefficient = newValue))
            }
        }
    }
}
