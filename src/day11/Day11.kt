package day11

import java.io.File

fun main() {
    val input = File("src/day11/input.txt").readText()
    var stones = input.split(" ").map { it.toLong() to 1L }.toMap().withDefault { 0 }
    println(stones)

    repeat(75) {
        val newStones = mutableMapOf<Long, Long>()
        for ((num, amount) in stones) {
            if (num == 0L) {
                newStones[1] = newStones.getOrDefault(1, 0) + amount
                continue
            }
            val s = num.toString()
            if (s.length % 2 == 0) {
                val first = s.take(s.length / 2)
                val second = s.drop(s.length / 2)
                newStones[first.toLong()] = newStones.getOrDefault(first.toLong(), 0) + amount
                newStones[second.toLong()] = newStones.getOrDefault(second.toLong(), 0) + amount
                continue
            }
            newStones[num * 2024] = newStones.getOrDefault(num * 2024, 0) + amount
        }
        stones = newStones
    }
    println(stones.map { (num, amount) -> amount }.sum())
}
