package aoc

import readFile
import kotlin.math.abs

fun day7a() {
    val lines = readFile("day7_input.txt")
    val numbers = lines.first().split(",").map { it.toInt() }.sorted()

    val median = numbers[numbers.size / 2]
    val score = numbers.sumOf { abs(it - median) }

    println("cheapest at position $median. Score: $score")
}

fun day7b() {
    val lines = readFile("day7_example.txt")
    val numbers = lines.first().split(",").map { it.toInt() }.sorted()

    var minimalScore = Int.MAX_VALUE
    var minimalPosition = -1
    for (i in 0..numbers.last()) {
        val score = numbers.sumOf { crabScore(abs(it - i)) }
        if (score < minimalScore) {
            minimalScore = score
            minimalPosition = i
        } else {
            break
        }
    }

    val mean = (numbers.sum().toDouble() / numbers.size)

    println("Mean: $mean")
    println("cheapest at position $minimalPosition. Score: $minimalScore")
}

fun crabScore(distance: Int): Int {
    return if (distance % 2 == 0) {
        (distance + 1) * (distance / 2)
    } else {
        (distance + 1) * (distance / 2) + (distance / 2) + 1
    }
}