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

package com.sparetimedevs.ami.graphic.vector

/**
 * A Vector in this case is a coordinate vector with an ordered list of numbers (a tuple) that
 * describes the vector in terms of a particular ordered basis. Vectors are placed on a
 * 2-dimensional area with Vector(0.0, 0.0) starting at the bottom left.
 */
data class Vector(val x: Double, val y: Double)
