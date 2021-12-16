package aoc

import readFile
import kotlin.math.min

fun day15a() {
    val lines = readFile("day15_input.txt")

    val cost = lines.map { line -> line.map { cell -> cell.toString().toInt() } }

    findLowestCost(cost)

}

fun findLowestCost(cost: List<List<Int>>) {
    val totalCost = MutableList(cost.size) { MutableList(cost[0].size) { 0 } }
    val coordinatesToCheck = mutableMapOf(Pair(Coordinate(0, 0), 0))

    while (coordinatesToCheck.isNotEmpty()) {
        val coordinate = coordinatesToCheck.minByOrNull { cost[it.key.y][it.key.x] + it.value }!!
        coordinatesToCheck.remove(coordinate.key)

        totalCost[coordinate.key.y][coordinate.key.x] = coordinate.value + cost[coordinate.key.y][coordinate.key.x]

        val neighbors = coordinate.key.inboundNeighbors(cost.first().size, cost.size)

        for (neighbor in neighbors) {
            if (totalCost[neighbor.y][neighbor.x] != 0) continue
            coordinatesToCheck[neighbor] = min(
                coordinatesToCheck[neighbor] ?: Int.MAX_VALUE,
                totalCost[coordinate.key.y][coordinate.key.x]
            )
        }
    }

    println("cost: ${totalCost.last().last() - totalCost.first().first()}")
}

fun day15b() {
    val lines = readFile("day15_input.txt")

    val cost = lines.map { line -> line.map { cell -> cell.toString().toInt() } }

    val bigCost = MutableList(cost.size * 5) { y ->
        MutableList(cost.first().size * 5) { x ->
            val height = cost.size
            val width = cost.first().size
            val increase = x / width + y / height

            (cost[y % height][x % width] + increase - 1) % 9 + 1

        }
    }

    findLowestCost(bigCost)
}