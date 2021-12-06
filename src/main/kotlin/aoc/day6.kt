package aoc

import readFile

fun day6a(days: Int = 80) {
    val lines = readFile("day6_input.txt")

    val initialFish = lines.first().split(",").map { it.toInt() }

    var fishByAge = MutableList(9) { index ->
        initialFish.count { it == index }.toLong()
    }

    for (i in 0 until days) {
        fishByAge = simulateDay(fishByAge)
    }

    val count = fishByAge.sum()

    println("After $days days: $count")
}

fun simulateDay(today: MutableList<Long>): MutableList<Long> {
    return MutableList(9) { index ->
        when (index) {
            6 -> today[7] + today[0]
            else -> today[(index + 1) % 9]
        }
    }
}

fun day6b() {
    day6a(256)
}