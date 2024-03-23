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

package com.sparetimedevs.ami.player.midi.message

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AllSoundOffMidiMessageTest :
    StringSpec({
        "a b c" {
            val x0 = AllSoundOffMidiMessage(0)
            println(Integer.toBinaryString(x0.message[0].toInt()))
            println(Integer.toBinaryString(x0.message[1].toInt()))
            println(Integer.toBinaryString(x0.message[2].toInt()))
            x0 shouldBe x0
            val x = AllSoundOffMidiMessage(1)
            println(Integer.toBinaryString(x.message[0].toInt()))
            println(Integer.toBinaryString(x.message[1].toInt()))
            println(Integer.toBinaryString(x.message[2].toInt()))
            x shouldBe x
            val x2 = AllSoundOffMidiMessage(2)
            println(Integer.toBinaryString(x2.message[0].toInt()))
            println(Integer.toBinaryString(x2.message[1].toInt()))
            println(Integer.toBinaryString(x2.message[2].toInt()))
            x2 shouldBe x2
        }
    })
