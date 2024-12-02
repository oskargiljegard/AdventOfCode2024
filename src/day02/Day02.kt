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
    //val diffs = levels.zipWithNext {a, b -> a - b}
    //val unifiedLevels = if (diffs.count { it < 0 } > diffs.count { it > 0 }) levels.reversed() else levels
    val options = levels.mapIndexed { index1, i ->
        levels.filterIndexed { index2, i -> index1 != index2 }
    }
    return options.any { safe(it) }
}

fun safe(levels: List<Int>): Boolean {
    val diffs = levels.zipWithNext {a, b -> a - b}
    if (diffs.all { it > 0 } || diffs.all { it < 0}) {
        if (diffs.all { abs(it) <= 3 }) {
            return true;
        }
    }
    return false;
}
