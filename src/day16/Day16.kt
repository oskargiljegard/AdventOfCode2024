package day16

import utils.*
import utils.Vector
import utils.aStarShortestPath
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.PI

data class State(val pos: Vector, val dir: Vector)

fun main() {
    val input = File("src/day16/input.txt").readText()
    val grid = input.asCharGrid { it }
    val dirs = listOf(
        Vector(1, 0),
        Vector(0, -1),
        Vector(-1, 0),
        Vector(0, 1),
    )
    val startPos = grid.positions.find { grid[it] == 'S' }!!
    val endPos = grid.positions.find { grid[it] == 'E' }!!
    val startDir = Vector(1, 0)
    grid[startPos] = '.'
    grid[endPos] = '.'
    val validStates = grid.positions.filter { grid[it] == '.' }.flatMap { pos -> dirs.map { dir -> State(pos, dir) } }
    val graph = HashmapGraph<State>(
        validStates.map { fromState ->
            val forward = if (grid[fromState.pos + fromState.dir] == '.') State(
                fromState.pos + fromState.dir,
                fromState.dir
            ) else null
            return@map fromState to listOf(
                State(fromState.pos, fromState.dir.rotated(PI / 2).rounded()),
                State(fromState.pos, fromState.dir.rotated(-PI / 2).rounded()),
            ) + if (forward != null) listOf(forward) else emptyList()
        }.toMap()
    )

    fun distance(from: State, to: State): Double = if (from.dir != to.dir) 1000.0 else 1.0
    val paths = graph.allShortestPaths(
        State(startPos, startDir),
        { state -> state.pos == endPos },
        ::distance,
    )
    paths.forEach { (p, s) -> p.forEach { grid[it.pos] = 'X' } }
    grid.toStringIndexed { pos, c -> c.toString() }.also { println(it) }
    println(paths.size)
    println(paths.map { it.second })
    val explored = paths.flatMap { p -> p.first.map { it.pos } }.toSet().size.also { println(it) }


    /*
    val path = (graph.aStarShortestPath(
        State(startPos, startDir),
        State(endPos, startDir),
        ::distance,
        { state -> 1000_000.0 },
    )!!).toMutableList()
    if (path[path.size - 2].pos == path[path.size - 1].pos) {
        path.removeLast()
    }
    path.add(0, State(startPos, startDir))
    val score = path.zipWithNext().map { (from, to) -> distance(from, to) }.sum()
    for (s in path) {
        grid[s.pos] = 'X'
    }
    grid.toStringIndexed { pos, c -> c.toString() }.also { println(it) }
    path.zipWithNext().filter { (from, to) -> from.pos == to.pos }.map { (from, to) -> from.pos }.also { println(it) }
    println(path)
    println(score)
    // WRONG 293124
     */
}

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
