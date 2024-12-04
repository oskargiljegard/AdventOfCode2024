package day04

import java.io.File

fun main() {
    val lines = File("src/day04/input.txt").readLines()
    //println(diags(lines).joinToString("\n"))
    //println(rotate(rotate(rotate(lines))).joinToString("\n"))
    //println(xmases(rotate(rotate(rotate(lines)))))
    //println()
    //println(lines.joinToString("\n"))
    var total = 0;
    println("CASE 1")
    var case = lines
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println("CASE 2")
    case = diags(lines)
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println("CASE 3")
    case = rotate(lines)
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println("CASE 4")
    case = diags(rotate(lines)) //???
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println("CASE 5")
    case = rotate(rotate(lines))
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println("CASE 6")
    case = diags(rotate(rotate(lines))) //???
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println("CASE 7")
    case = rotate(rotate(rotate(lines))) // ??? 0
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println("CASE 8")
    case = diags(rotate(rotate(rotate(lines)))) //???
    //println(case.joinToString("\n"))
    println(xmases(case))
    total += xmases(case)
    println(total)
    println("PART 2")
    //println(xdashmases(lines))
    //println(lines.joinToString("\n"))
    println(
        listOf(
            xdashmases(lines),
            xdashmases(rotate(lines)),
            xdashmases(rotate(rotate(lines))),
            xdashmases(rotate(rotate(rotate(lines)))),
        ).sum()
    )
}

fun xdashmases(mat: List<String>): Int {
    var total = 0
    for (y in 1..<(mat.size-1)) {
        for (x in 1..<(mat[0].length-1)) {
            if (mat[y][x] == 'A'
                && mat[y+1][x+1] == 'S'
                && mat[y+1][x-1] == 'S'
                && mat[y-1][x-1] == 'M'
                && mat[y-1][x+1] == 'M') {
                total++;
            }
        }
    }
    return total;
}

fun xmases(mat: List<String>): Int {
    val regex = "XMAS".toRegex()
    return mat.map {
        regex.findAll(it).count()
    }.sum()
}

fun rotate(mat: List<String>): List<String> {
    return List(mat[0].length) { y ->
        List(mat.size) { x ->
            mat[mat.size - x - 1][y]
        }.joinToString("")
    }
}

fun diags(mat: List<String>): List<String> {
    val output: MutableList<String> = mutableListOf()
    for (yStart in mat.indices) {
        var dx = 0;
        var dy = 0;
        var str = ""
        while (yStart + dy < mat.size && dx < mat[0].length) {
            str += mat[yStart + dy][dx];
            dx++;
            dy++;
        }
        output.add(str)
    }
    for (xStart in mat[0].indices) {
        if (xStart == 0) continue;
        var dx = 0;
        var dy = 0;
        var str = ""
        while (dy < mat.size && xStart + dx < mat[0].length) {
            str += mat[dy][xStart + dx];
            dx++;
            dy++;
        }
        output.add(str)
    }
    return output
}