package day21

import day08.isWithinGrid
import utils.Graph
import utils.Grid
import utils.Vector
import utils.asCharGrid
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.abs

val dirs = mapOf(
    '>' to Vector(1, 0),
    '^' to Vector(0, -1),
    '<' to Vector(-1, 0),
    'v' to Vector(0, 1)
)

// wrong 223838

fun main() {
    val inputLines = File("src/day21/input.txt").readLines()
    val numericKeypad = """
        789
        456
        123
        X0A
    """.trimIndent().asCharGrid { it }
    val directionalKeypad = """
        X^A
        <v>
    """.trimIndent().asCharGrid { it }
    println(numericKeypad.toStringIndexed { _, c -> c.toString() })
    println(directionalKeypad.toStringIndexed { _, c -> c.toString() })
    println(inputLines)

    var total = 0
    for (str in inputLines) {
        val manualPath = shortestManualPath(numericKeypad, directionalKeypad, str, 25)
        val numPart = str.filter { it.isDigit() }.toInt()
        println("${manualPath.length} * $numPart")
        total += manualPath.length * numPart
    }
    println("Total is $total")

}

fun shortestManualPath(numericKeypad: Grid<Char>, directionalKeypad: Grid<Char>, str: String, numMiddles: Int): String {
    val numericStartPos = numericKeypad.positions.find { numericKeypad[it] == 'A' }!!
    val directionalStartPos = directionalKeypad.positions.find { directionalKeypad[it] == 'A' }!!
    val firstRobotPaths = shortestTypingPaths(numericKeypad, str, numericStartPos)
    //val secondRobotPaths = firstRobotPaths.flatMap { shortestTypingPaths(directionalKeypad, it, directionalStartPos) }
    //val manualPaths = secondRobotPaths.flatMap { shortestTypingPaths(directionalKeypad, it, directionalStartPos) }
    var paths = firstRobotPaths
    for (i in 0..<numMiddles) {
        paths = paths.flatMap { shortestTypingPaths(directionalKeypad, it, directionalStartPos) }
        val shortestPath = paths.minBy { it.length }
        paths = paths.filter { it.length == shortestPath.length }
        println(i)
        println(paths.size)
    }
    return paths.minBy { it.length }
}


fun shortestTypingPaths(grid: Grid<Char>, str: String, from: Vector): List<String> {
    if (str.isEmpty()) return listOf("")
    val char = str.first()
    val restStr = str.drop(1)
    val charPos = grid.positions.find { grid[it] == char }!!
    return shortestSubPaths(grid, from, charPos).flatMap { pathToChar ->
        val remPaths = shortestTypingPaths(grid, restStr, charPos)
        return@flatMap remPaths.map { remPath -> pathToChar + 'A' + remPath }
    }
}

fun shortestSubPaths(grid: Grid<Char>, from: Vector, to: Vector): List<String> {
    val delta = to - from
    val xChar = if (delta.intX > 0) ">" else "<"
    val yChar = if (delta.intY > 0) "v" else "^"
    if (delta.intX == 0 && delta.intY == 0) {
        return listOf("")
    }
    if (delta.intX == 0) {
        return listOf(yChar.repeat(abs(delta.intY)))
    }
    if (delta.intY == 0) {
        return listOf(xChar.repeat(abs(delta.intX)))
    }
    val horVert =
        xChar.repeat(abs(delta.intX)) + yChar.repeat(abs(delta.intY))
    val vertHor =
        yChar.repeat(abs(delta.intY)) + xChar.repeat(abs(delta.intX))
    if (grid[Vector(0, grid.maxPos.intY)] == 'X') {
        if (from.intX == 0 && to.intY == grid.maxPos.intY) {
            // Cannot go vertHor
            return listOf(horVert)
        }
        if (to.intX == 0 && from.intY == grid.maxPos.intY) {
            // Cannot go horVert
            return listOf(vertHor)
        }
    }
    if (grid[Vector(0, 0)] == 'X') {
        if (from.intX == 0 && to.intY == 0) {
            // Cannot go vertHor
            return listOf(horVert)
        }
        if (to.intX == 0 && from.intY == 0) {
            // Cannot go horVert
            return listOf(vertHor)
        }
    }
    return listOf(horVert, vertHor)
}

fun Vector.isValidFor(grid: Grid<Char>) = isWithinGrid(grid) && grid[this] != 'X'

fun <N> Graph<N>.allShortestPaths(
    start: N,
    end: (node: N) -> Boolean,
    calculateDistance: (node: N, neighbor: N) -> Double,
): List<Pair<List<N>, Double>> {
    val paths = mutableListOf<Pair<List<N>, Double>>()
    val gScore = HashMap<N, Double>().withDefault { Double.POSITIVE_INFINITY }.apply { put(start, 0.0) }
    val open = PriorityQueue<N>(Comparator.comparing { gScore.getValue(it) }).apply { add(start) }
    val closed = HashSet<N>()
    val cameFrom = HashMap<N, List<N>>()

    //fun N.path(): List<List<N>> = cameFrom[this]?.let { it.path() + this } ?: listOf(this)
    fun N.paths(): List<List<N>> = cameFrom[this]?.let { cfs ->
        cfs.flatMap { cf -> cf.paths().map { it + this } }
    } ?: listOf(listOf(this))

    while (open.isNotEmpty()) {
        val current = open.remove()
        closed.add(current)
        if (end(current)) {
            println("found end")
            println(gScore[current])
            if (paths.isEmpty() || paths.first().second == gScore.getValue(current)) {
                for (p in current.paths()) {
                    paths.add(p to gScore.getValue(current))
                }
            } else {
                if (paths.first().second < gScore.getValue(current)) {
                    return paths
                }
                continue
            }
        }

        for (neighbor in getNeighbors(current)) {
            if (closed.contains(neighbor)) continue
            val tentativeGScore = gScore.getValue(current) + calculateDistance(current, neighbor)

            if (tentativeGScore < gScore.getValue(neighbor)) {
                cameFrom[neighbor] = listOf(current)
                gScore[neighbor] = tentativeGScore
                if (!open.contains(neighbor)) {
                    open.add(neighbor)
                }
            } else if (tentativeGScore == gScore.getValue(neighbor)) {
                cameFrom[neighbor] = cameFrom.getValue(neighbor) + listOf(current)
            }
        }
    }
    return paths
}
