package aoc

import jdk.jshell.spi.ExecutionControl
import readFile
import kotlin.math.exp

fun day16a() {

    checkVersionSum("8A004A801A8002F478", 16)
    checkVersionSum("620080001611562C8802118E34", 12)
    checkVersionSum("C0015000016115A2E0802F182340", 23)
    checkVersionSum("A0016C880162017C3686B18A3D4780", 31)
    checkVersionSum(readFile("day16_input.txt").first(), 0)

}

fun checkVersionSum(input: String, expectedCount: Int) {
    val bitString = hexStringToBitString(input)

    val packet = BITSPacket(bitString)

    val subPackets = packet.subPackets()

    val sum = packet.version() + subPackets.sumOf { it.version() }

    println("$input: $sum should be $expectedCount")
}

fun hexStringToBitString(input: String): String {
    val result = StringBuilder()

    input.chunked(1).forEach { chunk ->
        val value = Integer.valueOf(chunk, 16)
        var stringValue = Integer.toBinaryString(value)

        while (stringValue.length != 4) {
            stringValue = "0$stringValue"
        }

        result.append(stringValue)
    }

    return result.toString()
}

data class BITSPacket(val bitString: String) {
    override fun toString(): String {
        return "version: ${version()}, " +
                "typeId: ${typeID()}, " +
                "lengthTypeID: ${lengthTypeID()}, " +
                "endOfPacket: ${getEndOfPacket()}, " +
                "literalValue: ${literalValue()}"
    }

    fun version(): Int {
        return Integer.valueOf(bitString.substring(0, 3), 2)
    }

    private fun typeID(): Int {
        return Integer.valueOf(bitString.substring(3, 6), 2)
    }

    private fun lengthTypeID(): Int {
        if (typeID() == 4) return 0

        return Integer.valueOf(bitString.substring(6, 7), 2)
    }

    fun literalValue(): Long {
        when (typeID()) {
            0 -> {
                return subPackets().sumOf { it.literalValue() }
            }
            1 -> {
                val subPackets = subPackets()
                var value = subPackets.first().literalValue()
                for (i in 1 until subPackets.size) {
                    value *= subPackets[i].literalValue()
                }
                return value
            }
            2 -> {
                return subPackets().minOf { it.literalValue() }
            }
            3 -> {
                return subPackets().maxOf { it.literalValue() }
            }
            4 -> {
                val literalValue = StringBuilder()

                var index = 6
                while (bitString[index] == '1') {
                    literalValue.append(bitString.substring(index + 1, index + 5))
                    index += 5
                }
                literalValue.append(bitString.substring(index + 1, index + 5))
                return literalValue.toString().toLong(2)
            }
            5 -> {
                val subPackets = subPackets()
                return when (subPackets.first().literalValue() > subPackets.last().literalValue() ) {
                    true -> 1
                    false -> 0
                }
            }
            6 -> {
                val subPackets = subPackets()
                return when (subPackets.first().literalValue() < subPackets.last().literalValue() ) {
                    true -> 1
                    false -> 0
                }
            }
            7 -> {
                val subPackets = subPackets()
                return when (subPackets.first().literalValue() == subPackets.last().literalValue() ) {
                    true -> 1
                    false -> 0
                }
            }
            else -> throw ExecutionControl.NotImplementedException("typeID should be <= 7")
        }
    }

    private fun getEndOfPacket(): Int {
        if (typeID() == 4) {
            var index = 6
            while (bitString[index] == '1') {
                index += 5
            }
            return index + 5
        }

        if (lengthTypeID() == 0) {
            val packetSize = getPacketSize()
            return 7 + 15 + packetSize
        }

        val packetCount = getNumPackets()
        var packetIndex = 0
        var bitStringIndex = 6 + 11 + 1

        while (packetIndex < packetCount) {
            val packet = BITSPacket(bitString.substring(bitStringIndex))
            packetIndex++
            bitStringIndex += packet.getEndOfPacket()
        }

        return bitStringIndex
    }

    private fun getPacketSize() = Integer.valueOf(bitString.substring(7, 7 + 15), 2)

    private fun getNumPackets() = Integer.valueOf(bitString.substring(7, 7 + 11), 2)

    fun subPackets(): List<BITSPacket> {
        if (typeID() == 4) return listOf()

        val packets = mutableListOf<BITSPacket>()

        if (lengthTypeID() == 0) {
            val packetSize = getPacketSize()

            val packetString = bitString.substring(7 + 15, 7 + 15 + packetSize)
            var bitStringIndex = 0

            while (bitStringIndex < packetString.length) {
                val packet = BITSPacket(packetString.substring(bitStringIndex))
                packets.add(packet)
                //packets.addAll(packet.subPackets())
                bitStringIndex += packet.getEndOfPacket()
            }
        } else {

            val packetCount = getNumPackets()
            var packetIndex = 0
            var bitStringIndex = 6 + 11 + 1

            while (packetIndex < packetCount) {
                val packet = BITSPacket(bitString.substring(bitStringIndex))
                packets.add(packet)
                //packets.addAll(packet.subPackets())
                bitStringIndex += packet.getEndOfPacket()
                packetIndex++
            }
        }

        return packets
    }
}

fun day16b() {
    checkLiteralValue("C200B40A82", 3)
    checkLiteralValue("04005AC33890", 54)
    checkLiteralValue("880086C3E88112", 7)
    checkLiteralValue("CE00C43D881120", 9)
    checkLiteralValue("D8005AC2A8F0", 1)
    checkLiteralValue("F600BC2D8F", 0)
    checkLiteralValue("9C005AC2F8F0", 0)
    checkLiteralValue("9C0141080250320F1802104A08", 1)
    checkLiteralValue(readFile("day16_input.txt").first(), 0)
}

fun checkLiteralValue(input: String, expectedCount: Int) {
    val bitString = hexStringToBitString(input)

    val packet = BITSPacket(bitString)

    val sum = packet.literalValue()

    println("$input: $sum should be $expectedCount")
}