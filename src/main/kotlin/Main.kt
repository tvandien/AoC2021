import aoc.*
import java.io.File

fun main() {
    day7a()
    day7b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
