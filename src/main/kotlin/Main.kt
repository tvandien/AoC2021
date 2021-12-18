import aoc.*
import java.io.File

fun main() {
    day18a()
    day18b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
