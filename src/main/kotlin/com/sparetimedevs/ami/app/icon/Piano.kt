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

public val AmiDesktopAppIcons.Piano: ImageVector
    get() {
        if (_piano != null) {
            return _piano!!
        }
        _piano =
            Builder(
                    name = "Piano",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 24.0.dp,
                    viewportWidth = 64.0f,
                    viewportHeight = 64.0f
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = SolidColor(Color(0x00000000)),
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd
                    ) {
                        moveTo(31.0f, 62.0f)
                        curveTo(31.0f, 62.0f, 17.0f, 62.0f, 17.0f, 62.0f)
                        curveTo(17.0f, 62.0f, 17.0f, 42.0f, 17.0f, 42.0f)
                        curveTo(17.0f, 42.0f, 20.0f, 42.0f, 20.0f, 42.0f)
                        curveTo(20.553f, 42.0f, 21.0f, 41.553f, 21.0f, 41.0f)
                        curveTo(21.0f, 41.0f, 21.0f, 2.0f, 21.0f, 2.0f)
                        curveTo(21.0f, 2.0f, 27.0f, 2.0f, 27.0f, 2.0f)
                        curveTo(27.0f, 2.0f, 27.0f, 41.0f, 27.0f, 41.0f)
                        curveTo(27.0f, 41.553f, 27.447f, 42.0f, 28.0f, 42.0f)
                        curveTo(28.0f, 42.0f, 31.0f, 42.0f, 31.0f, 42.0f)
                        curveTo(31.0f, 42.0f, 31.0f, 62.0f, 31.0f, 62.0f)
                        close()
                    }
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = SolidColor(Color(0x00000000)),
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd
                    ) {
                        moveTo(47.0f, 62.0f)
                        curveTo(47.0f, 62.0f, 33.0f, 62.0f, 33.0f, 62.0f)
                        curveTo(33.0f, 62.0f, 33.0f, 42.0f, 33.0f, 42.0f)
                        curveTo(33.0f, 42.0f, 36.0f, 42.0f, 36.0f, 42.0f)
                        curveTo(36.553f, 42.0f, 37.0f, 41.553f, 37.0f, 41.0f)
                        curveTo(37.0f, 41.0f, 37.0f, 2.0f, 37.0f, 2.0f)
                        curveTo(37.0f, 2.0f, 43.0f, 2.0f, 43.0f, 2.0f)
                        curveTo(43.0f, 2.0f, 43.0f, 41.0f, 43.0f, 41.0f)
                        curveTo(43.0f, 41.553f, 43.447f, 42.0f, 44.0f, 42.0f)
                        curveTo(44.0f, 42.0f, 47.0f, 42.0f, 47.0f, 42.0f)
                        curveTo(47.0f, 42.0f, 47.0f, 62.0f, 47.0f, 62.0f)
                        close()
                    }
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = SolidColor(Color(0x00000000)),
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd
                    ) {
                        moveTo(62.0f, 60.0f)
                        curveTo(62.0f, 61.104f, 61.104f, 62.0f, 60.0f, 62.0f)
                        curveTo(60.0f, 62.0f, 49.0f, 62.0f, 49.0f, 62.0f)
                        curveTo(49.0f, 62.0f, 49.0f, 42.0f, 49.0f, 42.0f)
                        curveTo(49.0f, 42.0f, 52.0f, 42.0f, 52.0f, 42.0f)
                        curveTo(52.553f, 42.0f, 53.0f, 41.553f, 53.0f, 41.0f)
                        curveTo(53.0f, 41.0f, 53.0f, 2.0f, 53.0f, 2.0f)
                        curveTo(53.0f, 2.0f, 60.0f, 2.0f, 60.0f, 2.0f)
                        curveTo(61.016f, 2.0f, 62.0f, 2.896f, 62.0f, 4.0f)
                        curveTo(62.0f, 4.0f, 62.0f, 60.0f, 62.0f, 60.0f)
                        close()
                    }
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = SolidColor(Color(0x00000000)),
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = EvenOdd
                    ) {
                        moveTo(2.0f, 60.0f)
                        curveTo(2.0f, 60.0f, 2.0f, 4.0f, 2.0f, 4.0f)
                        curveTo(2.0f, 2.896f, 2.641f, 2.0f, 4.0f, 2.0f)
                        curveTo(4.0f, 2.0f, 11.0f, 2.0f, 11.0f, 2.0f)
                        curveTo(11.0f, 2.0f, 11.0f, 41.0f, 11.0f, 41.0f)
                        curveTo(11.0f, 41.553f, 11.447f, 42.0f, 12.0f, 42.0f)
                        curveTo(12.0f, 42.0f, 15.0f, 42.0f, 15.0f, 42.0f)
                        curveTo(15.0f, 42.0f, 15.0f, 62.0f, 15.0f, 62.0f)
                        curveTo(15.0f, 62.0f, 4.0f, 62.0f, 4.0f, 62.0f)
                        curveTo(2.896f, 62.0f, 2.0f, 61.104f, 2.0f, 60.0f)
                        close()
                    }
                }
                .build()
        return _piano!!
    }

private var _piano: ImageVector? = null
