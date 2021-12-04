package aoc

import readFile

fun day3a() {
    val lines = readFile("day3A_input.txt")

    var gamma = 0
    var epsilon = 0

    for (i in 0 until lines[0].length) {
        val sum = lines.sumOf { it[i].toString().toInt() }

        val factor = 1 shl (lines[0].length - i - 1)

        if (sum > lines.size / 2) {
            gamma += factor
        } else {
            epsilon += factor
        }
    }

    println("gamma: $gamma, epsilon: $epsilon, power consumption: ${gamma * epsilon}")
}

fun day3b() {
    val lines = readFile("day3A_input.txt")

    val oxygenGenerationRate = findFilteredLine(lines, mostCommon = true)
    val cO2ScrubbingRate = findFilteredLine(lines, mostCommon = false)

    println("oxygenGenerationRate: $oxygenGenerationRate, CO2ScrubbingRate: $cO2ScrubbingRate")
    println("o2: ${Integer.parseInt(oxygenGenerationRate, 2)}, co2: ${Integer.parseInt(cO2ScrubbingRate, 2)}")
    println("life support: ${Integer.parseInt(oxygenGenerationRate, 2) * Integer.parseInt(cO2ScrubbingRate, 2)}")
}

fun moreOnes(lines: List<String>, i: Int): Boolean {
    val sum = lines.sumOf { it[i].toString().toInt() }
    val difference = (lines.size) - (2 * sum)

    val result = when {
        difference > 0 -> false
        difference < 0 -> true
        else -> true
    }

    return  result
}

private fun findFilteredLine(lines: List<String>, mostCommon: Boolean): String {
    var filteredLines = lines
    for (i in 0 until filteredLines[0].length) {
        val more = moreOnes(filteredLines, i)

        filteredLines = if (more == mostCommon) {
            filteredLines.filter { it[i].toString().toInt() == 1 }
        } else {
            filteredLines.filter { it[i].toString().toInt() == 0 }
        }

        if (filteredLines.size == 1) break
    }

    if (filteredLines.size != 1) {
        println("Error! terminated without 1 line left")
    }

    return filteredLines[0]
}