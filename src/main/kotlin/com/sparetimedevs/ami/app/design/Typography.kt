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

package com.sparetimedevs.ami.app.design

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

val DefaultLineHeightStyle =
    LineHeightStyle(alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.None)

val DefaultTextStyle = TextStyle.Default.copy(lineHeightStyle = DefaultLineHeightStyle)

val AppTypography =
    Typography(
        defaultFontFamily = FontFamily.Default,
        h1 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Light,
                fontSize = 96.sp,
                lineHeight = 112.sp,
                letterSpacing = (-1.5).sp,
            ),
        h2 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Light,
                fontSize = 60.sp,
                lineHeight = 72.sp,
                letterSpacing = (-0.5).sp,
            ),
        h3 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 48.sp,
                lineHeight = 56.sp,
                letterSpacing = 0.sp,
            ),
        h4 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 34.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.25.sp,
            ),
        h5 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        h6 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp,
            ),
        subtitle1 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
            ),
        subtitle2 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp,
            ),
        body1 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        body2 =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                letterSpacing = 0.sp,
            ),
        button =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.25.sp,
            ),
        caption =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        overline =
            DefaultTextStyle.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.5.sp,
            ),
    )
