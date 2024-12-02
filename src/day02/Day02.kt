package day02

import java.io.File
import kotlin.math.abs

fun main() {
    val lines = File("src/day02/input.txt").readLines()
    val parsed = lines.map { line -> line.split(" ").map { it.toInt() } }
    println(parsed.count { safe(it) })
    println(parsed.count { partialSafe(it) })
}

fun partialSafe(levels: List<Int>): Boolean {
    val options = levels.indices.map { index1 ->
        levels.filterIndexed { index2, _ -> index1 != index2 }
    }
    return options.any { safe(it) }
}

fun safe(levels: List<Int>): Boolean {
    val diffs = levels.zipWithNext {a, b -> a - b}
    if (!(diffs.all { it > 0 } || diffs.all { it < 0})) {
        return false
    }
    return diffs.all { abs(it) <= 3 };
}
