class Day08 : Solvable("08") {

    override fun solveA(input: List<String>): String {
        return input
                .map { it.split("|").last().trim().split(" ") }
                .map { it.filter { listOf(2, 3, 4, 7).contains(it.length) }.size }
                .sum()
                .toString()
    }

    override fun solveB(input: List<String>): String {
        val lines = input.map { it.split("|").map { it.trim().split(" ").map { it.toSet() } } }

        return lines
                .map {
                    val (learning, display) = it

                    val ones = learning.find { it.size == 2 } as Set
                    val fours = learning.find { it.size == 4 } as Set

                    display
                            .map {
                                when (it.size) {
                                    2 -> 1
                                    3 -> 7
                                    4 -> 4
                                    5 ->
                                            if (it.intersect(ones).size == 2) 3
                                            else if (it.intersect(fours).size == 2) 2 else 5
                                    6 ->
                                            if (it.intersect(ones).size == 1) 6
                                            else if (it.intersect(fours).size == 3) 0 else 9
                                    else -> 8
                                }
                            }
                            .joinToString(separator = "")
                            .toInt()
                }
                .sum()
                .toString()
    }
}
