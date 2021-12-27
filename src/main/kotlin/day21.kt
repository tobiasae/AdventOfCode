class Day21 : Solvable("21") {

    override fun solveA(input: List<String>): String {
        val (player1, player2) = getPlayers(input)
        val dice = DeterministicDie()
        var count = 0
        while (true) {
            player1.play(dice.roll() + dice.roll() + dice.roll())
            if (player1.score >= 1000) {
                return ((count * 6 + 3) * player2.score).toString()
            }
            player2.play(dice.roll() + dice.roll() + dice.roll())
            if (player2.score >= 1000) {
                return ((count * 6 + 6) * player1.score).toString()
            }
            count++
        }
    }

    override fun solveB(input: List<String>): String {
        val (player1, player2) = getPlayers(input)
        val p = recursive(Universe(player1, player2, true), null)
        return Math.max(p.first, p.second).toString()
    }

    private fun getPlayers(input: List<String>): Pair<Player, Player> {
        return Player(input.first().split(":")[1].trim().toInt() - 1) to
                Player(input.last().split(":")[1].trim().toInt() - 1)
    }

    val dice =
            listOf(3, 4, 5, 4, 5, 6, 5, 6, 7, 4, 5, 6, 5, 6, 7, 6, 7, 8, 5, 6, 7, 6, 7, 8, 7, 8, 9)

    private val memo = HashMap<Universe, Pair<Long, Long>>()

    private fun recursive(universe: Universe, next: Int?): Pair<Long, Long> {

        val u = if (next != null) universe.next(next) else universe

        memo[u]?.let {
            return it
        }

        if (u.p1.score >= 21) {
            return 1L to 0L
        } else if (u.p2.score >= 21) {
            return 0L to 1L
        }

        return dice
                .map { recursive(u, it) }
                .reduce { (p0, p1), (p2, p3) -> p0 + p2 to p1 + p3 }
                .also { memo[u] = it }
    }
}

data class Universe(val p1: Player, val p2: Player, val p1sTurn: Boolean) {

    fun next(diceRoll: Int): Universe {
        return if (p1sTurn) Universe(p1.copy().apply { play(diceRoll) }, p2, false)
        else Universe(p1, p2.copy().apply { play(diceRoll) }, true)
    }
}

data class Player(var position: Int = 1, var score: Int = 0) {

    fun play(diceRoll: Int) {
        position += diceRoll
        position %= 10
        score += position + 1
    }
}

class DeterministicDie() {
    var last = 0

    fun roll(): Int {
        return (++last).also { if (it > 1000) last = 1 }
    }
}
