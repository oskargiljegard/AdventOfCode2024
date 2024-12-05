package day04

import java.io.File

fun main() {
    val lines = File("src/day04/input.txt").readLines()
    val rotations = generateSequence(lines, ::rotate).take(4)
    println("PART 1")
    rotations.sumOf { xmases(it) + xmases(diags(it)) }.also { println(it) }
    println("PART 2")
    rotations.sumOf { xdashmases(it) }.also { println(it) }
}

fun xdashmases(mat: List<String>): Int {
    var total = 0
    for (y in 1..<(mat.size-1)) {
        for (x in 1..<(mat[0].length-1)) {
            if (mat[y][x] == 'A'
                && mat[y+1][x+1] == 'S'
                && mat[y+1][x-1] == 'S'
                && mat[y-1][x-1] == 'M'
                && mat[y-1][x+1] == 'M') {
                total++;
            }
        }
    }
    return total;
}

fun xmases(mat: List<String>): Int {
    val regex = "XMAS".toRegex()
    return mat.map {
        regex.findAll(it).count()
    }.sum()
}

fun rotate(mat: List<String>): List<String> {
    return List(mat[0].length) { y ->
        List(mat.size) { x ->
            mat[mat.size - x - 1][y]
        }.joinToString("")
    }
}

fun diags(mat: List<String>): List<String> {
    fun safeGet(x: Int, y: Int) = mat.getOrNull(y)?.getOrNull(x)
    val startPositions = mat[0].indices.map { it to 0 } + (1..mat.lastIndex).map { 0 to it }
    return startPositions.map { (sx, sy) ->
        generateSequence(sx to sy) { (x, y) -> x+1 to y+1 }.map { (x, y) -> safeGet(x, y) }.takeWhile { it != null }.joinToString("")
    }
}