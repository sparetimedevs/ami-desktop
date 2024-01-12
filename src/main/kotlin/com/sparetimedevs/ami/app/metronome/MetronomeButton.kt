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

package com.sparetimedevs.ami.app.metronome

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MetronomeButton(enableMetronome: Boolean, onValueChanged: (Boolean) -> Unit) {
    TextButton(
        modifier = Modifier.padding(10.dp),
        onClick = { onValueChanged(!enableMetronome) },
        content = {
            when (enableMetronome) {
                true -> Text(text = "Metronome enabled")
                false -> Text(text = "Metronome disabled")
            }
        },
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.secondary),
        elevation = ButtonDefaults.elevation()
    )
}
