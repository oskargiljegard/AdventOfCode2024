package day12

import utils.Grid
import utils.Vector
import utils.asCharGrid
import java.io.File

fun main() {
    val input = File("src/day12/input.txt").readText()
    val grid = input.asCharGrid { it }
    val explored = mutableSetOf<Vector>()
    val dirs = listOf(Vector(1, 0), Vector(0, -1), Vector(-1, 0), Vector(0, 1))

    var sum = 0
    for (startPos in grid.positions) {
        val region = mutableSetOf<Vector>()
        val fenceRegions = mutableSetOf<Pair<Vector, String>>()
        var area = 0
        var perimeter = 0
        fun explore(pos: Vector) {
            explored.add(pos)
            region.add(pos)
            area++
            for (dir in dirs) {
                val next = pos + dir
                if (!next.isWithinGrid(grid)) {
                    perimeter++
                    val desc = if (dir.x < 0 || dir.y < 0) "${grid[pos]}." else ".${grid[pos]}"
                    fenceRegions.add(pos + dir / 2 to desc)
                    continue
                }
                if (grid[next] != grid[pos]) {
                    perimeter++
                    val desc = if (dir.x < 0 || dir.y < 0) "${grid[pos]}." else ".${grid[pos]}"
                    fenceRegions.add(pos + dir / 2 to desc)
                    continue
                }
                if (explored.contains(next)) continue
                explore(next)
            }
        }
        if (explored.contains(startPos)) continue
        explore(startPos)

        println(region)
        println(fenceRegions)
        println(grid[startPos])
        println(area)
        println(perimeter)

        val sides = mutableSetOf<Vector>()
        for ((f, s) in fenceRegions) {
            if (f.x != f.intX.toDouble() && f.y != f.intY.toDouble()) {
                throw Error("bad fences")
            }
            if (f.x == f.intX.toDouble() && f.y == f.intY.toDouble()) {
                throw Error("bad fences")
            }
            if (f.x != f.intX.toDouble()) {
                if (!fenceRegions.contains(f + Vector(0, 1) to s)) {
                    sides.add(f)
                }
            }
            if (f.y != f.intY.toDouble()) {
                if (!fenceRegions.contains(f + Vector(1, 0) to s)) {
                    sides.add(f)
                }
            }
        }
        println(sides)
        println(sides.size)
        println("$area * ${sides.size} = ${area * sides.size}")
        println()
        sum += area * sides.size
    }
    println(sum)
}

//fun getSides(region: Set<Vector>): Int {
//}

fun Vector.isWithinGrid(grid: Grid<Char>) = intX >= 0 && intY >= 0 && intX <= grid.maxPos.intX && intY <= grid.maxPos.intY
