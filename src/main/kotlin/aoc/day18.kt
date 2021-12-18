package aoc

import readFile
import kotlin.math.floor
import kotlin.math.ceil

fun day18a() {

    val lines = readFile("day18_input.txt")

    var example = SnailfishNumber(lines.first()).reduce()

    for (line in lines.subList(1, lines.size)) {
        example = example.add(line).reduce()
    }

    val magnitude = example.magnitude()

    println("magnitude: $magnitude")
}

data class SnailfishNumber(val number: String) {

    fun add(number: String): SnailfishNumber {
        return add(SnailfishNumber(number))
    }

    fun add(number: SnailfishNumber): SnailfishNumber {
        return SnailfishNumber("[${this.number},${number.number}]")
    }

    fun reduce(): SnailfishNumber {
        val result = StringBuilder(number)
        var found = true

        println("$number ->")

        while (found) {
            found = false

            while (explode(result)) {
                found = true
            }

            while (split(result)) {
                found = true
                break
            }
        }

        println(result.toString())

        return SnailfishNumber(result.toString())
    }

    private fun split(number: StringBuilder): Boolean {
        val illegalCharacters = listOf('[', ']', ',')
        var startIndex = -1
        var endIndex = -1

        for (i in number.indices) {
            if (number[i] !in illegalCharacters) {
                if (startIndex == -1) startIndex = i
                endIndex = i
            } else if (startIndex != -1) {
                if (endIndex - startIndex == 0) {
                    startIndex = -1
                    endIndex = -1
                    continue
                }
                val numberToSplit = number.substring(startIndex, endIndex + 1).toInt()
                val left = floor(numberToSplit / 2.0).toInt()
                val right = ceil(numberToSplit / 2.0).toInt()
                number.replace(startIndex, endIndex + 1, "[$left,$right]")
                return true
            }
        }

        return false
    }

    private fun findExplosionCandidates(number: StringBuilder): Pair<Int, Int>? {
        var depth = 0

        for (i in number.indices) {
            if (number[i] == '[') depth++
            if (number[i] == ']') depth--

            if (depth >= 5) {
                val indexOfOpeningBracket = number.indexOf('[', i + 1)
                val indexOfClosingBracket = number.indexOf(']', i)
                if (indexOfOpeningBracket != -1 && indexOfOpeningBracket < indexOfClosingBracket) continue
                return Pair(i, indexOfClosingBracket)
            }
        }

        return null
    }

    private fun explode(number: StringBuilder): Boolean {
        val candidate = findExplosionCandidates(number) ?: return false
        val (startIndex, endIndex) = candidate
        val explodingPair = number.substring(startIndex + 1, endIndex)
        val (left, right) = explodingPair.split(",").map { it.toInt() }

        number.replace(startIndex, endIndex + 1, "0")

        val indicesNumberToTheRight = findNumberIndicesInDirection(number, startIndex + 1, 1)
        val indicesNumberToTheLeft = findNumberIndicesInDirection(number, startIndex, -1)

        if (indicesNumberToTheRight != null) {
            val numberToTheRight =
                number.substring(indicesNumberToTheRight.first, indicesNumberToTheRight.second + 1).toInt()
            val newValue = right + numberToTheRight
            number.replace(indicesNumberToTheRight.first, indicesNumberToTheRight.second + 1, newValue.toString())
        }

        if (indicesNumberToTheLeft != null) {
            val numberToTheLeft =
                number.substring(indicesNumberToTheLeft.first, indicesNumberToTheLeft.second + 1).toInt()
            val newValue = left + numberToTheLeft
            number.replace(indicesNumberToTheLeft.first, indicesNumberToTheLeft.second + 1, newValue.toString())
        }

        return true
    }

    private fun findNumberIndicesInDirection(number: StringBuilder, index: Int, direction: Int): Pair<Int, Int>? {
        var i = index + direction
        val illegalCharacters = listOf('[', ']', ',')
        var firstHit = -1
        var lastHit = -1

        while (i in number.indices) {
            if (number[i] !in illegalCharacters) {
                if (firstHit == -1) {
                    firstHit = i
                }
                lastHit = i
            } else if (lastHit != -1) {
                return if (firstHit < lastHit) {
                    Pair(firstHit, lastHit)
                } else {
                    Pair(lastHit, firstHit)
                }
            }

            i += direction
        }

        return null
    }

    fun magnitude(startIndex: Int = 0, endIndex: Int = number.length): Long {
        val pairs = findPairs(startIndex, endIndex) ?: return 0L

        val leftMagnitude = if (pairs.first.number.contains("[")) {
            pairs.first.magnitude()
        } else {
            pairs.first.number.toLong()
        }
        val rightMagnitude = if (pairs.second.number.contains("[")) {
            pairs.second.magnitude()
        } else {
            pairs.second.number.toLong()
        }

        return leftMagnitude * 3 + rightMagnitude * 2
    }

    private fun findPairs(startIndex: Int, endIndex: Int): Pair<SnailfishNumber, SnailfishNumber>? {
        var depth = 0

        for (i in startIndex + 1..endIndex) {
            if (number[i] == '[') depth++
            if (number[i] == ']') depth--

            if (depth == 0) {
                return Pair(
                    SnailfishNumber(number.substring(startIndex + 1, i + 1)),
                    SnailfishNumber(number.substring(i + 2, endIndex - 1))
                )
            }
        }
        return null
    }
}

fun day18b() {
    val lines = readFile("day18_input.txt")

    var max = 0L

    for (line in lines) {
        for (line2 in lines) {
            if (line == line2) continue
            val mag = SnailfishNumber(line).add(SnailfishNumber(line2)).reduce().magnitude()
            if (mag > max) max = mag
        }
    }

    println("largest magnitude: $max")
}