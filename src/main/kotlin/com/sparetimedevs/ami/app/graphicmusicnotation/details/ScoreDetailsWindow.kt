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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sparetimedevs.ami.app.design.AppTypography
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationErrorForProperty
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiChannel
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiProgram
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrumentName
import com.sparetimedevs.ami.music.data.kotlin.part.PartName
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreTitle

@Composable
fun ScoreDetailsWindow(
    component: ScoreDetailsComponent,
    screenId: String,
    onSelectedIndexValueChange: (Int) -> Unit,
) {
    val score by component.scoreValue.subscribeAsState()

    // Set initial values
    val scoreId: MutableState<String> = remember(screenId) { mutableStateOf(score.id.value) }
    val scoreTitle: MutableState<String?> =
        remember(screenId) { mutableStateOf(score.title?.value) }
    val partIds: SnapshotStateMap<PartId, String> =
        remember(screenId) {
            val m: SnapshotStateMap<PartId, String> = mutableStateMapOf<PartId, String>()
            score.parts.forEach { part: Part -> m[part.id] = part.id.value }
            m
        }
    val partNames: SnapshotStateMap<PartId, String?> =
        remember(screenId) {
            val m: SnapshotStateMap<PartId, String?> = mutableStateMapOf<PartId, String?>()
            score.parts.forEach { part: Part ->
                if (part.name?.value != null) m[part.id] = part.name?.value
            }
            m
        }
    val partInstrumentNames: SnapshotStateMap<PartId, String?> =
        remember(screenId) {
            val m: SnapshotStateMap<PartId, String?> = mutableStateMapOf<PartId, String?>()
            score.parts.forEach { part: Part ->
                if (part.instrument?.name?.value != null) m[part.id] = part.instrument?.name?.value
            }
            m
        }
    val partInstrumentMidiChannels: SnapshotStateMap<PartId, String?> =
        remember(screenId) {
            val m: SnapshotStateMap<PartId, String?> = mutableStateMapOf<PartId, String?>()
            score.parts.forEach { part: Part ->
                if (part.instrument?.midiChannel?.value != null) {
                    m[part.id] =
                        part.instrument
                            ?.midiChannel
                            ?.value
                            ?.toString()
                }
            }
            m
        }
    val partInstrumentMidiPrograms: SnapshotStateMap<PartId, String?> =
        remember(screenId) {
            val m: SnapshotStateMap<PartId, String?> = mutableStateMapOf<PartId, String?>()
            score.parts.forEach { part: Part ->
                if (part.instrument?.midiProgram?.value != null) {
                    m[part.id] =
                        part.instrument
                            ?.midiProgram
                            ?.value
                            ?.toString()
                }
            }
            m
        }
    val validationErrors: SnapshotStateList<ValidationError> =
        remember(screenId) { mutableStateListOf<ValidationError>() }

    val dialogState = rememberDialogState(width = 1200.dp, height = 800.dp)

    DialogWindow(
        onCloseRequest = { onSelectedIndexValueChange(-1) },
        state = dialogState,
        title = "Score details",
    ) {
        Box(
            modifier =
                Modifier
                    .background(color = Color.DarkGray)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState()),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                ScoreDetailRow<ScoreId>(
                    displayKey = "Score ID",
                    displayValue = scoreId.value,
                    onValueChange = { s -> scoreId.value = s },
                    validationErrors = validationErrors,
                )
                ScoreDetailRow<ScoreTitle>(
                    displayKey = "Score title",
                    displayValue = scoreTitle.value,
                    onValueChange = { s -> scoreTitle.value = s },
                    validationErrors = validationErrors,
                )
                Column {
                    score.parts.forEach { part: Part ->
                        val filteredValidationErrors: List<ValidationError> =
                            validationErrors.filter {
                                containsValidationIdentifierForPart(
                                    it.validationIdentifier,
                                    part.id,
                                )
                            }
                        ScoreDetailRow<PartId>(
                            displayKey = "part ${part.id.value} partId",
                            displayValue = partIds[part.id],
                            onValueChange = { partId -> partIds[part.id] = partId },
                            validationErrors = filteredValidationErrors,
                        )
                        ScoreDetailRow<PartName>(
                            displayKey = "part ${part.id.value} partName",
                            displayValue = partNames[part.id],
                            onValueChange = { partName -> partNames[part.id] = partName },
                            validationErrors = filteredValidationErrors,
                        )
                        ScoreDetailRow<PartInstrumentName>(
                            displayKey = "part ${part.id.value} partInstrumentName",
                            displayValue = partInstrumentNames[part.id],
                            onValueChange = { partInstrumentName ->
                                partInstrumentNames[part.id] = partInstrumentName
                            },
                            validationErrors = filteredValidationErrors,
                        )
                        ScoreDetailRow<MidiChannel>(
                            displayKey = "part ${part.id.value} partInstrumentMidiChannel",
                            displayValue = partInstrumentMidiChannels[part.id],
                            onValueChange = { partInstrumentMidiChannel ->
                                partInstrumentMidiChannels[part.id] = partInstrumentMidiChannel
                            },
                            validationErrors = filteredValidationErrors,
                        )
                        ScoreDetailRow<MidiProgram>(
                            displayKey = "part ${part.id.value} partInstrumentMidiProgram",
                            displayValue = partInstrumentMidiPrograms[part.id],
                            onValueChange = { partInstrumentMidiProgram ->
                                partInstrumentMidiPrograms[part.id] = partInstrumentMidiProgram
                            },
                            validationErrors = filteredValidationErrors,
                        )
                    }
                    Text("Add new part", style = AppTypography.body2)
                    Button(onClick = { component.createNewPart() }) {
                        Text("Add new part", style = AppTypography.body2)
                    }
                }
                Button(
                    onClick = {
                        component
                            .saveScoreDetails(
                                scoreId = scoreId.value,
                                scoreTitle = scoreTitle.value,
                                partIds = partIds,
                                partNames = partNames,
                                partInstrumentNames = partInstrumentNames,
                                partInstrumentMidiChannels = partInstrumentMidiChannels,
                                partInstrumentMidiPrograms = partInstrumentMidiPrograms,
                            ).fold(
                                { e -> validationErrors.addAll(e) },
                                {
                                    validationErrors.clear()
                                    onSelectedIndexValueChange(-1)
                                },
                            )
                    },
                ) {
                    Text("Save", style = AppTypography.body2)
                }
            }
        }
    }
}

