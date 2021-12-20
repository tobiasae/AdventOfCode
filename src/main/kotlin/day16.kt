class Day16 : Solvable("16") {

    override fun solveA(input: List<String>): String {
        val bits = input.first().toBinary()

        val p = Packet.parsePacket(bits.toConsumer())

        fun getVersionSum(p: Packet): Int {
            var sum = p.version
            if (p is OperatorPacket) sum += p.packets.map { getVersionSum(it) }.sum()
            return sum
        }

        return getVersionSum(p).toString()
    }

    override fun solveB(input: List<String>): String {
        return Packet.parsePacket(input.first().toBinary().toConsumer()).evaluate().toString()
    }
}

class BitConsumer(private var bits: List<Int>) {
    fun consume(n: Int) = bits.take(n).also { bits = bits.drop(n) }

    fun consumeAsInt(n: Int) = consume(n).toInt()

    fun consumeNext() = bits[0].also { bits = bits.drop(1) }

    fun isNotEmpty() = bits.isNotEmpty()
}

abstract class Packet(val version: Int) {

    abstract fun evaluate(): Long

    companion object {
        fun parsePacket(bits: BitConsumer): Packet {
            val version = bits.consumeAsInt(3)
            val type = bits.consumeAsInt(3)

            return when (type) {
                0 -> SumPacket(version, bits)
                1 -> ProductPacket(version, bits)
                2 -> MinimumPacket(version, bits)
                3 -> MaximumPacket(version, bits)
                4 -> LiteralPacket(version, bits)
                5 -> GreaterPacket(version, bits)
                6 -> LessPacket(version, bits)
                7 -> EqualPacket(version, bits)
                else -> throw Exception("Invalid packet type '$type'")
            }
        }
    }
}

class LiteralPacket(version: Int, bits: BitConsumer) : Packet(version) {
    val value: Long

    init {
        val valueBits = mutableListOf<Int>()

        do {
            val next = bits.consume(5)
            valueBits.addAll(next.drop(1))
        } while (next[0] == 1)

        value = valueBits.toLong()
    }

    override fun evaluate() = value
}

abstract class OperatorPacket(version: Int, bits: BitConsumer) : Packet(version) {

    val packets: List<Packet>

    init {
        val lengthType = bits.consumeNext()

        packets = mutableListOf<Packet>()

        if (lengthType == 0) {
            val length = bits.consumeAsInt(15)
            val subpacketBits = bits.consume(length).toConsumer()
            while (subpacketBits.isNotEmpty()) {
                packets.add(Packet.parsePacket(subpacketBits))
            }
        } else {
            val numPackets = bits.consumeAsInt(11)
            repeat(numPackets) { packets.add(Packet.parsePacket(bits)) }
        }
    }
}

class SumPacket(version: Int, bits: BitConsumer) : OperatorPacket(version, bits) {
    override fun evaluate() = packets.map { it.evaluate() }.sum()
}

class ProductPacket(version: Int, bits: BitConsumer) : OperatorPacket(version, bits) {
    override fun evaluate() = packets.map { it.evaluate() }.reduce { l, r -> l * r }
}

class MinimumPacket(version: Int, bits: BitConsumer) : OperatorPacket(version, bits) {
    override fun evaluate() = packets.map { it.evaluate() }.min()!!
}

class MaximumPacket(version: Int, bits: BitConsumer) : OperatorPacket(version, bits) {
    override fun evaluate() = packets.map { it.evaluate() }.max()!!
}

class GreaterPacket(version: Int, bits: BitConsumer) : OperatorPacket(version, bits) {
    override fun evaluate() = if (packets[0].evaluate() > packets[1].evaluate()) 1L else 0L
}

class LessPacket(version: Int, bits: BitConsumer) : OperatorPacket(version, bits) {
    override fun evaluate() = if (packets[0].evaluate() < packets[1].evaluate()) 1L else 0L
}

class EqualPacket(version: Int, bits: BitConsumer) : OperatorPacket(version, bits) {
    override fun evaluate() = if (packets[0].evaluate() == packets[1].evaluate()) 1L else 0L
}

fun List<Int>.toConsumer() = BitConsumer(this)

fun List<Int>.toInt() = this.reversed().reduceIndexed { i, l, r -> l + (r shl i) }

fun List<Int>.toLong() = this.reversed().map(Int::toLong).reduceIndexed { i, l, r -> l + (r shl i) }

fun String.toBinary() =
        this.map { it.toString().toInt(16).toString(2) }
                .map { MutableList(4 - it.length) { 0 } + it.map { it.toInt() - 48 } }
                .flatten()
