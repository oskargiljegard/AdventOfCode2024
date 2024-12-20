package day19

import java.io.File

lateinit var towels: List<String>

// 199 wrong

fun main() {
    val lines = File("src/day19/input.txt").readLines()
    towels = lines[0].split(", ")
    val designs = lines.drop(2)
    println(towels)
    println(designs)
    println(designs.sumOf { hasSolution(it) })
}

fun hasSolution(design: String): Long {
    val numSolutions: Array<Long?> = Array(design.length) { null }
    for (i in design.indices.reversed()) {
        var solutionsAtPos = 0L
        for (j in i + 1..design.length) {
            val part = design.substring(i, j)
            if (towels.any { it == part }) {
                solutionsAtPos += if (j >= design.length) 1 else numSolutions[j]!!
            }
        }
        numSolutions[i] = solutionsAtPos
    }
    println(numSolutions.toList())
    return numSolutions[0]!!
}