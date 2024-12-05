package day05

import java.io.File

fun main() {
    val (rulesInput, linesInput) = File("src/day05/input.txt").readText().split("\n\n")
    val ruleNums = rulesInput.lines().map { l -> l.split("|").map { it.toInt() } }
    val uniqueStartNums = ruleNums.map { it[0] }.toSet().toList();
    val dict = uniqueStartNums.map { from -> from to ruleNums.filter { it[0] == from }.map { it[1] } }.toMap()
    println(dict)
    val lines = linesInput.lines().map { l -> l.split(",").map { it.toInt() } }
    println(lines[3])
    val line = lines[3]
    println(line.mapIndexed { i, _ ->
        line.subList(0, i+1)
    }.map { lineSeg ->
        val num = lineSeg.last()
        val mustBeAfter = dict.getOrDefault(num, emptyList())
        println(lineSeg)
        println(mustBeAfter)
        println("===")
        lineSeg.any { it in mustBeAfter }
    })
    val orderedLines = lines.filter { ordered(it, dict) }.also { println(it) }
    orderedLines.map { it[it.lastIndex/2] }.sum().also { println(it) }
    println("PART 2")
    val unOrderedLines = lines.filter { !ordered(it, dict) }.also { println(it) }
    unOrderedLines.map { line -> line.sortedWith(Comparator { a, b -> comp(a, b, dict)})}.also { println(it) }.map { it[it.lastIndex/2] }.sum().also { println(it) }
}

fun comp(a: Int, b: Int, dict: Map<Int, List<Int>>): Int {
    if (a in dict.getOrDefault(b, emptyList())) {
        return 1
    }
    if (b in dict.getOrDefault(a, emptyList())) {
        return -1
    }
    return 0
}

fun ordered(line: List<Int>, dict: Map<Int, List<Int>>): Boolean {
    return !line.mapIndexed { i, _ ->
        line.subList(0, i+1)
    }.map { lineSeg ->
        val num = lineSeg.last()
        val mustBeAfter = dict.getOrDefault(num, emptyList())
        lineSeg.any { it in mustBeAfter }
    }.any { it }
}
