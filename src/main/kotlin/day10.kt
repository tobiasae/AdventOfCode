class Day10 : Solvable("10") {

    override fun solveA(input: List<String>): String {
        return Math.abs(solve(input).filter { it < 0 }.sum()).toString()
    }

    override fun solveB(input: List<String>): String {
        solve(input).filter { it > 0 }.sorted().let {
            return it[it.size / 2].toString()
        }
    }

    /* The negative values are the solutions for part 1, the positive values for part 2 */
    private fun solve(input: List<String>): List<Long> {
        return input.map { s ->
            val stack = mutableListOf<Char>()
            s.forEach {
                if (isOpening(it)) stack.add(it)
                else if (stack.size == 0) return@map -getValuePart1(it)
                else if (isValidPair(stack.last(), it)) stack.removeAt(stack.size - 1)
                else return@map -getValuePart1(it)
            }
            stack.map(this::getValuePart2).reduceRight { l, r -> r * 5 + l }
        }
    }

    private fun isOpening(open: Char) = listOf('(', '[', '{', '<').contains(open)

    private fun isClosing(close: Char) = listOf(')', ']', '}', '>').contains(close)

    private fun isValidPair(open: Char, close: Char): Boolean {
        return when (open) {
            '(' -> close == ')'
            '[' -> close == ']'
            '{' -> close == '}'
            '<' -> close == '>'
            else -> false
        }
    }

    private fun getValuePart1(close: Char): Long {
        return when (close) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }

    private fun getValuePart2(close: Char): Long {
        return when (close) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            '<' -> 4
            else -> 0
        }
    }
}
