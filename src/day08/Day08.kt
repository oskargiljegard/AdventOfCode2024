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
    getAllAntiNodesInMap(grid, types) { drop(1).take(1) }.also { println(it) }
    getAllAntiNodesInMap(grid, types) { takeWhile { it.isWithinGrid(grid) }}.also { println(it) }
}

fun Vector.isWithinGrid(grid: Grid<Char>) = intX >= 0 && intY >= 0 && intX <= grid.maxPos.intX && intY <= grid.maxPos.intY

fun getAllAntiNodesInMap(grid: Grid<Char>, types: List<Char>, limitAntiNodes: Sequence<Vector>.() -> Sequence<Vector>): Int {
    val allAntiNodes = mutableListOf<Vector>()
    for (type in types) {
        val antennas = grid.positions.filter { grid[it] == type }
        val antennaPairs = antennas.product(antennas).filter { (a, b) -> a != b }
        val antiNodes = antennaPairs.flatMap { (a, b) -> generateSequence(b) { it + (b - a) }.limitAntiNodes() }
        allAntiNodes.addAll(antiNodes)
    }
    val allAntiNodesInMap =
        allAntiNodes.map { Vector(it.intX, it.intY) }.toSet()
            .filter { it.isWithinGrid(grid) }
    return allAntiNodesInMap.size
}
