import aoc.*
import java.io.File

fun main() {
    day6a()
    day6b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
