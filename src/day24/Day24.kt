package day24

import java.io.File

data class Gate(val a: String, val b: String, val op: String, val output: String)

fun main() {
    val input = File("src/day24/input.txt").readText()
    val (initialInput, gatesInput) = input.split("\n\n")
    val signals = mutableMapOf<String, Int>()
    initialInput.split("\n").forEach { initial ->
        val (key, value) = initial.split(": ")
        signals[key] = value.toInt()
    }
    val remainingGates = gatesInput.split("\n").map { gate ->
        val parts = gate.split(" ")
        Gate(parts[0], parts[2], parts[1], parts[4])
    }.toMutableList()

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
    val toFlip = zString.zip(sumString).mapIndexedNotNull { i, (a, b) -> if (a != b) "z${zString.length - i - 1}" else null }
    println(toFlip)
    println(zString.length)


}
