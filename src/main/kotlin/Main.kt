import aoc.*
import java.io.File

fun main() {
    day11a()
    day11b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
