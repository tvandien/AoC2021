package aoc

import readFile

fun day1a() {
    val lines = readFile("src/main/resources/day1A_input.txt")

    var last: Int = lines.first().toInt()
    var increases = 0

    for (line in lines) {
        if (line.toInt() > last) {
            increases++
        }
        last = line.toInt()
    }

    println("$increases increases")
}

fun day1b() {
    val lines = readFile("src/main/resources/day1A_input.txt")
    val offset = 3
    var last = 0
    var increases = 0

    last = lines.subList(0, offset - 1).sumOf { it.toInt() }

    for (i in offset until lines.size) {
        val count = last - lines[i-offset].toInt() + lines[i].toInt()

        if (count > last) {
            increases++
        }

        last = count
    }

    println("$increases increases")
}