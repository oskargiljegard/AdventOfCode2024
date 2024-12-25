package day24

import java.io.File

data class Gate(val a: String, val b: String, val op: String, val output: String)

lateinit var reverseMap: Map<String, Gate>

fun main() {
    val input = File("src/day24/input.txt").readText()
    val (initialInput, gatesInput) = input.split("\n\n")
    val signals = mutableMapOf<String, Int>()
    initialInput.split("\n").forEach { initial ->
        val (key, value) = initial.split(": ")
        signals[key] = value.toInt()
    }
    val gates = gatesInput.split("\n").map { gate ->
        val parts = gate.split(" ")
        Gate(parts[0], parts[2], parts[1], parts[4])
    }
        .flip("shh", "z21")
        .flip("dqr", "z33")
        .flip("vgs", "dtk")
        .flip("pfw", "z39")
    val remainingGates = gates.toMutableList()

    var iterations = 0
    while (remainingGates.isNotEmpty()) {
        val processedGates = mutableListOf<Gate>()
        for (g in remainingGates) {
            val aVal = signals[g.a]
            val bVal = signals[g.b]
            if (aVal != null && bVal != null) {
                val outputVal = when (g.op) {
                    "AND" -> aVal and bVal
                    "OR" -> aVal or bVal
                    "XOR" -> aVal xor bVal
                    else -> throw Error("Illegal op ${g.op}")
                }
                signals[g.output] = outputVal
                processedGates.add(g)
            }
        }
        if (processedGates.isEmpty()) throw Error("No processed gates")
        remainingGates.removeAll(processedGates)
        iterations++
    }
    println(iterations)
    val xString = signals.keys.filter { it.startsWith("x") }.sortedDescending().map { signals[it] }.joinToString("")
    val yString = signals.keys.filter { it.startsWith("y") }.sortedDescending().map { signals[it] }.joinToString("")
    val zString = signals.keys.filter { it.startsWith("z") }.sortedDescending().map { signals[it] }.joinToString("")
    val sumString = (xString.toLong(2) + yString.toLong(2)).toString(2)
    println(zString)
    println(sumString)
    val toFlip =
        zString.zip(sumString).mapIndexedNotNull { i, (a, b) -> if (a != b) "z${zString.length - i - 1}" else null }
    println(toFlip)
    println(zString.length)

    println(gates.size)
    gates.filter { g -> g.output.startsWith("z") }.sortedBy { it.output }.forEach { println(it) }
    reverseMap = gates.map { it.output to it }.toMap()
    println()
    println(expr("z03", 3))
    println(expr("z04", 3))
    println(expr("z05", 3))
    println(expr("z39", 3))
    println(expr("z40", 3))
    println(expr("z40", 2))


    listOf("shh", "z21", "dqr", "z33", "vgs", "dtk", "pfw", "z39").sorted().joinToString(",").also { println(it) }

    //println(gates.filter { it.op == "XOR" && ("x26" in listOf(it.a, it.b)) })

}

private fun List<Gate>.flip(o1: String, o2: String): List<Gate> = this.map { g ->
    when (g.output) {
        o1 -> g.copy(output = o2)
        o2 -> g.copy(output = o1)
        else -> g
    }
}

fun expr(output: String, depth: Int): String {
    if (depth == 0) return output
    val g = reverseMap[output] ?: return output
    val a = expr(g.a, depth - 1)
    val b = expr(g.b, depth - 1)
    return "(${a} ${g.op} ${b})"
}

// z21, z33, z39, z45
// flip shh and z21
// flip dqr and z33
// flip vgs and dtk