package aoc

import readFile
import java.util.*

fun day12a() {
    val lines = readFile("day12_input.txt")

    val graph = buildGraph(lines)
    val pathCount = countPaths(graph)

    println("paths: $pathCount")
}

fun buildGraph(lines: List<String>): List<Connection> {
    val graph = mutableListOf<Connection>()

    for (line in lines) {
        val (a, b) = line.split("-")
        if (b != "start")
            graph.add(Connection(a, b))
        if (a != "start")
            graph.add(Connection(b, a))
    }

    return graph
}

fun countPaths(graph: List<Connection>, allowSingleSmallCave: Boolean = false): Int {
    val openPaths = Stack<Path>()
    openPaths.push(Path("start", mutableListOf()))
    val finishedPaths = mutableListOf<Path>()

    while (openPaths.isNotEmpty()) {
        val path = openPaths.pop()

        val unusedConnectionsFromCurrentLocation = graph
            .filter { connection -> connection.start == path.currentLocation() }
            .filter { connection -> path.isValidNewConnection(connection, allowSingleSmallCave) }

        for (connection in unusedConnectionsFromCurrentLocation) {
            if (connection.end == "end") {
                finishedPaths.add(path.addToPath(connection))
            } else {
                openPaths.push(path.addToPath(connection))
            }
        }
    }

    return finishedPaths.count()
}

data class Connection(val start: String, val end: String) {
    fun isSmallCave(): Boolean {
        return end.uppercase() != end
    }
}

data class Path(val origin: String, val connections: MutableList<Connection>) {
    fun currentLocation(): String {
        return if (connections.isNotEmpty()) {
            connections.last().end
        } else {
            origin
        }
    }

    fun addToPath(connection: Connection): Path {
        val newPath = connections.toMutableList()
        newPath.add(connection)

        return Path(origin, newPath)
    }

    fun isValidNewConnection(connection: Connection, allowSingleSmallCave: Boolean): Boolean {
        return !connection.isSmallCave() ||
                (allowSingleSmallCave && !smallCavesVisitedTwice()) ||
                connection.end !in connections.map { it.end }
    }

    private fun smallCavesVisitedTwice(): Boolean {
        return connections
            .filter { it.isSmallCave() }
            .any { connection -> connections.count { it.end == connection.end } > 1 }
    }

    override fun toString(): String {
        var result = origin

        for (connection in connections) {
            result += "-${connection.end}"
        }

        return result
    }
}

fun day12b() {
    val lines = readFile("day12_input.txt")

    val graph = buildGraph(lines)
    val pathCount = countPaths(graph, true)

    println("paths: $pathCount")
}