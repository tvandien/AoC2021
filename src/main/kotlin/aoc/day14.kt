package aoc

import readFile

fun day14a() {
    val lines = readFile("day14_input.txt")

    val polymer = StringBuilder(lines.first())
    val rules = lines.subList(2, lines.size).map { stringToReplacement(it) }

    println("polymer at i=0: $polymer")

    for (i in 1..10) {
        applyRules(polymer, rules)
    }

    val groups = polymer.toString()
        .groupBy { it }

    val leastCommon = groups.minOf { it.value.size }
    val mostCommon = groups.maxOf { it.value.size }

    println("answer: ${mostCommon - leastCommon}")
}

data class Replacement(val match: String, val insert: Char)

fun stringToReplacement(input: String): Replacement {
    val (match, insert) = input.split(" -> ")
    return Replacement(match, insert.first())
}

fun applyRules(polymer: StringBuilder, rules: List<Replacement>) {
    var i = 0

    while (i < polymer.length - 1) {
        for (rule in rules) {
            if (polymer.substring(i, i + 2) == rule.match) {
                polymer.insert(i + 1, rule.insert)
                i++
                break
            }
        }
        i++
    }
}

fun day14b() {
    val lines = readFile("day14_input.txt")

    val polymer = lines.first()
    val rules = lines.subList(2, lines.size).map { stringToReplacement(it) }

    var pairs = mutableMapOf<String, Long>()

    for (i in 0 until polymer.length - 1) {
        val pair = polymer.substring(i, i + 2)
        if (pairs.containsKey(pair)) {
            pairs[pair] = pairs[pair]!! + 1
        } else {
            pairs[pair] = 1
        }
    }

    for (i in 1..40) {
        pairs = iterate(pairs, rules)
    }

    val atoms = mutableMapOf<String, Long>()

    for (pair in pairs) {
        insertOrUpdate(atoms, pair.key.first().toString(), pair.value)
        insertOrUpdate(atoms, pair.key.last().toString(), pair.value)
    }

    val counts = atoms.map { atom ->
        if (atom.key.first() in listOf(polymer.first(), polymer.last())) {
            (atom.value / 2) + 1
        } else {
            atom.value / 2
        }
    }.sorted()

    println("solution: ${counts.last() - counts.first()}")
}

data class AtomPair(val pair: String, val count: Long)

fun iterate(pairs: Map<String, Long>, rules: List<Replacement>): MutableMap<String, Long> {
    val newPairs = mutableMapOf<String, Long>()

    for (pair in pairs) {
        if (rules.any { it.match == pair.key }) {
            val rule = rules.first { it.match == pair.key }
            val (a, b) = atomReplacement(pair.key, pair.value, rule.insert)

            insertOrUpdate(newPairs, a.pair, a.count)

            insertOrUpdate(newPairs, b.pair, b.count)
        }
    }

    return newPairs
}

private fun insertOrUpdate(newPairs: MutableMap<String, Long>, pair: String, count: Long) {
    if (newPairs.containsKey(pair)) {
        newPairs[pair] = newPairs[pair]!! + count
    } else {
        newPairs[pair] = count
    }
}

fun atomReplacement(pair: String, count: Long, insert: Char): Pair<AtomPair, AtomPair> {
    return Pair(
        AtomPair(pair.first().toString() + insert.toString(), count),
        AtomPair(insert.toString() + pair.last().toString(), count)
    )
}

