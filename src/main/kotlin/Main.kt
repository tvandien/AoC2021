import aoc.*
import java.io.File

fun main() {
    day4a()
    day4b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
