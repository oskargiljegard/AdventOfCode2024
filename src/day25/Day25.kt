package day25

import utils.Grid
import utils.Vector
import utils.asCharGrid
import java.io.File

fun main() {
    val input = File("src/day25/input.txt").readText()
    val grids = input.split("\n\n").map { it.asCharGrid { it } }
    val keyGrids = grids.filter { g -> (g.xPositions).all { g[Vector(it, g.maxPos.intY)] == '#' } }
    val lockGrids = grids.filter { g -> (g.xPositions).all { g[Vector(it, g.maxPos.intY)] == '.' } }
    val keyHeights = keyGrids.map { getHeights(it) }
    val lockHeights = lockGrids.map { getHeights(it) }
    var fits = 0
    for (k in keyHeights) {
        for (l in lockHeights) {
            if (k.zip(l).map { (a, b) -> a + b }.all { it < 8 }) {
                fits++
            }
        }
    }
    println(fits)
}

fun getHeights(grid: Grid<Char>): List<Int> {
    return grid.xPositions.map { x ->
        grid.yPositions.filter { y ->
            grid[Vector(
                x,
                y
            )] == '#'
        }.size
    }
}
