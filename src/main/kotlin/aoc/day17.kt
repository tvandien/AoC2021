package aoc

import readFile
import kotlin.math.max
import kotlin.math.min

fun day17a() {
    val inputString = readFile("day17_input.txt").first()

    val probeArea = inputStringToProbeArea(inputString)

    val maxDistance = -probeArea.y1
    val initialYVelocity = maxDistance - 1
    val score = crabScore(initialYVelocity)

    println("max y pos: $score")
}

data class ProbeArea(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

fun inputStringToProbeArea(input: String): ProbeArea {
    val (_, coordinates) = input.split(": ")
    val (xs, ys) = coordinates.split(", ")
    val (x1, x2) = xs.replace("x=", "").split("..")
    val (y1, y2) = ys.replace("y=", "").split("..")

    return ProbeArea(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
}

fun day17b() {
    val inputString = readFile("day17_input.txt").first()
    val probeArea = inputStringToProbeArea(inputString)

    //printAllExamples(probeArea)

    val xOptions = 0..probeArea.x2 //getXOptions(probeArea).sorted()
    val yOptions = probeArea.y1..-probeArea.y1 //getYOptions(probeArea).sorted()

    //val xcoords = parseCoordList().map { it.first }.distinct().sorted()
    //val ycoords = parseCoordList().map { it.second }.distinct().sorted()

    val validStarts = mutableListOf<Pair<Int, Int>>()

    for (x in xOptions) {
        for (y in yOptions) {
            val coordinates = getCoordinates(probeArea, x, y)

            if (coordinates.any { it.first in probeArea.x1..probeArea.x2 && it.second in probeArea.y1..probeArea.y2}) {
                validStarts.add(Pair(x, y))
            }
        }
    }

}

private fun getXOptions(probeArea: ProbeArea): List<Int> {
    val results = mutableListOf<Int>()

    // direct hits
    for (x in probeArea.x1..probeArea.x2) {
        results.add(x)
    }

    // slow hits
    for (i in 1..(probeArea.x2 / 2)) {
        val coordinates = getCoordinates(probeArea, i, 0)
        if (coordinates.any { it.first in probeArea.x1..probeArea.x2}) {
            results.add(i)
        }
    }

    return results.distinct()
}

private fun getYOptions(probeArea: ProbeArea): List<Int> {
    val results = mutableListOf<Int>()

    // direct hits
    for (y in probeArea.y1..probeArea.y2) {
        results.add(y)
    }

    // slow/fast hits
    for (i in (probeArea.y1)..-(probeArea.y1)) {
        val coordinates = getCoordinates(probeArea, 0, i)
        if (coordinates.any { it.second in probeArea.y1..probeArea.y2}) {
            results.add(i)
        }
    }

    return results.distinct()
}

private fun printAllExamples(probeArea: ProbeArea) {
    val coordList = parseCoordList()

    for (coord in coordList) {
        println("(${coord.first}, ${coord.second})")
        printTargetArea(probeArea, coord.first, coord.second)
    }
}

private fun parseCoordList(): MutableList<Pair<Int, Int>> {
    val coordString = readFile("day17_coords.txt")
    val coordList = mutableListOf<Pair<Int, Int>>()

    coordString.forEach { input ->
        input.split(" ")
            .filter { it != "" }
            .forEach { it ->
                val (x, y) = it.split(",")
                coordList.add(Pair(x.toInt(), y.toInt()))
            }
    }
    return coordList
}

fun printTargetArea(probeArea: ProbeArea, xVel: Int, yVel: Int) {
    val yMax = if (yVel > 0) crabScore(yVel) else 0
    val result = StringBuilder()
    val coordinates = getCoordinates(probeArea, xVel, yVel)

    for (y in yMax downTo probeArea.y1) {
        for (x in 0..probeArea.x2) {
            result.append(
                when {
                    x == 0 && y == 0 -> "S"
                    Pair(x, y) in coordinates -> "#"
                    (x >= probeArea.x1 && x <= probeArea.x2) && (y >= probeArea.y1 && y <= probeArea.y2) -> "T"
                    else -> "."
                }
            )
        }
        result.append("\n")
    }

    println(result.toString())
}

fun getCoordinates(probeArea: ProbeArea, xVel: Int, yVel: Int): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    var dx = xVel
    var dy = yVel
    var x = 0
    var y = 0

    while (x < probeArea.x2 && y > probeArea.y1) {
        x += dx
        y += dy

        result.add(Pair(x, y))

        dx = max(dx - 1, 0)
        dy--
    }

    return result
}