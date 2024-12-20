package day20

import day18.isWithinGrid
import utils.*
import java.io.File
import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

val dirs = listOf(Vector(1, 0), Vector(0, -1), Vector(-1, 0), Vector(0, 1))

fun main() {
    val input = File("src/day20/input.txt").readText()
    val grid = input.asCharGrid { it }
    val startPos = grid.positions.find { grid[it] == 'S' }!!
    val endPos = grid.positions.find { grid[it] == 'E' }!!
    grid[startPos] = '.'
    grid[endPos] = '.'
    grid.toStringIndexed { pos, c -> c.toString() }.also { println(it) }
    println()
    val distGrid = makeDistGrid(grid, endPos)
    distGrid.toStringIndexed { pos, c -> c?.toString() ?: "." }.also { println(it) }

    val cheats = distGrid.positions.filter { distGrid[it] != null }.flatMap { from ->
        distGrid.positions.filter { distGrid[it] != null }.mapNotNull inner@{ to ->
            val dist = abs(from.intX - to.intX) + abs(from.intY - to.intY)
            if (dist > 20) {
                return@inner null
            }
            val saving = distGrid[from]!! - distGrid[to]!! - dist
            if (saving < 100) {
                return@inner null
            }
            return@inner Triple(from, to, saving)
        }
    }
    println(cheats.size)
    //println(cheats.map { (from, to, saving) -> saving }.groupingBy { it }.eachCount())
}

fun makeDistGrid(grid: Grid<Char>, endPos: Vector): Grid<Int?> {
    val distGrid: Grid<Int?> = gridOf(grid.size, null)
    val closed: MutableSet<Vector> = mutableSetOf()
    val open: Queue<Vector> = LinkedList()
    open.add(endPos)
    distGrid[endPos] = 0
    while (open.isNotEmpty()) {
        val pos = open.remove()
        closed.add(pos)
        val neighbors = dirs.map { dir -> pos + dir }
            .filter { it.isWithinGrid(grid) }
            .filter { grid[it] == '.' }
            .filter { !closed.contains(it) }
        for (n in neighbors) {
            distGrid[n] = distGrid[pos]!! + 1
            open.add(n)
        }
    }
    return distGrid
}