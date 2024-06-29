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

package com.sparetimedevs.ami.app.graphicmusicnotation.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

val BackgroundColor: Color = Color.White

@Composable
fun DrawBackdrop(
    modifier: Modifier = Modifier,
    backdropLines: List<TheLineThatRepresentsAPitch>,
    lineThickness: Float
) {
    Canvas(modifier = modifier.fillMaxSize().background(BackgroundColor)) {
        drawBackdrop(backdropLines, lineThickness)
    }
}

private fun DrawScope.drawBackdrop(
    backdropLines: List<TheLineThatRepresentsAPitch>,
    lineThickness: Float
): Unit {
    backdropLines.forEach { l: TheLineThatRepresentsAPitch ->
        drawPath(
            path = l.pathData.asComposePath(),
            color = l.color,
            style = Stroke(width = lineThickness)
        )
    }
}
