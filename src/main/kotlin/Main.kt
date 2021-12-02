import aoc.*
import java.io.File

fun main() {
    day2a()
    day2b()
}

fun readFile(file: String): List<String> = File(file).readLines()
