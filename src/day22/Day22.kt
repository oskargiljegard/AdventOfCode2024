package day22

import java.io.File

fun main() {
    val lines = File("src/day22/input.txt").readLines()
    println(lines)
    val initialSecrets = lines.map { it.toLong() }
    //initialSecrets.map { s -> generateSequence(s, ::nextSecret).drop(2000).first() }.sum().also { println("Part 1 $it") }

    val prices = initialSecrets.map { s ->
        generateSequence(s, ::nextSecret).map { it % 10 }.take(2001).toList()
    }

    val changes = prices.map { ps ->
        ps.zipWithNext().map { (a, b) -> b - a }.toList()
    }

    val monkeyMaps = prices.zip(changes).map { (ps, cs) ->
        (4..(cs.size)).map { cs.subList(it - 4, it) to ps[it] }.reversed().toMap()
    }
    val r = (-9L..9L)
    val allSequences = r.flatMap { a -> r.flatMap { b -> r.flatMap { c -> r.map { d -> listOf(a, b, c, d) } } } }

    val totalMap: Map<List<Long>, Long> =
        allSequences.map { seq -> seq to monkeyMaps.map { map -> map[seq] ?: 0 }.sum() }.toMap()

    println(totalMap.values.max())

    /*
    val seqs = (0..(changes[0].size - 4)).map { changes[0].subList(it, it+4) }.also { println(it) }
    println(seqs.last())
    println(changes[0].takeLast(5))
    println(getPrice(prices[0], changes[0], seqs.last()))

    val totalBananas = seqs.mapIndexed { i, seq ->
        if (i % 100 == 0) println("Finished $i/${seqs.size}")
        prices.zip(changes).map { (ps, cs) -> getPrice(ps, cs, seq) ?: 0 }.sum()
    }
    println("Most Bananas ${totalBananas.max()}")
     */
}
// 2048 wrong
// 2045 wrong

fun getPrice(ps: List<Long>, cs: List<Long>, seq: List<Long>): Long? {
    val i = (0..(cs.size - seq.size)).firstOrNull { start ->
        cs.subList(start, start + seq.size) == seq
    } ?: return null

    return ps[i + seq.size]
}

val p = 16777216L

fun nextSecret(inputSecret: Long): Long {
    var s = inputSecret
    s = (s xor (s.shl(6))) % p
    s = (s xor (s.shr(5))) % p
    s = (s xor (s.shl(11))) % p
    return s
}
