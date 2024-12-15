package day15

import utils.Vector
import utils.asCharGrid
import java.io.File

fun main() {
    part2()
}

fun part2() {
    val input = File("src/day15/input.txt").readText()
    val translation = mapOf(
        '#' to "##",
        'O' to "[]",
        '.' to "..",
        '@' to "@.",
    )
    val (gridInput, movesInput) = input.split("\n\n")
    val gridInputMapped =
        gridInput.lines().map { s -> s.map { translation.getValue(it) }.joinToString("") }.joinToString("\n")
    val grid = gridInputMapped.asCharGrid { it }
    val moves = movesInput.lines().joinToString("")
    val dirs = mapOf(
        '>' to Vector(1, 0),
        '^' to Vector(0, -1),
        '<' to Vector(-1, 0),
        'v' to Vector(0, 1)
    )
    grid.toStringIndexed { _, c -> c.toString() }.also { println(it) }
    println(moves)
    var robot = grid.positions.find { grid[it] == '@' }!!
    grid[robot] = '.'

    //for (dirChar in "<^") {
    for (dirChar in moves) {
        val dir = dirs.getValue(dirChar)
        if (dir.intY == 0) {
            // Horizontal movement
            val infLine = generateSequence(robot + dir) { it + dir }
            val line = infLine.takeWhile { "[]".contains(grid[it]) }.toList()
            if (line.isEmpty()) {
                if (grid[robot + dir] == '.') {
                    robot += dir
                }
            } else {
                if (grid[line.last() + dir] == '.') {
                    for (p in line.reversed()) {
                        grid[p + dir] = grid[p]
                    }
                    grid[line.first()] = '.'
                    robot += dir
                }
            }
        } else {
            // Vertical movement
            val infLine = generateSequence(setOf(robot)) { row ->
                row.flatMap { pos ->
                    when (grid[pos + dir]) {
                        ']' -> setOf(pos + dir, pos + dir + Vector(-1, 0))
                        '[' -> setOf(pos + dir, pos + dir + Vector(1, 0))
                        else -> emptySet()
                    }
                }.toSet()
            }
            val line = infLine.drop(1).map { row -> row.filter { "[]".contains(grid[it]) } }.takeWhile { it.isNotEmpty() }.toList()
            if (line.isEmpty()) {
                if (grid[robot + dir] != '#') {
                    robot += dir
                }
            } else {
                if (!line.any { row -> row.any { grid[it + dir] == '#' } }) {
                    for (row in line.reversed()) {
                        for (p in row) {
                            grid[p + dir] = grid[p]
                            grid[p] = '.'
                        }
                    }
                    robot += dir
                }
            }
        }
        require(grid[robot] == '.')
        //println(dirChar)
        //println(dir)
        //grid.toStringIndexed { p, c -> if (p == robot) "@" else c.toString() }.also { println(it) }
    }

    grid.positions.filter { grid[it] == '[' }.map { (x, y) -> 100 * y + x }.sum().also { println(it) }
}

fun part1() {
    val input = File("src/day15/input.txt").readText()
    val (gridInput, movesInput) = input.split("\n\n")
    val grid = gridInput.asCharGrid { it }
    val moves = movesInput.lines().joinToString("")
    val dirs = mapOf(
        '>' to Vector(1, 0),
        '^' to Vector(0, -1),
        '<' to Vector(-1, 0),
        'v' to Vector(0, 1)
    )
    grid.toStringIndexed { _, c -> c.toString() }.also { println(it) }
    println(moves)
    var robot = grid.positions.find { grid[it] == '@' }!!
    grid[robot] = '.'

    for (dirChar in moves) {
        val dir = dirs.getValue(dirChar)
        val infLine = generateSequence(robot + dir) { it + dir }
        val line = infLine.takeWhile { grid[it] == 'O' }.toList()
        if (line.isEmpty()) {
            if (grid[robot + dir] == '#') continue
            robot += dir
            continue
        }
        if (grid[line.last() + dir] == '#') continue
        grid[line.first()] = '.'
        grid[line.last() + dir] = 'O'
        robot += dir
    }

    grid.positions.filter { grid[it] == 'O' }.map { (x, y) -> 100 * y + x }.sum().also { println(it) }
}