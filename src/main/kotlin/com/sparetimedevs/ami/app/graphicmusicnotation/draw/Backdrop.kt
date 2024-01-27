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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.Dispatchers
import org.jetbrains.skia.Image

val backgroundColor: Color = Color.White

@Composable
fun drawBackdrop(component: DrawGraphicMusicNotationComponent, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        drawBackdrop(component)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun drawBackdropToImage(
    component: DrawGraphicMusicNotationComponent,
    modifier: Modifier = Modifier
): Image {

    val windowInfo = LocalWindowInfo.current
    val with = windowInfo.containerSize.width
    val height = windowInfo.containerSize.height
    println(with)
    println(height)

    val scene =
        ImageComposeScene(
            width = with,
            height = height,
            density = Density(1f), // Is this the right value?
            coroutineContext = Dispatchers.Unconfined
        ) {
            Canvas(modifier = modifier.fillMaxSize().background(backgroundColor)) {
                drawBackdrop(component)
            }
        }
    val img: Image = scene.render()

    return img
}

private fun DrawScope.drawBackdrop(component: DrawGraphicMusicNotationComponent): Unit {
    component.getBackdrop().forEach { l: TheLineThatRepresentsAPitch ->
        drawPath(
            path = l.pathData.asComposePath(),
            color = l.color,
            style = Stroke(width = component.getLineThickness())
        )
    }
}
