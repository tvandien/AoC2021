package aoc

import readFile

fun day4a() {
    val lines = readFile("day4_input.txt")

    val inputList = lines[0].split(",").map { it.toInt() }

    val boards = mutableListOf<List<List<Int>>>()
    for (i in 2..lines.size step 6) {
        boards.add(readBoard(lines.subList(i, i+5)))
    }

    for (i in inputList.indices) {
        val winner = hasWinner(boards, inputList.subList(0, i + 1))
        if (winner != null) {
            println("score: ${score(winner, inputList.subList(0, i + 1))}")
            break
        }
    }

}

fun readBoard(input: List<String>) : List<List<Int>> {
    val board = mutableListOf<List<Int>>()

    input.forEach { inputLine ->
        board.add(
            inputLine
                .split(" ")
                .filter { it != "" }
                .map { it.toInt() }
        )
    }

    return board
}

fun hasWinner(boards: List<List<List<Int>>>, numbers: List<Int>): List<List<Int>>? {
    for (board in boards) {
        for (i in 0..4) {
            if (board[i].all { it in numbers }) return board
            if (board.all { it[i] in numbers }) return board
        }
    }

    return null
}

fun score(board: List<List<Int>>, numbers:List<Int>): Int {
    return board.flatten().filter { it !in numbers }.sum() * numbers.last()
}

fun day4b() {
    val lines = readFile("day4_input.txt")

    val inputList = lines[0].split(",").map { it.toInt() }

    val boards = mutableListOf<List<List<Int>>>()
    for (i in 2..lines.size step 6) {
        boards.add(readBoard(lines.subList(i, i+5)))
    }

    var nonWinners = boards
    for (i in inputList.indices) {
        nonWinners = getNonWinners(nonWinners, inputList.subList(0, i + 1))
        if (nonWinners.size == 1) {
            findLoserWin(i, inputList, nonWinners)
            break
        }
    }
}

fun getNonWinners(boards: List<List<List<Int>>>, numbers: List<Int>): MutableList<List<List<Int>>> {
    val winners = mutableListOf<List<List<Int>>>()
    for (board in boards) {
        for (i in 0..4) {
            if (board[i].all { it in numbers } || board.all { it[i] in numbers }) {
                winners.add(board)
                break
            }
        }
    }

    return boards.filter { it !in winners }.toMutableList()
}

fun findLoserWin(start: Int, inputList: List<Int>, boards: MutableList<List<List<Int>>>) {
    for (i in start until inputList.size) {
        val winner = hasWinner(boards, inputList.subList(0, i + 1))
        if (winner != null) {
            println("score: ${score(winner, inputList.subList(0, i + 1))}")
            break
        }
    }
}