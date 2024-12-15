package utils

import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

interface Graph<N> {
    val nodes: Collection<N>
    fun getNeighbors(node: N): Collection<N>
}

class HashmapGraph<N>(private val graph: Map<N, List<N>>) : Graph<N> {
    override val nodes get() = graph.keys
    override fun getNeighbors(node: N) = graph[node] ?: emptyList()
}

fun <N> List<Pair<N, N>>.asEdgesToUndirectedGraph(): Graph<N> =
    HashmapGraph((this + this.map { pair -> pair.second to pair.first }).groupBy({ it.first }, { it.second }))

infix fun <T> Iterable<T>.product(other: Iterable<T>): Iterable<Pair<T, T>> =
    this.flatMap { first -> other.map { second -> first to second } }

infix fun IntRange.product(other: IntRange): Iterable<Pair<Int, Int>> = this.toList() product other.toList()

class Grid<T>(val size: Vector, private val grid: Array<Array<T>>) {
    companion object {
        inline operator fun <reified T> invoke(): Grid<T> {
            return Grid(Vector.zero, emptyArray())
        }

        inline operator fun <reified T> invoke(size: Vector, init: (pos: Vector) -> T): Grid<T> {
            val grid: Array<Array<T>> =
                Array(size.intY) { y -> Array(size.intX) { x -> init(Vector(x, y)) } }
            return Grid(size, grid)
        }
    }

    val minPos: Vector
        get() = Vector(0, 0)
    val maxPos: Vector
        get() = Vector(size.intX - 1, size.intY - 1)

    operator fun get(pos: Vector): T = grid[pos.intY][pos.intX]
    operator fun set(pos: Vector, value: T) {
        grid[pos.intY][pos.intX] = value
    }

    val xPositions get() = (minPos.intX..maxPos.intX)
    val yPositions get() = (minPos.intY..maxPos.intY)
    val positions get() = (xPositions product yPositions).map { it.toVector() }

    fun forEach(action: (T) -> Unit) = positions.forEach { pos -> action(get(pos)) }
    fun forEachIndexed(action: (pos: Vector, T) -> Unit) = positions.forEach { pos -> action(pos, get(pos)) }

    fun mapInPlace(mutator: (T) -> T) = positions.forEach { pos -> set(pos, mutator(get(pos))) }
    fun mapInPlaceIndexed(mutator: (pos: Vector, T) -> T) =
        positions.forEach { pos -> set(pos, mutator(pos, get(pos))) }

    operator fun iterator(): Iterator<T> = positions.map { get(it) }.iterator()

    override fun toString(): String = toString { it.toString() }
    fun toString(elementTransformer: (T) -> String): String =
        toStringIndexed { pos, element -> "$pos: ${elementTransformer(element)}" }

    fun toStringIndexed(elementTransformer: (pos: Vector, T) -> String): String {
        val elementSize = positions.maxOfOrNull { elementTransformer(it, get(it)).length } ?: 0
        return yPositions.fold("") { acc, y ->
            acc + xPositions.fold("") { rowAcc, x ->
                rowAcc + elementTransformer(Vector(x, y), get(Vector(x, y))).padEnd(elementSize + 1)
            } + "\n"
        }
    }
}

inline fun <reified T> gridOf(size: Vector, value: T): Grid<T> = Grid(size) { value }

inline fun <reified T> String.asCharGrid(transform: (Char) -> T) = this.lines().map { it.toList() }.toGrid(transform)

inline fun <reified T> String.asGridWithSeparators(
    rowSeparator: String = "\n",
    tileSeparator: String = " ",
    transform: (String) -> T
) = this.split(rowSeparator).map { it.split(tileSeparator) }.toGrid(transform)

inline fun <I, reified T> List<List<I>>.toGrid(transform: (I) -> T): Grid<T> {
    if (isEmpty()) return Grid()
    val size = Vector(this[0].size, this.size)
    val grid = Array(size.intY) { y ->
        require(this[y].size == size.intX) { "Inconsistent grid size: ${this[y].size} != $size" }
        Array(size.intX) { x -> transform(this[y][x]) }
    }
    return Grid(size, grid)
}

/**
 * Finds the shortest path using A*
 *
 * A* prioritises visiting nodes with the lowest fScore = gScore + hScore
 * gScore is the distance from the starting node to the current node using the best known path
 * hScore is an estimate of the distance from the current node to the end node
 *
 * @param start: the starting node
 * @param end: the ending node
 * @param calculateDistance: a function that calculates the distance between two nodes (sometimes known as the weight)
 * @param estimateDistanceToEnd: a function that estimates the distance to the end node (sometimes known as the heuristic or h score).
 *                               Must never be greater than the actual distance to the end node.
 */
fun <N> Graph<N>.aStarShortestPath(
    start: N,
    end: N,
    calculateDistance: (node: N, neighbor: N) -> Double,
    estimateDistanceToEnd: (node: N) -> Double
): List<N>? {
    val gScore = HashMap<N, Double>().withDefault { Double.POSITIVE_INFINITY }.apply { put(start, 0.0) }
    fun fScore(n: N) = gScore.getValue(n) + estimateDistanceToEnd(n)
    val open = PriorityQueue<N>(Comparator.comparing { fScore(it) }).apply { add(start) }
    val cameFrom = HashMap<N, N>()
    fun N.path(): List<N> = cameFrom[this]?.let { it.path() + this } ?: emptyList()

    while (open.isNotEmpty()) {
        val current = open.remove()
        if (current == end) return current.path()

        for (neighbor in getNeighbors(current)) {
            val tentativeGScore = gScore.getValue(current) + calculateDistance(current, neighbor)

            if (tentativeGScore < gScore.getValue(neighbor)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                if (!open.contains(neighbor)) {
                    open.add(neighbor)
                }
            }
        }
    }
    return null
}