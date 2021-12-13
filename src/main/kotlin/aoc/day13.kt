package aoc

import readFile

fun day13a() {
    val lines = readFile("day13_input.txt")

    val (coordinatesString, rulesString) = lines.filter { it != "" }.chunked(lines.indexOf(""))
    val coordinates = coordinatesString.map { coordinate ->
        val (x, y) = coordinate.split(",")
        x.toInt() to y.toInt()
    }

    val folds = rulesString.map { foldFromString(it) }

    val result = performFolds(coordinates, listOf(folds.first()))

    println("dots: ${result.size}")
}

data class Fold(val axis: String, val value: Int)

fun foldFromString(input: String): Fold {
    val (axis, value) = input.split("=")
    return Fold(axis.last().toString(), value.toInt())
}

fun performFolds(coordinates: List<Pair<Int, Int>>, rules: List<Fold>): List<Pair<Int, Int>> {
    var actualCoordinates = mutableListOf<Pair<Int, Int>>()

    for (coordinate in coordinates) {
        var actualCoordinate = coordinate.copy()
        for (rule in rules) {
            if (rule.axis == "x" && actualCoordinate.first > rule.value) {
                val delta = actualCoordinate.first - rule.value
                actualCoordinate = actualCoordinate.copy(rule.value - delta, actualCoordinate.second)
            } else if (rule.axis == "y" && actualCoordinate.second > rule.value) {
                val delta = actualCoordinate.second - rule.value
                actualCoordinate = actualCoordinate.copy(actualCoordinate.first, rule.value - delta)
            }
        }
        actualCoordinates.add(actualCoordinate)
    }
    actualCoordinates = actualCoordinates.distinct().toMutableList()

    return actualCoordinates
}

fun printResults(coordinates: List<Pair<Int, Int>>) {
    val xMin = coordinates.minOf { it.first }
    val xMax = coordinates.maxOf { it.first }
    val yMin = coordinates.minOf { it.second }
    val yMax = coordinates.maxOf { it.second }

    var output = ""

    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            output += (if (coordinates.contains(Pair(x, y))) "#" else ".")
        }
        output += "\n"
    }

    println(output)

}

fun day13b() {
    val lines = readFile("day13_input.txt")

    val (coordinatesString, rulesString) = lines.filter { it != "" }.chunked(lines.indexOf(""))
    val coordinates = coordinatesString.map { coordinate ->
        val (x, y) = coordinate.split(",")
        x.toInt() to y.toInt()
    }

    val folds = rulesString.map { foldFromString(it) }

    val dots = performFolds(coordinates, folds)

    printResults(dots)
}