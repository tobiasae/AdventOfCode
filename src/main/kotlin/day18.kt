import java.util.ArrayDeque

class Day18 : Solvable("18") {

    override fun solveA(input: List<String>): String {
        return input
                .map { SnailFishNumber.fromString(it) }
                .reduce { l, r -> l + r }
                .magnitude()
                .toString()
    }

    override fun solveB(input: List<String>): String {
        return input
                .map {
                    input
                            .filter { o -> it != o }
                            .map { o ->
                                (SnailFishNumber.fromString(it) + SnailFishNumber.fromString(o))
                                        .magnitude()
                            }
                            .max()
                            ?: 0
                }
                .max()
                .toString()
    }
}

abstract class SnailFishNumber(var parent: PairNumber?, var depth: Int = 0) {

    abstract fun explode(): Boolean

    abstract fun split(): Boolean

    abstract fun magnitude(): Int

    fun reduce() {
        while (explode() || split()) {}
    }

    operator fun plus(other: SnailFishNumber): SnailFishNumber =
            PairNumber(this, other).also {
                it.initializeDepth()
                it.reduce()
            }

    abstract fun addToLeftRegularNumber(value: Int, child: PairNumber?)

    abstract fun addToRightRegularNumber(value: Int, child: PairNumber?)

    abstract fun initializeDepth(depth: Int = 0)

    companion object {
        fun fromString(s: String): SnailFishNumber {
            val numbers = ArrayDeque<SnailFishNumber>()

            s.forEach {
                if (it.isDigit()) {
                    numbers.addLast(RegularNumber(it.toInt() - 48))
                } else if (it == ']') {
                    val right = numbers.removeLast()
                    val left = numbers.removeLast()
                    numbers.addLast(PairNumber(left, right))
                }
            }
            return numbers.removeLast()
        }
    }
}

class PairNumber(var left: SnailFishNumber, var right: SnailFishNumber) : SnailFishNumber(null) {

    init {
        left.parent = this
        right.parent = this
    }

    override fun explode(): Boolean {
        if (depth == 4) {
            parent?.childExploded(this)
            return true
        } else {
            return left.explode() || right.explode()
        }
    }

    override fun split() = left.split() || right.split()

    override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()

    fun childExploded(child: PairNumber) {
        val zero =
                RegularNumber(0).also {
                    it.depth = depth + 1
                    it.parent = this
                }
        if (child == left) {
            left = zero
            parent?.addToLeftRegularNumber((child.left as RegularNumber).number, this)
            right.addToLeftRegularNumber((child.right as RegularNumber).number, null)
        } else {
            right = zero
            left.addToRightRegularNumber((child.left as RegularNumber).number, null)
            parent?.addToRightRegularNumber((child.right as RegularNumber).number, this)
        }
    }

    fun childSplit(child: RegularNumber) {
        val l = child.number / 2
        val r = child.number - l
        val pair =
                PairNumber(RegularNumber(l), RegularNumber(r)).also {
                    it.depth = depth + 1
                    it.parent = this
                }
        if (child == left) left = pair else if (child == right) right = pair
    }

    override fun addToLeftRegularNumber(value: Int, child: PairNumber?) {
        if (child == null) left.addToLeftRegularNumber(value, null)
        else if (child == left) parent?.addToLeftRegularNumber(value, this)
        else if (child == right) left.addToRightRegularNumber(value, null)
    }

    override fun addToRightRegularNumber(value: Int, child: PairNumber?) {
        if (child == null) right.addToRightRegularNumber(value, null)
        else if (child == left) right.addToLeftRegularNumber(value, null)
        else if (child == right) parent?.addToRightRegularNumber(value, this)
    }

    override fun initializeDepth(depth: Int) {
        this.depth = depth
        left.initializeDepth(depth + 1)
        right.initializeDepth(depth + 1)
    }
}

class RegularNumber(var number: Int) : SnailFishNumber(null) {

    override fun explode() = false

    override fun split(): Boolean {
        if (number >= 10) {
            parent?.childSplit(this)
            return true
        }
        return false
    }

    override fun magnitude() = number

    override fun addToLeftRegularNumber(value: Int, child: PairNumber?) {
        number += value
    }

    override fun addToRightRegularNumber(value: Int, child: PairNumber?) {
        number += value
    }

    override fun initializeDepth(depth: Int) {
        this.depth = depth
    }
}
