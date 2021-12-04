class Day03 : Solvable("03") {

    override fun solveA(input: List<String>): String {
        val gammaList = getMostCommon(input)
        val epsilonList = gammaList.map { if (it == 0) 1 else 0 }

        return (binaryToInt(gammaList) * binaryToInt(epsilonList)).toString()
    }

    override fun solveB(input: List<String>): String {
        val intInput = input.map { it.map { it.toInt() - 48 } }
        var pos = 0

        var oxygen = intInput
        while (oxygen.size > 1) {
            oxygen = oxygen.filter { l -> l[pos] == getMostCommonInt(oxygen)[pos] }
            pos++
        }

        pos = 0

        var co2 = intInput
        while (co2.size > 1) {
            co2 = co2.filter { it[pos] != getMostCommonInt(co2)[pos] }
            pos++
        }

        return (binaryToInt(oxygen[0]) * binaryToInt(co2[0])).toString()
    }

    private fun getMostCommon(input: List<String>): List<Int> {
        return getMostCommonInt(input.map { it.map { it.toInt() - 48 } })
    }

    private fun getMostCommonInt(input: List<List<Int>>): List<Int> {
        return input.reduce { l, r -> l.zip(r) { a, b -> a + b } }.map {
            if (it >= input.size - it) 1 else 0
        }
    }

    private fun binaryToInt(list: List<Int>): Int {
        return list.reversed().reduceIndexed { i, l, r -> l + (r shl i) }
    }
}
