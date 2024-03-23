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

import java.math.BigInteger
import javax.sound.midi.MidiMessage
import javax.sound.midi.ShortMessage

class AllSoundOffMidiMessage(data: ByteArray) : MidiMessage(data) {

    constructor(channel: Int) : this(ByteArray(3)) {
        // https://midi.org/summary-of-midi-1-0-messages
        // All sound off on channel specified

        data[0] = (ShortMessage.CONTROL_CHANGE or channel).toByte()
        // data[0] = BigInteger("10110000", 2).toByte() // channel 0
        // data[0] = BigInteger("10110001", 2).toByte() // channel 1
        data[1] = BigInteger("01111000", 2).toByte()
        data[2] = BigInteger("00000000", 2).toByte()

        length = 3
    }

    override fun clone(): Any {
        val newData = ByteArray(length)
        System.arraycopy(data, 0, newData, 0, newData.size)
        return AllSoundOffMidiMessage(newData)
    }
}
