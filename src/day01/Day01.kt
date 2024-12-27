package day01

import java.io.File
import kotlin.math.abs

fun main() {
    val lines = File("src/day01/input.txt").readLines()
    val parsed = lines.map { line -> line.split("   ").map { it.toInt() } }
    val left = parsed.map { pair -> pair[0] }.sorted()
    val right = parsed.map { pair -> pair[1] }.sorted()
    val differences = left.zip(right).map { abs(it.first - it.second) }
    println("part 1")
    println(differences.sum())

    println("part 2")
    val similarities = left.map { leftVal ->
        leftVal * right.count { it == leftVal }
    }
    println(similarities.sum())

}
