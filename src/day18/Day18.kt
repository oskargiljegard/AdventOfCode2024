package day18

import utils.Grid
import utils.HashmapGraph
import utils.Vector
import utils.aStarShortestPath
import java.io.File

fun main() {
    val lines = File("src/day18/input.txt").readLines()
    val bytes = lines.map { l ->
        val (x, y) = l.split(",")
        return@map Vector(x.toInt(), y.toInt())
    }
    //val gridSize = Vector(7, 7)
    val gridSize = Vector(71, 71)
    val grid = Grid(gridSize) { '.' }
    for (b in bytes) {
        grid[b] = '#'
        val dirs = listOf(
            Vector(1, 0),
            Vector(0, -1),
            Vector(-1, 0),
            Vector(0, 1),
        )
        val graph = HashmapGraph<Vector>(
            grid.positions.filter { grid[it] == '.' }.map { pos ->
                pos to dirs.map { dir -> pos + dir }.filter { it.isWithinGrid(grid) }.filter { grid[it] == '.' }
            }.toMap()
        )
        val path = graph.aStarShortestPath(
            Vector(0, 0),
            grid.maxPos,
            { v1, v2 -> 1.0 },
            { v -> 99999999.0 }
        )
        if (path == null) {
            println("NO PATH")
            println(b)
        }
    }

    /*
    for (b in bytes.take(1024)) {
        grid[b] = '#'
    }
    println(bytes)
    grid.toStringIndexed { pos, c -> c.toString() }.also { println(it) }
    val dirs = listOf(
        Vector(1, 0),
        Vector(0, -1),
        Vector(-1, 0),
        Vector(0, 1),
    )
    val graph = HashmapGraph<Vector>(
        grid.positions.filter { grid[it] == '.' }.map { pos ->
            pos to dirs.map { dir -> pos + dir }.filter { it.isWithinGrid(grid) }.filter { grid[it] == '.' }
        }.toMap()
    )
    val path = graph.aStarShortestPath(
        Vector(0, 0),
        grid.maxPos,
        { v1, v2 -> 1.0 },
        { v -> 99999999.0 }
    )!!
    println(path.size)
     */
}

fun Vector.isWithinGrid(grid: Grid<Char>) = intX >= 0 && intY >= 0 && intX <= grid.maxPos.intX && intY <= grid.maxPos.intY
