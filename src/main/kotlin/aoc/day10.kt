package aoc

import readFile
import java.util.*

fun day10a() {
    val lines = readFile("day10_input.txt")

    val score = lines
        .sumOf { getScoreForLine(it, false) }

    println("result: $score")
}

val closers = setOf('}', ']',']', '>') //"}])>"
val openers = setOf('{', '[', '(', '<') //"{[(<"

val openerToCloser = mapOf(Pair('{', '}'), Pair('[', ']'), Pair('(', ')'), Pair('<', '>'))

fun getScoreForLine(line: String, incomplete: Boolean): Long {
    val stack = Stack<Char>()

    for (character in line) {
        if (character in openers) {
            stack.push(character)
            continue
        }

        if (character != openerToCloser[stack.pop()]) {
            if (!incomplete) {
                return getScoreForIncorrectCloser(character)
            }
            return 0
        }
    }

    if (!incomplete) return 0

    if (stack.isNotEmpty()) {
        return getScoreForAutoComplete(stack)
    }

    return 0
}

fun getScoreForIncorrectCloser(input: Char): Long {
    return when (input) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> 0
    }
}

val openerToScore = mapOf(Pair('(', 1), Pair('[', 2), Pair('{', 3), Pair('<', 4))

fun getScoreForAutoComplete(input: Stack<Char>): Long {
    var score = 0L
    while (input.isNotEmpty()) {
        val c = input.pop()
        score = score * 5 + openerToScore[c]!!
    }
    return score
}

fun day10b() {
    val lines = readFile("day10_input.txt")

    val scores = lines
        .map { getScoreForLine(it, true) }
        .filter { it != 0L }
        .sorted()

    val middle = scores[scores.size / 2]

    println("result: $middle")
}