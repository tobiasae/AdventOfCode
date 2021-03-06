import java.io.File

abstract class Solvable(val day: String) {
    
    final fun run() {
        println(solveADefault())
        println(solveBDefault())
    }

    final fun debug() {
        println(debugA())
        println(debugB())
    }

    final fun solveADefault() = solveA(getInput())

    final fun solveBDefault() = solveB(getInput())

    final fun debugA() = solveA(getExampleInput())

    final fun debugB() = solveB(getExampleInput())

    abstract fun solveA(input: List<String>): String

    abstract fun solveB(input: List<String>): String

    private fun getInput() = File("inputs/input${day}.txt").readLines()

    private fun getExampleInput() = File("examples/example${day}.txt").readLines()
}

fun main(args: Array<String>) {
    val days =
            listOf(
                    Day01(),
                    Day02(),
                    Day03(),
                    Day04(),
                    Day05(),
                    Day06(),
                    Day07(),
                    Day08(),
                    Day09(),
                    Day10(),
                    Day11(),
                    Day12(),
                    Day13(),
                    Day14(),
                    Day15(),
                    Day16(),
                    Day17(),
                    Day18(),
                    Day19(),
                    Day20(),
                    Day21(),
                    Day22(),
                    Day23(),
                    Day24(),
                    Day25()
            )

    if (args.size > 0) {
        days.forEach {
            println("=== DEBUG Day ${it.day} ===")
            it.debug()
            println()
        }
    } else {
        days.forEach {
            println("=== Day ${it.day} ===")
            it.run()
            println()
        }
    }
}