@Composable
inline fun <reified T : Any> DisplayValidationError(
    validationErrors: List<ValidationError>,
    modifier: Modifier,
) {
    if (validationErrors.contains(validationErrorForProperty<T>())) {
        Text(
            text =
                validationErrors
                    .find { it.validationErrorForProperty == validationErrorForProperty<T>() }
                    ?.message ?: "",
            color = Color.Red,
            style = AppTypography.body2,
            modifier = modifier,
        )
    }
}

@Composable
inline fun <reified T : Any> ScoreDetailRow(
    displayKey: String,
    displayValue: String?,
    crossinline onValueChange: (String) -> Unit,
    validationErrors: List<ValidationError>,
) {
    Row(modifier = Modifier) {
        Text(
            text = displayKey,
            style = AppTypography.body2,
            modifier = Modifier.align(Alignment.CenterVertically).width(400.dp),
        )
        CustomBasicTextField(value = displayValue ?: "", onValueChange = { s -> onValueChange(s) })
        DisplayValidationError<T>(validationErrors, Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun CustomBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            Modifier
                .height(24.dp) // Custom height
                .width(400.dp) // Set a width
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)) // Add a border
                .padding(horizontal = 8.dp), // Inner padding
        textStyle = AppTypography.body2.copy(color = LocalContentColor.current),
        singleLine = true, // Ensure single-line input
        cursorBrush = SolidColor(LocalContentColor.current),
        decorationBox = { innerTextField ->
            // Box to center the text vertically
            Box(
                modifier = Modifier.height(24.dp).padding(vertical = 4.dp),
                contentAlignment = Alignment.CenterStart, // Align text vertically center and left
            ) {
                innerTextField()
            }
        },
    )
}

fun List<ValidationError>.contains(validationErrorForProperty: ValidationErrorForProperty): Boolean =
    this.map { it.validationErrorForProperty }.contains(validationErrorForProperty)
