package day10

import utils.Grid
import utils.Vector
import utils.asCharGrid
import java.io.File

fun main() {
    val input = File("src/day10/input.txt").readText()
    val grid = input.asCharGrid { if (it.isDigit()) it.digitToInt() else -9 }
    val startPositions = grid.positions.filter { grid[it] == 0 }.sortedBy { it.y*100 + it.x }
    /*
    for (s in startPositions) {
        println(s)
        println(trailScore(s, grid))
    }

     */
    startPositions.map { trailScore(it, grid) }.sum().also { println(it) }
}

fun trailScore(startPos: Vector, grid: Grid<Int>): Int {
    var numPaths = 0
    val deltas = listOf(Vector(1, 0), Vector(0, 1), Vector(-1, 0), Vector(0, -1))
    val peaks = mutableSetOf<Vector>()

    fun explore(pos: Vector) {
        if (grid[pos] == 9) {
            /*
            if (!peaks.contains(Vector(pos.intX, pos.intY))) {
                numPaths++
                peaks.add(Vector(pos.intX, pos.intY))
            }

             */
            numPaths++
            return
        }
        for (d in deltas) {
            val next = pos + d
            if (!next.isWithinGrid(grid)) {
                continue
            }
            if (grid[next] == grid[pos] + 1) {
                explore(next)
            }
        }
    }

    explore(startPos)
    return numPaths
}

fun Vector.isWithinGrid(grid: Grid<Int>) = intX >= 0 && intY >= 0 && intX <= grid.maxPos.intX && intY <= grid.maxPos.intY
