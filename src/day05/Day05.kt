package day05

import java.io.File

lateinit var dict: Map<Int, List<Int>>

fun main() {
    val (rulesInput, linesInput) = File("src/day05/input.txt").readText().split("\n\n")
    val ruleNums = rulesInput.lines().map { l -> l.split("|").map { it.toInt() } }
    val uniqueStartNums = ruleNums.map { it[0] }.toSet().toList();
    dict = uniqueStartNums.associateWith { from -> ruleNums.filter { it[0] == from }.map { it[1] } }.withDefault { emptyList() }
    val lines = linesInput.lines().map { l -> l.split(",").map { it.toInt() } }
    val linesWithSorted = lines.zip(lines.map { it.sortedWith { a, b -> comp(a, b) } })
    println("PART 1")
    linesWithSorted.filter { (l, s) -> l == s }.map{(l,s) -> l}.sumOf { it[it.lastIndex / 2] }.also { println(it) }
    println("PART 2")
    linesWithSorted.filter { (l, s) -> l != s }.map{(l,s) -> s}.sumOf { it[it.lastIndex / 2] }.also { println(it) }
}

fun comp(a: Int, b: Int): Int {
    if (a in dict.getValue(b)) return 1
    if (b in dict.getValue(a)) return -1
    return 0
}
