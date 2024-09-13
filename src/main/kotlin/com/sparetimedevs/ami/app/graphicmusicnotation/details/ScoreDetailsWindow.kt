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
import com.sparetimedevs.ami.core.validation.validationErrorForProperty
import com.sparetimedevs.ami.music.data.kotlin.part.Part
import com.sparetimedevs.ami.music.data.kotlin.part.PartId
import com.sparetimedevs.ami.music.data.kotlin.part.PartInstrumentName
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreId
import com.sparetimedevs.ami.music.data.kotlin.score.ScoreTitle

@Composable
fun ScoreDetailsWindow(
    component: ScoreDetailsComponent,
    onSelectedIndexValueChange: (Int) -> Unit,
) {

    val score by component.scoreValue.subscribeAsState()
    val scoreId by component.scoreIdValue.subscribeAsState()
    val scoreTitle by component.scoreTitleValue.subscribeAsState()
    val partInstrumentNames by component.partInstrumentNamesValue.subscribeAsState()
    val mappedValidationErrors by component.mappedValidationErrorsValue.subscribeAsState()

    DialogWindow(onCloseRequest = { onSelectedIndexValueChange(-1) }, title = "Score details") {
        Column(modifier = Modifier.padding(10.dp)) {
            Row {
                Text("Score ID")
                OutlinedTextField(
                    value = scoreId,
                    onValueChange = { scoreId -> component.updateScoreId(scoreId) },
                )
                if (mappedValidationErrors.containsKey(validationErrorForProperty<ScoreId>())) {
                    Text(
                        mappedValidationErrors.getOrDefault(
                            validationErrorForProperty<ScoreId>(),
                            "",
                        ),
                        color = Color.Red,
                    )
                }
            }
            Row {
                Text("Score title")
                OutlinedTextField(
                    value = scoreTitle,
                    onValueChange = { scoreTitle -> component.updateScoreTitle(scoreTitle) },
                )
                if (mappedValidationErrors.containsKey(validationErrorForProperty<ScoreTitle>())) {
                    Text(
                        mappedValidationErrors.getOrDefault(
                            validationErrorForProperty<ScoreTitle>(),
                            "",
                        ),
                        color = Color.Red,
                    )
                }
            }
            // TODO here make a list of parts
            // TODO and then validate if we can get the validation error for a particular
            // partInstrumentName to the right part.
            Column {
                score.parts.forEach { part: Part ->
                    Row {
                        Text("part ${part.id.value} partInstrumentName")
                        OutlinedTextField(
                            value =
                                partInstrumentNames.getOrDefault(
                                    part.id,
                                    part.instrument?.name?.value ?: ""
                                ),
                            onValueChange = { partInstrumentName ->
                                component.updatePartInstrumentName(
                                    partId = part.id,
                                    newValue = partInstrumentName,
                                )
                            }
                        )
                        if (
                            mappedValidationErrors.containsKey(
                                validationErrorForProperty<PartInstrumentName>()
                            )
                        ) {
                            Text(
                                mappedValidationErrors.getOrDefault(
                                    validationErrorForProperty<PartInstrumentName>(),
                                    "",
                                ),
                                color = Color.Red,
                            )
                        }
                    }
                }
                Text("Add new part")
                Row {
                    Text("new part partInstrumentName")
                    OutlinedTextField(
                        value =
                            partInstrumentNames.getOrDefault(PartId.unsafeCreate("new part"), ""),
                        onValueChange = { partInstrumentName ->
                            component.updatePartInstrumentName(
                                partId = PartId.unsafeCreate("new part"),
                                newValue = partInstrumentName,
                            )
                        }
                    )
                    if (
                        mappedValidationErrors.containsKey(
                            validationErrorForProperty<PartInstrumentName>()
                        )
                    ) {
                        Text(
                            mappedValidationErrors.getOrDefault(
                                validationErrorForProperty<PartInstrumentName>(),
                                "",
                            ),
                            color = Color.Red,
                        )
                    }
                }
            }
            Button(onClick = { component.saveScoreDetails() }) { Text("Save") }
        }
    }
}
