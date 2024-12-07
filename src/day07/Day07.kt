package day07

import java.io.File

typealias Op = (Long, Long) -> Long

fun main() {
    val lines = File("src/day07/input.txt").readLines()
    val totals = lines.map { it.substringBefore(": ").toLong() }
    val numsList = lines.map { line -> line.substringAfter(": ").split(" ").map { it.toLong() } }
    val totalsWithNums = totals.zip(numsList)
    val ops1: List<Op> = listOf(
        { a, b -> a * b },
        { a, b -> a + b }
    )
    val ops2 = ops1 + listOf { a, b -> "$a$b".toLong() }
    totalsWithNums.filter { (t, n) -> possibleSums(n, ops1).any { it == t } }.sumOf { (t, n) -> t }.also { println(it) }
    totalsWithNums.filter { (t, n) -> possibleSums(n, ops2).any { it == t } }.sumOf { (t, n) -> t }.also { println(it) }
}

fun possibleSums(nums: List<Long>, ops: List<Op>): Sequence<Long> = sequence {
    if (nums.size < 2) {
        throw Error()
    }
    val (a, b) = nums
    val rest = nums.drop(2)
    val results = ops.map { op -> op(a, b) }
    if (rest.isEmpty()) {
        yieldAll(results)
    } else {
        yieldAll(results.flatMap { r -> possibleSums(listOf(r) + rest, ops) })
    }
}
