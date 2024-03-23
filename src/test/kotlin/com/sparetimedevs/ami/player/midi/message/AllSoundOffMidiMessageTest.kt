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
import java.math.BigInteger

class AllSoundOffMidiMessageTest :
    StringSpec({
        "AllSoundOffMidiMessage should be successfully created for all channels" {
            AllSoundOffOnChannel0MidiMessage.message[0] shouldBe BigInteger("10110000", 2).toByte()
            AllSoundOffOnChannel1MidiMessage.message[0] shouldBe BigInteger("10110001", 2).toByte()
            AllSoundOffOnChannel2MidiMessage.message[0] shouldBe BigInteger("10110010", 2).toByte()
            AllSoundOffOnChannel3MidiMessage.message[0] shouldBe BigInteger("10110011", 2).toByte()
            AllSoundOffOnChannel4MidiMessage.message[0] shouldBe BigInteger("10110100", 2).toByte()
            AllSoundOffOnChannel5MidiMessage.message[0] shouldBe BigInteger("10110101", 2).toByte()
            AllSoundOffOnChannel6MidiMessage.message[0] shouldBe BigInteger("10110110", 2).toByte()
            AllSoundOffOnChannel7MidiMessage.message[0] shouldBe BigInteger("10110111", 2).toByte()
            AllSoundOffOnChannel8MidiMessage.message[0] shouldBe BigInteger("10111000", 2).toByte()
            AllSoundOffOnChannel9MidiMessage.message[0] shouldBe BigInteger("10111001", 2).toByte()
            AllSoundOffOnChannel10MidiMessage.message[0] shouldBe BigInteger("10111010", 2).toByte()
            AllSoundOffOnChannel11MidiMessage.message[0] shouldBe BigInteger("10111011", 2).toByte()
            AllSoundOffOnChannel12MidiMessage.message[0] shouldBe BigInteger("10111100", 2).toByte()
            AllSoundOffOnChannel13MidiMessage.message[0] shouldBe BigInteger("10111101", 2).toByte()
            AllSoundOffOnChannel14MidiMessage.message[0] shouldBe BigInteger("10111110", 2).toByte()
            AllSoundOffOnChannel15MidiMessage.message[0] shouldBe BigInteger("10111111", 2).toByte()

            AllSoundOffOnChannel0MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel1MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel2MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel3MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel4MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel5MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel6MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel7MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel8MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel9MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel10MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel11MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel12MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel13MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel14MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()
            AllSoundOffOnChannel15MidiMessage.message[1] shouldBe BigInteger("01111000", 2).toByte()

            AllSoundOffOnChannel0MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel1MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel2MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel3MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel4MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel5MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel6MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel7MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel8MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel9MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel10MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel11MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel12MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel13MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel14MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
            AllSoundOffOnChannel15MidiMessage.message[2] shouldBe BigInteger("00000000", 2).toByte()
        }
    })
