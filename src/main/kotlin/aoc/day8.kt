package aoc

import readFile

fun day8a() {
    val lines = readFile("day8_input.txt")

    var ones = 0
    var fours = 0
    var sevens = 0
    var eights = 0

    for (line in lines) {
        val (_, output) = line.split(" | ")

        val outputs = output.split(" ")

        ones += outputs.count { it.length == 2 }
        fours += outputs.count { it.length == 4 }
        sevens += outputs.count { it.length == 3 }
        eights += outputs.count { it.length == 7 }
    }

    println("ones: $ones, fours: $fours, sevens: $sevens, eights: $eights, sum: ${ones + fours + sevens + eights}")
}


fun day8b() {
    val lines = readFile("day8_input.txt")

    var totalSum = 0
    for (line in lines) {
        val (pre, post) = line.split(" | ")
        val display = SegmentDisplay()


        val inputs = pre.split(" ")

        for (input in inputs) {
            display.filter(input)
        }

        display.filterLengthFive(inputs.filter { it.length == 5 })
        display.filterLengthSix(inputs.filter { it.length == 6 })

        val outputs = post.split(" ")

        var sum = 0
        for (output in outputs) {
            sum = 10 * sum + display.getNumberFromInput(output)
        }

        totalSum += sum
    }
    println("Sum: $totalSum")
}
/*
  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg

 0000
1    2
1    2
 3333
4    5
4    5
 6666

 aaaa    aaaa    aaaa
.    c  .    c  b    .
.    c  .    c  b    .
 dddd    dddd    dddd
e    .  .    f  .    f
e    .  .    f  .    f
 gggg    gggg    gggg
*/
class SegmentDisplay {
    private val segments = MutableList(7) { "abcdefg"}

    fun filter(input: String) {
        when (input.length) {
            2 -> filter(input, 2, 5) // 1
            3 -> filter(input, 0, 2, 5) // 7
            4 -> filter(input, 1, 2, 3, 5) // 4
            7 -> filter(input, 0, 1, 2, 3, 4, 5, 6) // 8
        }
    }

    fun filterLengthFive(inputs: List<String>) {
        val uniques = getUniquesInList(inputs)

        filter(uniques, 1, 2, 4, 5)
    }

    fun filterLengthSix(inputs: List<String>) {
        val uniques = getUniquesInList(inputs)

        filter(uniques, 2, 3, 4)
    }

    private fun getUniquesInList(inputs: List<String>) =
        (inputs[0].filter { it !in inputs[1] || it !in inputs[2] } +
                inputs[1].filter { it !in inputs[0] || it !in inputs[2] } +
                inputs[2].filter { it !in inputs[0] || it !in inputs[1] })
            .toCharArray()
            .distinct()
            .joinToString("")

    private fun filter(input: String, vararg indices: Int): MutableList<String> {
        for (i in indices) {
            segments[i] = segments[i].filter { it in input }
        }

        for (i in 0..6) {
            if (i in indices) continue

            segments[i] = segments[i].filter { it !in input }
        }

        return segments
    }

    fun getNumberFromInput(input: String): Int {
        return when (input.length) {
            2 -> 1
            3 -> 7
            4 -> 4
            7 -> 8
            5 -> getNumberForLengthFive(input) // 2, 3, 5
            6 -> getNumberForLengthSix(input) // 0, 6, 9
            else -> throw Exception("Unknown input length")
        }
    }

    private fun getNumberForLengthFive(input: String): Int {
        // 2, 3, 5
        if (matchesFilter(input, 2, 4)) {
            return 2
        }

        if (matchesFilter(input, 2, 5)) {
            return 3
        }

        if (matchesFilter(input, 1, 5)) {
            return 5
        }

        throw Exception("Error Five!")
    }

    private fun getNumberForLengthSix(input: String): Int {
        // 0, 6, 9
        if (matchesFilter(input, 2, 4)) {
            return 0
        }

        if (matchesFilter(input, 3, 4)) {
            return 6
        }

        if (matchesFilter(input, 3, 5)) {
            return 9
        }

        throw Exception("Error Six!")
    }

    private fun matchesFilter(input: String, vararg indices: Int): Boolean {
        return indices.all { index ->
            segments[index].any { it in input }
        }
    }
}
