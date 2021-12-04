class Day04 : Solvable("04") {

    override fun solveA(input: List<String>): String {
        val (drawnNumbers, boards) = readInput(input)

        for (i in drawnNumbers) {
            boards.forEach {
                val result = it.drawNumber(i)
                if (result >= 0) {
                    return (result * i).toString()
                }
            }
        }

        return "nobody wins"
    }

    override fun solveB(input: List<String>): String {
        val (drawnNumbers, boards) = readInput(input)
        var index = 0
        var remainingBoards = boards

        while (remainingBoards.size > 1) {
            remainingBoards = remainingBoards.filter { it.drawNumber(drawnNumbers[index]) < 0 }
            index++
        }

        val lastBoard = remainingBoards.first()

        for (i in index until drawnNumbers.size) {
            val result = lastBoard.drawNumber(drawnNumbers[i])
            if (result >= 0) {
                return (result * drawnNumbers[i]).toString()
            }
        }

        return "the last one does not win"
    }

    private fun readInput(input: List<String>): Pair<List<Int>, List<Board>> {
        val drawnNumbers = input.first().split(",").map { it.toInt() }
        val boards = mutableListOf<Board>()

        for (b in 2 until input.size step 6) {
            boards.add(Board(input.subList(b, b + 5)))
        }

        return Pair(drawnNumbers, boards)
    }
}

class Board {

    val board: Array<IntArray>
    val rowCount: IntArray
    val columnCount: IntArray

    constructor(lines: List<String>) {
        board =
                lines
                        .map { it.trim().split("\\s+".toRegex()).map { it.toInt() }.toIntArray() }
                        .toTypedArray()
        rowCount = board.map { 0 }.toIntArray()
        columnCount = board.first().map { 0 }.toIntArray()
    }

    fun drawNumber(n: Int): Int {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == n) {
                    board[i][j] = -1
                    rowCount[i]++
                    columnCount[j]++
                }
            }
        }
        return getSolution()
    }

    fun getSolution(): Int {
        if (rowCount.contains(board.size) || columnCount.contains(board.first().size)) {
            return board.map { it.filter { it > 0 }.sum() }.sum()
        }
        return -1
    }
}
