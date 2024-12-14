package day14

import utils.Grid
import utils.Vector
import java.io.File

data class Robot(var pos: Vector, val vel: Vector)

fun main() {
    val lines = File("src/day14/input.txt").readLines()
    //val mapSize = Vector(11, 7)
    val mapSize = Vector(101, 103)
    val robots = lines.map { l ->
        val (px, py, vx, vy) = "p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)".toRegex().find(l)!!.destructured
        return@map Robot(Vector(px.toLong(), py.toLong()), Vector(vx.toLong(), vy.toLong()))
    }.also { println(it) }
    // Assume that a Christmas tree means that 80% of the bots form a region?
    /*
    repeat(100) { iteration ->
        val grid = Grid(mapSize) { 0 }
        for (r in robots) {
            val p = r.pos + r.vel
            r.pos = Vector((p.intX + mapSize.intX) % mapSize.intX, (p.intY + mapSize.intY) % mapSize.intY)
            grid[r.pos]++
        }
        val seconds = iteration + 1
    }
     */


    val sb = StringBuilder()
    // 1 75 102 178 203 281 304 384 405 487
    // loops by 101
    // loops by 103
    // 75 wrong
    repeat(20000) { iteration ->
        val grid = Grid(mapSize) { 0 }
        for (r in robots) {
            val p = r.pos + r.vel
            r.pos = Vector((p.intX + mapSize.intX) % mapSize.intX, (p.intY + mapSize.intY) % mapSize.intY)
            grid[r.pos]++
        }
        sb.append("### Iteration ${iteration+1}\n")
        sb.append(grid.toStringIndexed { pos, n -> if (n == 0) " " else n.toString() })
        sb.append("\n\n")
        if (iteration % 1000 == 0) println(iteration)
    }
    val counts = mutableListOf<Int>()
    counts.add(robots.count { it.pos.intX < mapSize.intX / 2 && it.pos.intY < mapSize.intY / 2 })
    counts.add(robots.count { it.pos.intX > mapSize.intX / 2 && it.pos.intY < mapSize.intY / 2 })
    counts.add(robots.count { it.pos.intX < mapSize.intX / 2 && it.pos.intY > mapSize.intY / 2 })
    counts.add(robots.count { it.pos.intX > mapSize.intX / 2 && it.pos.intY > mapSize.intY / 2 })
    println(robots)
    println(counts)
    println(counts.reduce { acc, i -> acc * i })
    File("test.txt").writeText(sb.toString())

    // 17071 has the tree, answer was too large
    // Searching for exact pattern also found the tree at 6668

}


