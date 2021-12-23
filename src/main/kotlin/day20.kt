class Day20 : Solvable("20") {

    override fun solveA(input: List<String>): String {
        val (enhancement, image) = getImageEnhancement(input)

        return solve(image, enhancement, 2).toString()
    }

    override fun solveB(input: List<String>): String {
        val (enhancement, image) = getImageEnhancement(input)

        return solve(image, enhancement, 50).toString()
    }

    private fun getImageEnhancement(input: List<String>): Pair<String, List<List<Char>>> {
        val enhancement = input.first()
        val image =
                mutableListOf<List<Char>>().also {
                    input.drop(2).forEach { line -> it.add(line.toCharArray().toList()) }
                }
        return Pair(enhancement, image)
    }

    private fun solve(image: List<List<Char>>, enhancement: String, num: Int): Int {
        var res = image
        for (i in 0 until num) {
            res =
                    enhance(
                            res,
                            enhancement,
                            if (enhancement.first() == '#' && i % 2 == 0) enhancement.last()
                            else enhancement.first()
                    )
        }
        return res.map { it.count { it == '#' } }.sum()
    }

    private fun enhance(
            image: List<List<Char>>,
            enhancement: String,
            default: Char
    ): List<List<Char>> {
        return (-1..image.size).map { j ->
            (-1..image.first().size).map { i ->
                val number =
                        getSurroundingPoints(Pair(i, j))
                                .map { p ->
                                    val c =
                                            if (p.second in image.indices &&
                                                            p.first in image[p.second].indices
                                            )
                                                    image[p.second][p.first]
                                            else default
                                    if (c == '#') 1 else 0
                                }
                                .toInt()
                enhancement[number]
            }
        }
    }

    private fun getSurroundingPoints(p: Pair<Int, Int>): List<Pair<Int, Int>> {
        return listOf(
                Pair(p.first - 1, p.second - 1),
                Pair(p.first, p.second - 1),
                Pair(p.first + 1, p.second - 1),
                Pair(p.first - 1, p.second),
                Pair(p.first, p.second),
                Pair(p.first + 1, p.second),
                Pair(p.first - 1, p.second + 1),
                Pair(p.first, p.second + 1),
                Pair(p.first + 1, p.second + 1)
        )
    }
}
