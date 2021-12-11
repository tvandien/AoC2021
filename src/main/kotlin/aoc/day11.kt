package aoc

import readFile

fun day11a() {
    val lines = readFile("day11_input.txt")

    val octopi = lines.map { line -> line.map { it.toString().toInt() }.toMutableList() }
    var flashes = 0

    for (i in 1..100) {
        flashes += simulate(octopi)
    }

    println("flashes after 100 cycles: $flashes")
}

fun simulate(octopi: List<MutableList<Int>>): Int {
    var flashes = 0
    // increase all by one
    octopi.forEachIndexed { y, line ->
        line.forEachIndexed { x, _ ->
            octopi[y][x]++
        }
    }

    // find flashes
    while (octopi.flatten().any { it > 9 }) {
        octopi.forEachIndexed { y, line ->
            line.forEachIndexed { x, octopus ->
                if (octopus > 9) {
                    flashes++
                    octopi[y][x] = 0
                    incrementNeighbors(octopi, x, y)
                }
            }
        }
    }

    return flashes
}

fun incrementNeighbors(octopi: List<MutableList<Int>>, x: Int, y: Int) {
    for (offsetY in -1..1) {
        for (offsetX in -1..1) {
            if (offsetY == 0 && offsetX == 0) continue
            if (!inBounds(octopi, x + offsetX, y + offsetY)) continue

            if (octopi[y + offsetY][x + offsetX] > 0) {
                octopi[y + offsetY][x + offsetX]++
            }
        }
    }
}

fun inBounds(octopi: List<MutableList<Int>>, x: Int, y: Int): Boolean {
    return x >= 0 && x < octopi[0].size &&
            y >= 0 && y < octopi.size
}

fun day11b() {
    val lines = readFile("day11_input.txt")

    val octopi = lines.map { line -> line.map { it.toString().toInt() }.toMutableList() }

    for (i in 1..1000) {
        val newFlashes = simulate(octopi)

        if (newFlashes == 100) {
            println("100 flashes after $i cycles")
            return
        }
    }

    println("Never happens :(")
}