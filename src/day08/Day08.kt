package day08

import utils.Grid
import utils.Vector
import utils.asCharGrid
import utils.product
import java.io.File

fun main() {
    val input = File("src/day08/input.txt").readText()
    val grid = input.asCharGrid { it }
    grid.toStringIndexed { pos, c -> c.toString() }.also { println(it) }
    val types = grid.positions.map { grid[it] }.filter { it != '.' }.toSet().toList()
    var total = 0
    val allAntiNodes = mutableListOf<Vector>()
    for (type in types) {
        val antennas = grid.positions.filter { grid[it] == type }
        val antennaPairs = antennas.product(antennas).filter { (a, b) -> a != b }
        val antiNodes = antennaPairs.flatMap { (a, b) -> generateSequence(b, { it + (b - a) }).takeWhile { it.isWithinGrid(grid) } }
        allAntiNodes.addAll(antiNodes)
    }
    val antiNodesInMap =
        allAntiNodes.map { Vector(it.intX, it.intY) }.toSet()
            .filter { it.intX >= 0 && it.intY >= 0 && it.intX <= grid.maxPos.intX && it.intY <= grid.maxPos.intY }
    println(antiNodesInMap.size)
}

fun Vector.isWithinGrid(grid: Grid<Char>) = intX >= 0 && intY >= 0 && intX <= grid.maxPos.intX && intY <= grid.maxPos.intY

