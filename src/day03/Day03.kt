package day03

import java.io.File
import kotlin.math.abs

fun main() {
    val text = File("src/day03/input.txt").readText()
    val r = "mul\\((\\d+),(\\d+)\\)".toRegex()
    val res = r.findAll(text);
    println(res.map {
        val (a,b) = it.destructured
        return@map a.toInt() * b.toInt();
    }.sum())
    val r2 = "(mul\\((\\d+),(\\d+)\\))|(do\\(\\))|(don't\\(\\))".toRegex()
    val res2 = r2.findAll(text);
    var enabled = true;
    var sum = 0;
    for (match in res2) {
        when (match.groupValues[0]) {
            "do()" -> {
                enabled = true;
            }
            "don't()" -> {
                enabled = false;
            }
            else -> {
                println(match.groupValues[0])
                val (a,b) = r.findAll(match.groupValues[0]).first().destructured
                if (enabled) {
                    sum += a.toInt() * b.toInt()
                }
            }
        }
    }
    println(sum);
}

