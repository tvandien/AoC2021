import aoc.*
import java.io.File

fun main() {
    day13a()
    day13b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
