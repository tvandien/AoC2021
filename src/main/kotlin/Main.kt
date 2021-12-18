import aoc.*
import java.io.File

fun main() {
    day17a()
    day17b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
