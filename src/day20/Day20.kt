package day20

import day18.isWithinGrid
import utils.*
import java.io.File
import kotlin.math.abs

val dirs = listOf(Vector(1, 0), Vector(0, -1), Vector(-1, 0), Vector(0, 1))

fun main() {
    val input = File("src/day20/input.txt").readText()
    val grid = input.asCharGrid { it }
    val startPos = grid.positions.find { grid[it] == 'S' }!!
    val endPos = grid.positions.find { grid[it] == 'E' }!!
    grid[startPos] = '.'
    grid[endPos] = '.'
    //grid.toStringIndexed { pos, c -> c.toString() }.also { println(it) }
    println(grid.positions.filter { grid[it] == '#' }.size)
    val origPath = getPath(grid, startPos, endPos)

    val lenGrid = Grid<Int?>(grid.size) { pos -> null }
    for ((i, p) in (listOf(startPos) + origPath).withIndex()) {
        lenGrid[p] = i
    }
    lenGrid.positions.filter { grid[it] == '#' }.filter { pos -> dirs.product(dirs).any { (d1, d2) ->
        val p1 = pos + d1
        val p2 = pos + d2
        if (!p1.isWithinGrid(grid) || !p2.isWithinGrid(grid)) {
            return@any false
        }
        val v1 = lenGrid[p1]
        val v2 = lenGrid[p2]
        if (v1 == null || v2 == null) {
            return@any false
        }
        return@any abs(v1 - v2) >= 102
    } }.size.also { println(it) }

    /*
    val possibleCheats =
        (listOf(startPos) + origPath).flatMap { pos -> dirs.map { pos + it } }.filter { it.isWithinGrid(grid) }
            .filter { grid[it] == '#' }.toSet()
    println(possibleCheats.size)
    var numCheats = 0
    for ((i, cheat) in possibleCheats.withIndex()) {
        grid[cheat] = '.'
        val len = getPath(grid, startPos, endPos).size
        if (len <= origPath.size - 100) {
            numCheats++
        }
        grid[cheat] = '#'
        if (i % 100 == 0) println(i)
    }
    println(numCheats)
     */
}

fun getPath(grid: Grid<Char>, startPos: Vector, endPos: Vector): List<Vector> {
    val graph = HashmapGraph(
        grid.positions.filter { grid[it] == '.' }.map { pos ->
            pos to dirs.map { pos + it }.filter { it.isWithinGrid(grid) }.filter { grid[it] == '.' }
        }.toMap()
    )
    val path = graph.aStarShortestPath(
        startPos,
        endPos,
        { from, to -> 1.0 },
        { from -> abs(from.x - endPos.x) + abs(from.y - endPos.y) }
    )!!
    return path
}