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

package com.sparetimedevs.ami.app.graphicmusicnotation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.sparetimedevs.ami.core.validation.NoValidationIdentifier
import com.sparetimedevs.ami.core.validation.ValidationError
import com.sparetimedevs.ami.core.validation.ValidationErrorForProperty
import com.sparetimedevs.ami.core.validation.ValidationIdentifier
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import com.sparetimedevs.ami.music.data.kotlin.midi.MidiChannel
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrumentName
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreTitle
import com.sparetimedevs.ami.music.input.validation.ValidationIdentifierForPart

@Composable
fun ScoreDetailsWindow(
    component: ScoreDetailsComponent,
    onSelectedIndexValueChange: (Int) -> Unit,
) {

    val score by component.scoreValue.subscribeAsState()
    val scoreId by component.scoreIdValue.subscribeAsState()
    val scoreTitle by component.scoreTitleValue.subscribeAsState()
    val partInstrumentNames by component.partInstrumentNamesValue.subscribeAsState()
    val partMidiChannels by component.partMidiChannelsValue.subscribeAsState()
    val mappedValidationErrors by component.mappedValidationErrorsValue.subscribeAsState()

    // TODO style this a lot better.
    DialogWindow(onCloseRequest = { onSelectedIndexValueChange(-1) }, title = "Score details") {
        Column(modifier = Modifier.padding(10.dp)) {
            Row {
                Text("Score ID")
                OutlinedTextField(
                    value = scoreId,
                    onValueChange = { scoreId -> component.updateScoreId(scoreId) },
                )
                DisplayValidationError<ScoreId>(mappedValidationErrors)
            }
            Row {
                Text("Score title")
                OutlinedTextField(
                    value = scoreTitle,
                    onValueChange = { scoreTitle -> component.updateScoreTitle(scoreTitle) },
                )
                DisplayValidationError<ScoreTitle>(mappedValidationErrors)
            }
            Column {
                score.parts.forEach { part: Part ->
                    val filteredValidationErrors: List<ValidationError> =
                        mappedValidationErrors.filter { thingy(it.validationIdentifier, part.id) }
                    Row {
                        Text("part ${part.id.value} partInstrumentName")
                        OutlinedTextField(
                            value =
                                partInstrumentNames.getOrDefault(
                                    part.id,
                                    part.instrument?.name?.value ?: "",
                                ),
                            onValueChange = { partInstrumentName ->
                                component.updatePartInstrumentName(
                                    partId = part.id,
                                    newValue = partInstrumentName,
                                )
                            },
                        )
                        DisplayValidationError<PartInstrumentName>(filteredValidationErrors)
                    }
                    Row {
                        Text("part ${part.id.value} partMidiChannel")
                        OutlinedTextField(
                            value =
                                partMidiChannels.getOrDefault(
                                    part.id,
                                    part.instrument?.midiChannel?.value?.toString() ?: "",
                                ),
                            onValueChange = { partMidiChannel ->
                                component.updatePartMidiChannel(
                                    partId = part.id,
                                    newValue = partMidiChannel,
                                )
                            },
                        )
                        DisplayValidationError<MidiChannel>(filteredValidationErrors)
                    }
                }
                Text("Add new part")
                Button(onClick = { component.createNewPart() }) { Text("Add new part") }
            }
            Button(onClick = { component.saveScoreDetails() }) { Text("Save") }
        }
    }
}

private fun thingy(validationIdentifier: ValidationIdentifier, partId: PartId): Boolean {
    val xxx = returnFirstValidationIdentifierForPart(validationIdentifier)
    return xxx?.identifier == partId
}

// TODO what happens if there is no ValidationIdentifierForPart and NoValidationIdentifier in the
// hierarchy? Write unit test for it.
private tailrec fun returnFirstValidationIdentifierForPart(
    validationIdentifier: ValidationIdentifier
): ValidationIdentifierForPart? =
    when (validationIdentifier) {
        is ValidationIdentifierForPart -> validationIdentifier
        is NoValidationIdentifier -> null
        else ->
            returnFirstValidationIdentifierForPart(validationIdentifier.validationIdentifierParent)
    }

@Composable
inline fun <reified T : Any> DisplayValidationError(validationErrors: List<ValidationError>) {
    if (validationErrors.contains(validationErrorForProperty<T>())) {
        Text(
            validationErrors
                .find { it.validationErrorForProperty == validationErrorForProperty<T>() }
                ?.message ?: "",
            color = Color.Red,
        )
    }
}

fun List<ValidationError>.contains(
    validationErrorForProperty: ValidationErrorForProperty
): Boolean = this.map { it.validationErrorForProperty }.contains(validationErrorForProperty)
