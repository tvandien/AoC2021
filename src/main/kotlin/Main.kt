import aoc.*
import java.io.File

fun main() {
    day15a()
    day15b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
