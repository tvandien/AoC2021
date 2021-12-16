import aoc.*
import java.io.File

fun main() {
    day16a()
    day16b()
}

fun readFile(file: String): List<String> = File("src/main/resources/$file").readLines()
