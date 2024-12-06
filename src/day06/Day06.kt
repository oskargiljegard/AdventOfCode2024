package day06

import utils.Grid
import utils.Vector
import utils.asCharGrid
import java.io.File

fun main() {
    val input = File("src/day06/input.txt").readText()
    val grid = input.asCharGrid { it }
    println(grid.toStringIndexed { pos, c -> "${c}" })
    println("PART 1")
    part1(input)
    println("PART 2")
    part2(input)
}

fun part1(input: String) {
    val grid = input.asCharGrid { it }
    val startPos = grid.positions.find { grid[it] == '^' }!!
    println(startPos)
    var pos = startPos;
    var dir = Vector(0, -1)
    outer@ while (true) {
        grid[pos] = 'X'
        while (true) {
            val nextPos = pos + dir
            if (nextPos.x < 0 || nextPos.y < 0 || nextPos.x > grid.maxPos.x || nextPos.y > grid.maxPos.y) {
                break@outer;
            }
            if (grid[nextPos] == '#') {
                dir = dir.rotated(Math.PI / 2)
            } else {
                pos = nextPos
                break
            }
        }
    }
    println(grid.positions.filter { grid[it] == 'X' }.count())
}

data class State(val pos: Vector, val dir: Vector)
fun part2(input: String) {
    val originalGrid = input.asCharGrid { it }
    originalGrid.positions.filter {
        val grid = input.asCharGrid { it }
        if (grid[it] != '.') return@filter false
        grid[it] = '#'
        return@filter hasLoop(grid)
    }.size.also { println(it) }
}

fun hasLoop(grid: Grid<Char>): Boolean {
    val startPos = grid.positions.find { grid[it] == '^' }!!
    val explored: MutableSet<State> = mutableSetOf()
    var pos = startPos;
    var dir = Vector(0, -1)
    outer@ while (true) {
        if (explored.contains(State(pos, dir))) {
            return true
        } else {
            explored.add(State(pos, dir))
        }
        grid[pos] = 'X'
        while (true) {
            val nextPos = pos + dir
            if (nextPos.x < 0 || nextPos.y < 0 || nextPos.x > grid.maxPos.x || nextPos.y > grid.maxPos.y) {
                break@outer;
            }
            if (grid[nextPos] == '#') {
                dir = dir.rotated(Math.PI / 2)
                dir = Vector(dir.intX, dir.intY)
            } else {
                pos = nextPos
                break
            }
        }
    }
    return false
}