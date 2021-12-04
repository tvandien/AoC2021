package aoc

import readFile


fun day2a() {
    val lines = readFile("day2A_input.txt")

    var horizontalPosition = 0
    var depth = 0

    for (line in lines) {
        val (command, distance) = line.split(" ")

        when (command) {
            "forward" -> horizontalPosition += distance.toInt()
            "down" -> depth += distance.toInt()
            "up" -> depth -= distance.toInt()
        }
    }

    println("Position ($horizontalPosition, $depth): ${horizontalPosition*depth}")
}

fun day2b() {
    val lines = readFile("day2A_input.txt")

    var horizontalPosition = 0
    var depth = 0
    var aim = 0

    for (line in lines) {
        val (command, distance) = line.split(" ")

        when (command) {
            "forward" -> {
                horizontalPosition += distance.toInt()
                depth += aim * distance.toInt()
            }
            "down" -> aim += distance.toInt()
            "up" -> aim -= distance.toInt()
        }
    }

    println("Position ($horizontalPosition, $depth): ${horizontalPosition*depth}")
}