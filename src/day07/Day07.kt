package day07

import java.io.File

fun main() {
    val lines = File("src/day07/input.txt").readLines()
    val totals = lines.map { it.substringBefore(": ").toLong() }
    val nums = lines.map { line -> line.substringAfter(": ").split(" ").map { it.toLong() } }
    totals.zip(nums).filter { (t, n) -> isValid(t, n) }.map { (t, n) -> t }.sum().also { println(it) }
}

fun possibleSums(nums: List<Long>): Sequence<Long> = sequence {
    if (nums.size < 2) {
        throw Error()
    }
    val (n1, n2) = nums
    val rest = nums.drop(2)
    val s1 = n1*n2
    val s2 = n1+n2
    val s3 = "$n1$n2".toLong()
    if (rest.isEmpty()) {
        yield(s1)
        yield(s2)
        yield(s3)
    } else {
        yieldAll(possibleSums(listOf(s1) + rest))
        yieldAll(possibleSums(listOf(s2) + rest))
        yieldAll(possibleSums(listOf(s3) + rest))
    }
}

fun isValid(total: Long, nums: List<Long>): Boolean {
    return possibleSums(nums).any { it == total }
}
