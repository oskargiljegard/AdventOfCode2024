package day17

import java.io.File

data class State(var a: Long, var b: Long, var c: Long, var ptr: Int, val inst: List<Int>, val outputs: MutableList<Int>)

fun main() {
    val lines = File("src/day17/input.txt").readLines()
    val origState = State(
        lines[0].split(" ").last().toLong(),
        lines[1].split(" ").last().toLong(),
        lines[2].split(" ").last().toLong(),
        0,
        lines.last().split(" ").last().split(",").map { it.toInt() },
        mutableListOf(),
    )
    println(lines)
    println(origState)
    /*
    while (true) {
        val halted = state.next()
        println(state)
        if (halted) break
    }
    println(state.outputs.joinToString(","))
     */
    var initialA = 0L
    while (true) {
        val state = origState.copy(outputs = mutableListOf(), a = initialA)
        val isCopy = state.isCopy()
        if (isCopy) {
            println("found copy!")
            println(initialA)
            break
        }
        if (initialA % 1000000 == 0L) {
            println(initialA)
        }
        initialA++
    }


}

fun State.comboValue(operandCode: Int): Long {
    return when (operandCode) {
        in 0..3 -> operandCode.toLong()
        4 -> a
        5 -> b
        6 -> c
        7 -> throw IllegalStateException("Reserved operandCode 7")
        else -> throw IllegalStateException("Invalid operandCode $operandCode")
    }
}

fun State.isCopy(): Boolean {
    while (true) {
        val halted = next()
        if (outputs.size == inst.size) {
            return outputs == inst
        }
        if (outputs.isNotEmpty()) {
            if (outputs.last() != inst[outputs.lastIndex]) return false
        }
        /*
        if (outputs.isEmpty()) continue
        val outIndex = outputs.lastIndex
        if (outputs[outIndex] != inst[outIndex]) return false
        if (outputs.size == inst.size) {
            if (outputs == inst) return true
        }
         */
        if (halted) return false
    }
}

fun State.next(): Boolean {
    if (ptr > inst.lastIndex) {
        return true // Halted
    }
    val opcode = inst[ptr]
    if (ptr + 1 > inst.lastIndex) {
        throw IllegalStateException("Operand is outside inst list")
    }
    val operandCode = inst[ptr + 1]
    //println("Executing $opcode with $operandCode")
    when (opcode) {
        0 -> {
            a = a.shr(comboValue(operandCode).toInt())
        }

        1 -> {
            b = b xor operandCode.toLong()
        }

        2 -> {
            b = comboValue(operandCode) % 8
        }

        3 -> {
            if (a != 0L) {
                ptr = operandCode - 2
            }
        }

        4 -> {
            b = b xor c
        }

        5 -> {
            val x = comboValue(operandCode) % 8
            outputs.add(x.toInt())
        }

        6 -> {
            b = a.shr(comboValue(operandCode).toInt())
        }

        7 -> {
            c = a.shr(comboValue(operandCode).toInt())
        }
    }
    ptr += 2
    return false
}
/*
2,4 B=A%8
1,3 B=B xor 3 (what does it mean to XOR any integer???)
7,5 C=A shr B
0,3 A=A shr 3
1,5 B=B xor 5
4,4 B=B xor C
5,5 prints B % 8
3,0




5,5 prints ((A % 8) xor 3 xor 5 xor (A shr B)) % 8


5,5 prints ((A % 8) xor (A shr B) xor 6) % 8
should be 2

(A % 8) xor (A shr B)

6: 110
2: 010

(A % 8) xor (A shr B): 100

 */