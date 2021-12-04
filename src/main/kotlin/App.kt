import java.io.File

abstract class Solvable {
    final fun run() {
        println(solveA(getInput()))
        println(solveB(getInput()))
    }

    final fun debug() {
        println(solveA(getExampleInput()))
        println(solveB(getExampleInput()))
    }

    abstract fun day(): String

    abstract fun solveA(input: List<String>): String

    abstract fun solveB(input: List<String>): String

    private fun getInput() = File("inputs/input${day()}.txt").readLines()

    private fun getExampleInput() = File("examples/example${day()}.txt").readLines()
}

fun main(args: Array<String>) {
    val currDay = Day04()
    if (args.size > 0) {
        currDay.debug()
    } else {
        currDay.run()
    }
}
