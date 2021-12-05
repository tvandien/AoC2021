import aoc.*
import java.io.File

fun main() {
    day5a()
    day5b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
