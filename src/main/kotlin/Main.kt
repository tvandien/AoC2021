import aoc.*
import java.io.File

fun main() {
    day8a()
    day8b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
