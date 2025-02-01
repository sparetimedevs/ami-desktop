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

package com.sparetimedevs.ami.app

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.ParcelableContainer
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.sparetimedevs.ami.app.design.AppLightColors
import com.sparetimedevs.ami.app.design.AppTypography
import com.sparetimedevs.ami.app.root.DefaultRootComponent
import com.sparetimedevs.ami.app.root.RootContent
import com.sparetimedevs.ami.app.utils.runOnUiThread
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@OptIn(ExperimentalDecomposeApi::class)
fun main() {
    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    val lifecycle = LifecycleRegistry()
    val stateKeeper = StateKeeperDispatcher(tryRestoreStateFromFile())

    val root =
        runOnUiThread {
            DefaultRootComponent(
                componentContext =
                    DefaultComponentContext(
                        lifecycle = lifecycle,
                        stateKeeper = stateKeeper,
                    ),
            )
        }

    application {
        val windowState = rememberWindowState(width = 1200.dp, height = 800.dp)

        LifecycleController(lifecycle, windowState)

        var isCloseRequested by remember { mutableStateOf(false) }

        Window(
            onCloseRequest = { isCloseRequested = true },
            state = windowState,
            title = "Ami",
            onKeyEvent = ::handleKeyEvent,
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MaterialTheme(colors = AppLightColors, typography = AppTypography) {
                    CompositionLocalProvider(LocalScrollbarStyle provides defaultScrollbarStyle()) {
                        RootContent(root)
                    }
                }
            }

            if (isCloseRequested) {
                SaveStateDialog(
                    onSaveState = { saveStateToFile(stateKeeper.save()) },
                    onExitApplication = ::exitApplication,
                    onDismiss = { isCloseRequested = false },
                )
            }
        }
    }
}

@Composable
private fun SaveStateDialog(
    onSaveState: () -> Unit,
    onExitApplication: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) { Text(text = "Cancel") }

                TextButton(onClick = onExitApplication) { Text(text = "No") }

                TextButton(
                    onClick = {
                        onSaveState()
                        onExitApplication()
                    },
                ) {
                    Text(text = "Yes")
                }
            }
        },
        title = { Text(text = "Decompose Sample") },
        text = { Text(text = "Do you want to save the application's state?") },
        modifier = Modifier.width(400.dp),
    )
}

private const val SAVED_STATE_FILE_NAME = "saved_state.dat"

private fun saveStateToFile(state: ParcelableContainer) {
    ObjectOutputStream(File(SAVED_STATE_FILE_NAME).outputStream()).use { output ->
        output.writeObject(state)
    }
}

private fun tryRestoreStateFromFile(): ParcelableContainer? =
    File(SAVED_STATE_FILE_NAME).takeIf(File::exists)?.let { file ->
        try {
            ObjectInputStream(file.inputStream()).use(ObjectInputStream::readObject)
                as ParcelableContainer
        } catch (e: Exception) {
            null
        } finally {
            file.delete()
        }
    }

private fun handleKeyEvent(keyEvent: KeyEvent): Boolean {
    when (keyEvent.key) {
        Key.Z -> {
            // TODO this should behave differently per target device.
            if (keyEvent.isMetaPressed) {
                println("Command + Z is pressed")
            } else if (keyEvent.isCtrlPressed) {
                println("Ctrl + Z is pressed")
            } else {
                println("${keyEvent.key} is pressed")
            }
        }
        Key.B -> {
            println("${keyEvent.key} is pressed")
        }
        else -> {
            println("${keyEvent.key} is pressed, but not mapped to any action")
        }
    }
    return false // true //Means propagate the key event.
}

private fun KeyEvent.isAltPressed(): Boolean =
    awtEventOrNull?.isAltDown == true || awtEventOrNull?.isAltGraphDown == true
