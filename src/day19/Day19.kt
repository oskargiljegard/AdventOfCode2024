package day19

import java.io.File

lateinit var towels: List<String>
lateinit var towelMap: Map<String, List<String>>

fun main() {
    val lines = File("src/day19/input.txt").readLines()
    towels = lines[0].split(", ")
    val designs = lines.drop(2)
    println(towels)
    println(designs)

    towelMap = everyDesign(8).toList().mapNotNull { design ->
        val matchingTowels = towels.filter { t -> design.startsWith(t) }
        if (matchingTowels.isEmpty()) {
            null
        } else {
            design to matchingTowels
        }
    }.toMap()

    println(towelMap.size)

    //println(buildDesign(designs[7], 0))



    var possible = 0
    for ((attempts, d) in designs.withIndex()) {
        if (buildDesign(d)) {
            possible++
        }
        println(attempts)
    }
    println("Possible: $possible")
}

fun everyDesign(length: Int): Sequence<String> = sequence {
    if (length == 0) {
        yield("")
    } else {
        yieldAll(everyDesign(length - 1))
        for (c in "wubrg") {
            yieldAll(everyDesign(length - 1).map { c + it })
        }
    }
}

fun buildDesign(design: String): Boolean {
    if (design.isEmpty()) return true
    val possibleTowels = towelMap[design.take(8)] ?: return false
    for (t in possibleTowels) {
        if (design.startsWith(t)) {
            if (buildDesign(design.removePrefix(t))) {
                return true
            }
        }
    }
    return false
}

/*
val cache = mutableMapOf<Pair<String, Int>, List<String>?>()
fun buildDesign(design: String, from: Int): List<String>? {
    if (from == design.length) return emptyList()
    if (cache.contains(design to from)) return cache[design to from]
    val possibleTowels = towelMap[design.drop(from).take(8)] ?: return null
    for (t in possibleTowels) {
        if (from + t.length < design.length && design.substring(from, from + t.length) == t) {
            val rest = buildDesign(design, from + t.length) ?: continue
            val result = listOf(t) + rest
            cache[design to from] = result
            return listOf(t) + rest
        }
    }
    return null
}
 */

/*
val cache = mutableMapOf<Pair<String, Int>, List<String>?>()
fun buildDesign(design: String, from: Int): List<String>? {
    if (from == design.length) return emptyList()
    if (cache.contains(design to from)) return cache[design to from]
    for (t in towels) {
        if (from + t.length < design.length && design.substring(from, from + t.length) == t) {
            val rest = buildDesign(design, from + t.length) ?: continue
            val result = listOf(t) + rest
            cache[design to from] = result
            return listOf(t) + rest
        }
    }
    return null
}
 */

/*
val cache = mutableMapOf<String, List<String>?>()
fun buildDesign(design: String): List<String>? {
    println(design)
    if (design.isEmpty()) return emptyList()
    if (cache.contains(design)) return cache[design]
    for (t in towels) {
        if (design.startsWith(t)) {
            val rest = buildDesign(design.removePrefix(t)) ?: continue
            val result = listOf(t) + rest
            cache[design] = result
            return listOf(t) + rest
        }
    }
    return null
}
*/