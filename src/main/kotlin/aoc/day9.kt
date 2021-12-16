package aoc

import readFile

fun day9a() {
    val lines = readFile("day9_input.txt")

    val grid = lines.map { line -> line.map { cell -> cell.toString().toInt()}}

    val lowPoints = mutableListOf<Int>()

    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (lowPoint(grid, x, y)) {
                lowPoints.add(grid[y][x])
            }
        }
    }

    println("sum ${lowPoints.sum() + lowPoints.size}")
}

fun lowPoint(grid: List<List<Int>>, x: Int, y: Int): Boolean {
    return lowerThan(grid, x, y, x-1, y) &&
            lowerThan(grid, x, y, x+1, y) &&
            lowerThan(grid, x, y, x, y-1) &&
            lowerThan(grid, x, y, x, y+1)
}

fun lowerThan(grid: List<List<Int>>, x1 : Int, y1: Int, x2: Int, y2: Int): Boolean {
    if (!inBounds(grid, x2, y2)) {
        return true
    }

    return (grid[y1][x1] < grid[y2][x2])
}

fun inBounds(grid: List<List<Int>>, x: Int, y: Int): Boolean =
    0 <= x && x < grid[0].size && 0 <= y && y < grid.size

fun day9b() {
    val lines = readFile("day9_input.txt")

    val grid = lines.map { line -> line.map { cell -> cell.toString().toInt()}}

    val lowPoints: MutableList<Coordinate> = mutableListOf()

    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (lowPoint(grid, x, y)) {
                lowPoints.add(Coordinate(x, y))
            }
        }
    }

    val basinSizes = lowPoints.map { findBasinSize(grid, it) }
    val threeLargestProduct = basinSizes
        .sortedDescending()
        .subList(0, 3)
        .reduce { acc, it -> acc * it }

    println("product: $threeLargestProduct")
}

fun findBasinSize(grid: List<List<Int>>, coordinate: Coordinate): Int {
    val coordinatesToCheck = mutableListOf(coordinate)
    val coordinatesInBasin = mutableListOf<Coordinate>()

    while (coordinatesToCheck.size > 0) {
        val currentCoordinate = coordinatesToCheck.first()
        coordinatesToCheck.removeAt(0)

        val higherNeighbors = findHigherNeighbors(grid, currentCoordinate)
        val newCoordinates = higherNeighbors.filter { neighbor ->
            neighbor !in coordinatesInBasin &&
                    neighbor !in coordinatesToCheck &&
                    grid[neighbor.y][neighbor.x] != 9
        }

        coordinatesToCheck.addAll(newCoordinates)
        coordinatesInBasin.add(currentCoordinate)
    }

    return coordinatesInBasin.size
}

fun findHigherNeighbors(grid: List<List<Int>>, coordinate: Coordinate): List<Coordinate> {
    return coordinate.neighbors().filter { neighbor -> higherThan(grid, coordinate, neighbor) }
}

fun higherThan(grid: List<List<Int>>, coordinate1: Coordinate, coordinate2: Coordinate): Boolean {
    if (!inBounds(grid, coordinate2.x, coordinate2.y)) {
        return false
    }

    return (grid[coordinate1.y][coordinate1.x] < grid[coordinate2.y][coordinate2.x])
}

data class Coordinate(val x: Int, val y: Int) {
    private fun left(): Coordinate = Coordinate(x-1, y)
    private fun right(): Coordinate = Coordinate(x+1, y)
    private fun up(): Coordinate = Coordinate(x, y-1)
    private fun down(): Coordinate = Coordinate(x, y+1)

    fun neighbors(): List<Coordinate> = listOf(left(), right(), up(), down())

    fun inboundNeighbors(width: Int, height: Int): List<Coordinate> {
        return neighbors().filter { neighbor ->
            neighbor.x in 0 until width && neighbor.y in 0 until height
        }
    }
}