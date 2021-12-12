import aoc.*
import java.io.File

fun main() {
    day12a()
    day12b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
