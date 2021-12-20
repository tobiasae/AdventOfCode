class Day14 : Solvable("14") {

    override fun solveA(input: List<String>): String {
        return solve(input, 10)
    }

    override fun solveB(input: List<String>): String {
        return solve(input, 40)
    }

    private fun solve(input: List<String>, iterations: Int): String {
        val template = input.first()
        val pairMap =
                HashMap<Pair<Char, Char>, Long>().also {
                    template.take(template.length - 1).forEachIndexed { i, v ->
                        val p = Pair(v, template[i + 1])
                        it[p] = (it[p] ?: 0) + 1
                    }
                }

        val rulesMap =
                HashMap<Pair<Char, Char>, Char>().also {
                    input.subList(2, input.size).map { r ->
                        val (match, insertion) = r.split(" -> ")
                        it[Pair(match[0], match[1])] = insertion[0]
                    }
                }

        repeat(iterations) {
            val newPairs = HashMap<Pair<Char, Char>, Long>()
            pairMap.forEach {
                rulesMap[it.key]?.let { insert ->
                    val p1 = Pair(it.key.first, insert)
                    val p2 = Pair(insert, it.key.second)
                    newPairs[p1] = (newPairs[p1] ?: 0) + it.value
                    newPairs[p2] = (newPairs[p2] ?: 0) + it.value
                    newPairs[it.key] = (newPairs[it.key] ?: 0) - it.value
                }
            }
            newPairs.forEach { pairMap[it.key] = (pairMap[it.key] ?: 0) + it.value }
            pairMap.filter { it.value == 0L }.forEach { pairMap.remove(it.key) }
        }

        val letterCount =
                HashMap<Char, Long>().also {
                    pairMap.forEach { p -> it[p.key.first] = (it[p.key.first] ?: 0) + p.value }
                    it[template.last()] = (it[template.last()] ?: 0) + 1
                }

        return (letterCount.maxBy { it.value }!!.value - letterCount.minBy { it.value }!!.value)
                .toString()
    }
}
