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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import com.sparetimedevs.ami.app.player.PlayerState
import com.sparetimedevs.ami.music.example.getExampleScore
import com.sparetimedevs.ami.music.example.getExampleScoreAsturias
import com.sparetimedevs.ami.music.example.getExampleScoreHeighHoNobodyHome

@Composable
fun TopAppBarTitleDropDown(
    text: String,
    component: MusicScoreDetailsComponent,
    onValueChange: (PlayerState) -> Unit
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    var selectedIndex: Int by remember { mutableStateOf(-1) }
    val items =
        listOf(
            TopAppBarTitleDropDownAction("New") {
                DialogWindow(
                    onCloseRequest = { selectedIndex = -1 },
                    title = "Sure you want to start new score?",
                ) {
                    Button(
                        onClick = {
                            selectedIndex = -1
                            component.onNewScoreClicked()
                            onValueChange(PlayerState.PAUSE)
                        }
                    ) {
                        Text("New")
                    }
                }
            },
            TopAppBarTitleDropDownAction("Load") {
                DialogWindow(
                    onCloseRequest = { selectedIndex = -1 },
                    title = "What score to load?",
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Button(
                            onClick = {
                                selectedIndex = -1
                                component.onLoadScoreClicked(getExampleScore())
                                onValueChange(PlayerState.PAUSE)
                            }
                        ) {
                            Text("Load example score")
                        }
                        Button(
                            onClick = {
                                selectedIndex = -1
                                component.onLoadScoreClicked(getExampleScoreAsturias())
                                onValueChange(PlayerState.PAUSE)
                            }
                        ) {
                            Text("Load Asturias")
                        }
                        Button(
                            onClick = {
                                selectedIndex = -1
                                component.onLoadScoreClicked(getExampleScoreHeighHoNobodyHome())
                                onValueChange(PlayerState.PAUSE)
                            }
                        ) {
                            Text("Load Heigh Ho Nobody Home")
                        }
                    }
                }
            },
            TopAppBarTitleDropDownAction("Save") {
                DialogWindow(
                    onCloseRequest = { selectedIndex = -1 },
                    title = "Sure you want to save the score?",
                ) {
                    Button(
                        onClick = {
                            selectedIndex = -1
                            component.onSaveScoreClicked()
                            onValueChange(PlayerState.PAUSE)
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        )
    Box(modifier = Modifier.wrapContentSize()) {
        Text(text, modifier = Modifier.clickable(onClick = { expanded = true }))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            items.forEachIndexed { index, action ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                    }
                ) {
                    Text(text = action.text)
                }
            }
        }
    }

    if (selectedIndex >= 0 && selectedIndex < items.size) {
        items[selectedIndex].onClick()
    }
}

data class TopAppBarTitleDropDownAction(val text: String, val onClick: @Composable () -> Unit)
