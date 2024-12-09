package day09

import java.io.File

data class Chunk(val size: Int, val id: Int, val isEmpty: Boolean)

fun main() {
    val input = File("src/day09/input.txt").readText()
    val nums = input.map { it.digitToInt() }
    val isFiles = generateSequence(true) { !it }.take(nums.size).toList()
    val numsWithIsFiles = nums.zip(isFiles)
    var chunks = mutableListOf<Chunk>()
    var id = 0
    for ((size, isFile) in numsWithIsFiles) {
        if (size == 0) continue
        if (isFile) {
            chunks.add(Chunk(size, id, false))
            id++
        } else {
            chunks.add(Chunk(size, -1, true))
        }
    }
    var movingId = chunks.maxBy { it.id }.id + 1
    while (movingId >= 0) {
        movingId--
        val movingChunk = chunks.first { it.id == movingId }
        var fromIndex = chunks.indexOf(movingChunk)
        val toIndex = chunks.indexOfFirst { it.isEmpty && it.size >= movingChunk.size }
        if (toIndex == -1) continue
        if (toIndex > fromIndex) continue
        val toChunk = chunks[toIndex]
        // Place the chunk in the empty space
        chunks.removeAt(toIndex)
        chunks.add(toIndex, movingChunk)
        if (toChunk.size != movingChunk.size) {
            chunks.add(toIndex + 1, Chunk(toChunk.size - movingChunk.size, -1, true))
            fromIndex++
        }
        // Remove the chunk from the old space
        chunks.removeAt(fromIndex)
        chunks.add(fromIndex, Chunk(movingChunk.size, -1, true))
        if (fromIndex < chunks.size - 1 && chunks[fromIndex + 1].isEmpty) {
            val m1 = chunks[fromIndex + 1]
            val m2 = chunks[fromIndex]
            chunks.removeAt(fromIndex + 1)
            chunks.removeAt(fromIndex)
            chunks.add(fromIndex, Chunk(m1.size + m2.size, -1, true))
        }
        if (fromIndex > 0 && chunks[fromIndex - 1].isEmpty) {
            val m1 = chunks[fromIndex]
            val m2 = chunks[fromIndex - 1]
            chunks.removeAt(fromIndex)
            chunks.removeAt(fromIndex - 1)
            chunks.add(fromIndex - 1, Chunk(m1.size + m2.size, -1, true))
        }

        //println()
        //chunks.forEach { c -> repeat(c.size) { print(if (c.isEmpty) '.' else c.id) } }
    }
    val blocks = chunks.flatMap { c -> List(c.size) { if (c.isEmpty) -1 else c.id } }
    println()
    blocks.mapIndexed { index, id -> index.toLong() * id.toLong() }.filter { it > 0 }.sum().also { println(it) }


    /*
    val blocks = mutableListOf<Int>()
    var id = 0
    for ((size, isFile) in numsWithIsFiles) {
        if (isFile) {
            repeat(size) {
                blocks.add(id)
            }
            id++
        } else {
            repeat(size) {
                blocks.add(-1)
            }
        }
    }
    println(blocks.map { if (it==-1) '.' else it }.joinToString(""))
    var pEmpty = 0;
    var pFilled = blocks.lastIndex
    while (blocks[pEmpty] != -1) pEmpty++
    while (blocks[pFilled] == -1) pFilled--
    while (pEmpty <= pFilled) {
        blocks[pEmpty] = blocks[pFilled]
        blocks[pFilled] = -1
        while (blocks[pEmpty] != -1) pEmpty++
        while (blocks[pFilled] == -1) pFilled--
    }
    println(blocks.map { if (it==-1) '.' else it }.joinToString(""))
    blocks.filter { it != -1 }.mapIndexed { index, id -> index.toLong() * id.toLong() }.sum().also { println(it) }
     */
}
