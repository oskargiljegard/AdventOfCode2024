package day23

import utils.HashmapGraph
import java.io.File

fun main() {
    val lines = File("src/day23/input.txt").readLines()
    println(lines)
    val connections = lines.map {
        val (a, b) = it.split("-")
        a to b
    }
    val computers = connections.flatMap { listOf(it.first, it.second) }.toSet()
    println(computers)
    val graph = HashmapGraph(
        computers.associateWith { comp ->
            connections.mapNotNull { conn ->
                when (comp) {
                    conn.first -> conn.second
                    conn.second -> conn.first
                    else -> null
                }
            }
        }
    )
    println(graph.getNeighbors(graph.nodes.toList()[0]))

    val antiGraph = HashmapGraph(
        computers.associateWith { c1 ->
            computers.filter { c2 ->
                !graph.getNeighbors(c1).contains(c2)
            }
        }
    )
    println(antiGraph.getNeighbors(graph.nodes.toList()[0]))

    val party = getBiggestParty(antiGraph, emptySet(), emptySet())
    println(party)
    println(party.size)
    println(party.sorted().joinToString(","))



    // Part 1
    /*
    val parties = graph.nodes.flatMap { c1 ->
        graph.getNeighbors(c1).flatMap { c2 ->
            graph.getNeighbors(c2).mapNotNull { c3 ->
                if (graph.getNeighbors(c3).contains(c1)) {
                    listOf(c1, c2, c3).sorted()
                } else {
                    null
                }
            }
        }
    }.toSet().toList()
    println("Parties: ${parties.size}")

    val partiesWithT = parties.filter { p -> p.any { c -> c.startsWith('t') } }
    println("Parties with T: ${partiesWithT.size}")
     */
}

fun getBiggestParty(antiGraph: HashmapGraph<String>, current: Set<String>, banned: Set<String>): Set<String> {
    val possible = antiGraph.nodes - current.flatMap { antiGraph.getNeighbors(it) }.toSet() - banned
    if (possible.isEmpty()) return current
    for (n in possible) {
        val biggestWithN = getBiggestParty(antiGraph, current + n, banned)
        val biggestWithoutN = getBiggestParty(antiGraph, current, banned + n)
        return if (biggestWithN.size > biggestWithoutN.size) {
            biggestWithN
        } else {
            biggestWithoutN
        }
    }
    throw Error("Unreachable")
}