package day13

import utils.Vector
import java.io.File
import kotlin.math.roundToInt
import kotlin.math.roundToLong

data class Machine(val a: Vector, val b: Vector, val prize: Vector)

fun main() {
    val input = File("src/day13/input.txt").readText()
    val extra = 10000000000000L
    val machines = input.split("\n\n").map { mInput ->
        val (ax, ay) = "Button A: X\\+(\\d+), Y\\+(\\d+)".toRegex().find(mInput)!!.destructured
        val (bx, by) = "Button B: X\\+(\\d+), Y\\+(\\d+)".toRegex().find(mInput)!!.destructured
        val (px, py) = "Prize: X=(\\d+), Y=(\\d+)".toRegex().find(mInput)!!.destructured
        return@map Machine(
            Vector(ax.toInt(), ay.toInt()),
            Vector(bx.toInt(), by.toInt()),
            Vector(px.toLong() + extra, py.toLong() + extra)
        )
    }
    println(machines)
    var total = 0L
    for (m in machines) {
        val (a, b) = getSolution(m) ?: continue
        total += a*3 + b
    }
    println(total)

}

fun getSolution(m: Machine): Pair<Long, Long>? {
    val (a, c) = m.a
    val (b, d) = m.b
    val k = 1.0 / (a*d - b*c)
    val ia = d*k
    val ib = -b*k
    val ic = -c*k
    val id = a*k
    val sol1 = (ia * m.prize.x + ib * m.prize.y).roundToLong()
    val sol2 = (ic * m.prize.x + id * m.prize.y).roundToLong()
    if (m.a * sol1 + m.b * sol2 == m.prize) {
        return sol1 to sol2
    }
    return null
}