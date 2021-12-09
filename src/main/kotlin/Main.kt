import aoc.*
import java.io.File

fun main() {
    day9a()
    day9b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
