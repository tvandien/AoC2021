package aoc

import readFile
import java.lang.Integer.max
import kotlin.math.abs

data class Point(val x: Int, val y: Int)

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    fun getPoints(diagonal: Boolean): List<Point> {
        val dx = if (x2 - x1 == 0) 0 else (x2 - x1) / abs(x2 - x1)
        val dy = if (y2 - y1 == 0) 0 else (y2 - y1) / abs(y2 - y1)

        if (dx != 0 && dy != 0 && !diagonal) return listOf()

        var currentPoint = Point(x1, y1)
        val points = mutableListOf(currentPoint)

        while (currentPoint.x != x2 || currentPoint.y != y2) {
            currentPoint = Point(currentPoint.x + dx, currentPoint.y + dy)
            points.add(currentPoint)
        }

        return points
    }
}

fun stringToLine(input: String): Line {
    val (left, right) = input.split(" -> ")
    val (x1, y1) = left.split(",")
    val (x2, y2) = right.split(",")
    return Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
}

fun printMap(map: MutableList<MutableList<Int>>) {
    for (row in map) {
        for (point in row) {
            print(if (point == 0) "." else point)
        }
        println()
    }
}

fun day5a(diagonal: Boolean = false) {
    val lines = readFile("day5_input.txt")

    val linesToDraw = lines.map { stringToLine(it) }

    val width = linesToDraw.map { max(it.x1, it.x2) }.maxOf { it }
    val height = linesToDraw.map { max(it.y1, it.y2) }.maxOf { it }

    val map = List(height + 1) { MutableList(width + 1) { 0 } }

    linesToDraw.forEach { line ->
        line.getPoints(diagonal).forEach { point ->
            map[point.y][point.x] += 1
        }
    }

    val scary = map.flatten().filter { it > 1 }.size

    println("Scary points: $scary")
}

fun day5b() {
    day5a(true)
}

