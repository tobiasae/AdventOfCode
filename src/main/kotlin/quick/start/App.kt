package quick.start

import java.io.File

abstract class Solvable {
    fun run() {
        println(solveA(getInput()))
        println(solveB(getInput()))
    }

    abstract fun day(): String

    abstract fun solveA(input: List<String>): String

    abstract fun solveB(input: List<String>): String

    private fun getInput() = File("inputs/input${day()}.txt").readLines()
}

fun main() {
    Day01().run()
}
