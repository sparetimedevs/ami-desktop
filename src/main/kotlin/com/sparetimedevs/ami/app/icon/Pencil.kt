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

package com.sparetimedevs.ami.app.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AmiDesktopAppIcons.Pencil: ImageVector
    get() {
        if (_pencil != null) {
            return _pencil!!
        }
        _pencil =
            Builder(
                name = "Pencil",
                defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp,
                viewportWidth = 60.031f,
                viewportHeight = 60.031f,
            ).apply {
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    stroke = null,
                    strokeLineWidth = 0.0f,
                    strokeLineCap = Butt,
                    strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd,
                ) {
                    moveTo(50.038f, 26.0f)
                    lineTo(22.106f, 54.06f)
                    arcToRelative(4.029f, 4.029f, 0.0f, false, true, -1.793f, 1.04f)
                    lineToRelative(-7.235f, 1.974f)
                    lineToRelative(-10.13f, -10.132f)
                    lineToRelative(1.974f, -7.235f)
                    arcToRelative(4.088f, 4.088f, 0.0f, false, true, 0.4f, -0.965f)
                    arcToRelative(4.0f, 4.0f, 0.0f, false, true, 0.638f, -0.828f)
                    lineTo(34.03f, 9.988f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    stroke = null,
                    strokeLineWidth = 0.0f,
                    strokeLineCap = Butt,
                    strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd,
                ) {
                    moveTo(58.839f, 17.324f)
                    lineTo(54.04f, 22.0f)
                    lineTo(38.032f, 5.986f)
                    lineToRelative(4.663f, -4.807f)
                    arcToRelative(4.036f, 4.036f, 0.0f, false, true, 5.708f, 0.0f)
                    lineToRelative(10.436f, 10.437f)
                    arcTo(4.029f, 4.029f, 0.0f, false, true, 58.839f, 17.324f)
                    close()
                    moveTo(2.557f, 59.944f)
                    arcToRelative(2.016f, 2.016f, 0.0f, false, true, -2.477f, -2.479f)
                    lineToRelative(1.582f, -5.806f)
                    lineToRelative(6.7f, 6.7f)
                    close()
                }
            }.build()
        return _pencil!!
    }

@Suppress("ktlint:standard:backing-property-naming")
private var _pencil: ImageVector? = null
