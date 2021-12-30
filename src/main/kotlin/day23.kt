import java.util.*
import java.util.PriorityQueue
import kotlin.collections.HashMap
import kotlin.comparisons.compareBy
import kotlin.math.*

class Day23 : Solvable("23") {

    override fun solveA(input: List<String>) = dijkstra(GameState.from(input)).toString()

    override fun solveB(input: List<String>): String {
        val state = GameState.from(input).also { it.unfold() }

        return dijkstra(state).toString()
    }

    private fun dijkstra(start: GameState): Int {
        val distanceMap = HashMap<GameState, Int>()
        val queue = PriorityQueue<GameState>(compareBy { distanceMap[it] })

        distanceMap[start] = 0
        queue.add(start)

        fun addNewState(statePair: Pair<GameState, Int>) {
            if (distanceMap[statePair.first] ?: Int.MAX_VALUE > statePair.second) {
                distanceMap[statePair.first] = statePair.second
                queue.add(statePair.first)
            }
        }

        while (queue.isNotEmpty()) {
            val el = queue.remove()
            el.getNextStates(distanceMap[el]!!).forEach { addNewState(it) }
        }

        return distanceMap[GameState.finalPosition]!!
    }
}

data class GameState(
        val hallway: Hallway,
        val roomA: Room,
        val roomB: Room,
        val roomC: Room,
        val roomD: Room
) {

    fun unfold() {
        roomA.p4 = roomA.p2
        roomB.p4 = roomB.p2
        roomC.p4 = roomC.p2
        roomD.p4 = roomD.p2

        roomA.p2 = 'D'
        roomB.p2 = 'C'
        roomC.p2 = 'B'
        roomD.p2 = 'A'

        roomA.p3 = 'D'
        roomB.p3 = 'B'
        roomC.p3 = 'A'
        roomD.p3 = 'C'
    }

    fun getNextStates(prev: Int = 0): List<Pair<GameState, Int>> {
        val states = mutableListOf<Pair<GameState, Int>>()

        // Move from hallway into room
        for (i in hallway.toArray().indices) {
            val cLeave = hallway[i]
            if (cLeave == '.') continue
            if (!roomOf(cLeave).letterCanEnter()) continue
            val d = hallway.distanceToRoom(i)
            if (d < 0) continue

            val state = this.deepCopy()
            val dEnter = state.roomOf(cLeave).letterEnters()
            state.hallway[i] = '.'

            states.add(state to prev + (d + dEnter) * letterCost(cLeave))
        }

        // Move from room into room (returns true if room to hallway might still be possible)
        fun moveFromRoomToRoom(room: Room): Boolean {
            val cLeave = room.letterCanLeave()
            if (cLeave == '.') return false
            val canEnter = roomOf(cLeave).letterCanEnter()
            if (!canEnter) return true
            val d = hallway.distanceBetweenRooms(roomOf(cLeave), room)
            if (d < 0) return true

            val state = this.deepCopy()
            val from = state.roomOf(room.goal)
            val dLeave = from.letterLeaves()
            val to = state.roomOf(cLeave)
            val dEnter = to.letterEnters()

            states.add(state to prev + (dLeave + d + dEnter) * letterCost(cLeave))
            return false
        }

        // Move from room into hallway
        fun moveFromRoomToHallway(room: Room) {
            val cLeave = room.letterCanLeave()
            if (cLeave == '.') return

            var startLeft = Math.floor(room.pos()).toInt()
            while (startLeft >= 0 && this.hallway[startLeft] == '.') {
                val state = this.deepCopy()
                val from = state.roomOf(room.goal)
                val dLeave = from.letterLeaves()
                state.hallway[startLeft] = cLeave
                val d = from.index() - Hallway.posToIndex(startLeft)
                states.add(state to prev + (dLeave + d) * letterCost(cLeave))
                startLeft--
            }

            var startRight = Math.ceil(room.pos()).toInt()
            while (startRight <= 6 && this.hallway[startRight] == '.') {
                val state = this.deepCopy()
                val from = state.roomOf(room.goal)
                val dLeave = from.letterLeaves()
                state.hallway[startRight] = cLeave
                val d = Hallway.posToIndex(startRight) - from.index()
                states.add(state to prev + (dLeave + d) * letterCost(cLeave))
                startRight++
            }
        }

        fun moveFromRoom(room: Room) {
            moveFromRoomToRoom(room)
            moveFromRoomToHallway(room)
        }

        moveFromRoom(roomA)
        moveFromRoom(roomB)
        moveFromRoom(roomC)
        moveFromRoom(roomD)

        return states
    }

    private fun roomOf(ch: Char) =
            when (ch) {
                'A' -> roomA
                'B' -> roomB
                'C' -> roomC
                'D' -> roomD
                else -> throw Exception("invalid character '$ch'")
            }

    private fun letterCost(ch: Char) =
            when (ch) {
                'A' -> 1
                'B' -> 10
                'C' -> 100
                'D' -> 1000
                else -> throw Exception("invalid character '$ch'")
            }

    fun deepCopy() = 
            GameState(
                    hallway.copy(),
                    roomA.copy(),
                    roomB.copy(),
                    roomC.copy(),
                    roomD.copy()
            )

    companion object {
        fun from(input: List<String>) =
                GameState(
                                Hallway(),
                                Room('A', input[2][3], input[3][3]),
                                Room('B', input[2][5], input[3][5]),
                                Room('C', input[2][7], input[3][7]),
                                Room('D', input[2][9], input[3][9])
                        )
                        .apply {
                            this.hallway[0] = input[1][1]
                            this.hallway[1] = input[1][2]
                            this.hallway[2] = input[1][4]
                            this.hallway[3] = input[1][6]
                            this.hallway[4] = input[1][8]
                            this.hallway[5] = input[1][10]
                            this.hallway[6] = input[1][11]
                        }
        val finalPosition = GameState(Hallway(), Room.A, Room.B, Room.C, Room.D)
    }
}

data class Room(
        val goal: Char,
        var p1: Char,
        var p2: Char,
        var p3: Char = goal,
        var p4: Char = goal
) {

    fun letterCanLeave(): Char {
        if (!containsForeigns()) return '.'
        if (p1 != '.') return p1
        if (p2 != '.') return p2
        if (p3 != '.') return p3
        if (p4 != '.') return p4
        return '.'
    }

    fun letterCanEnter(): Boolean {
        if (!containsForeigns()) {
            return p4 == '.' || p3 == '.' || p2 == '.' || p1 == '.'
        }
        return false
    }

    fun letterEnters(): Int {
        if (p4 == '.') return 4.also { p4 = goal }
        if (p3 == '.') return 3.also { p3 = goal }
        if (p2 == '.') return 2.also { p2 = goal }
        if (p1 == '.') return 1.also { p1 = goal }
        throw Exception("no letter can enter")
    }

    fun letterLeaves(): Int {
        if (p1 != '.') return 1.also { p1 = '.' }
        if (p2 != '.') return 2.also { p2 = '.' }
        if (p3 != '.') return 3.also { p3 = '.' }
        if (p4 != '.') return 4.also { p4 = '.' }
        throw Exception("no letter can leave")
    }

    fun pos() = Room.pos(goal)

    fun index() = Room.index(goal)

    private fun containsForeigns() =
            isForeign(p1) || isForeign(p2) || isForeign(p3) || isForeign(p4)

    private fun isForeign(c: Char) = c != goal && c != '.'

    companion object {
        val A = Room('A', 'A', 'A', 'A', 'A')
        val B = Room('B', 'B', 'B', 'B', 'B')
        val C = Room('C', 'C', 'C', 'C', 'C')
        val D = Room('D', 'D', 'D', 'D', 'D')

        fun pos(c: Char) =
                when (c) {
                    'A' -> 1.5
                    'B' -> 2.5
                    'C' -> 3.5
                    'D' -> 4.5
                    else -> throw Exception("invalid letter $c")
                }

        fun index(c: Char) =
                when (c) {
                    'A' -> 2
                    'B' -> 4
                    'C' -> 6
                    'D' -> 8
                    else -> throw Exception("invalid letter $c")
                }
    }
}

data class Hallway(
        var p0: Char = '.',
        var p1: Char = '.',
        var p2: Char = '.',
        var p3: Char = '.',
        var p4: Char = '.',
        var p5: Char = '.',
        var p6: Char = '.'
) {
    private val realDistance = arrayOf(0, 1, 3, 5, 7, 9, 10)

    fun distanceToRoom(pos: Int): Int {
        val character = this[pos]
        if (character == '.') return -1
        val roomPos =
                Room.pos(character).let {
                    if (pos < it) Math.floor(it).toInt() else Math.ceil(it).toInt()
                }
        val occupiedPlaces =
                (Math.min(roomPos, pos)..Math.max(roomPos, pos)).count { this[it] != '.' }
        if (occupiedPlaces > 1) return -1
        return Math.abs(Room.index(character) - realDistance[pos])
    }

    fun distanceBetweenRooms(room1: Room, room2: Room): Int {
        if (room1.goal == room2.goal) return -1
        val (roomMin, roomMax) = listOf(room1, room2).sortedBy { it.goal }
        val occupiedPlaces =
                (Math.ceil(roomMin.pos()).toInt()..Math.floor(roomMax.pos()).toInt()).count {
                    this[it] != '.'
                }
        if (occupiedPlaces >= 1) return -1
        return roomMax.index() - roomMin.index()
    }

    operator fun set(i: Int, v: Char) {
        when (i) {
            0 -> p0 = v
            1 -> p1 = v
            2 -> p2 = v
            3 -> p3 = v
            4 -> p4 = v
            5 -> p5 = v
            6 -> p6 = v
        }
    }

    operator fun get(i: Int): Char {
        return when (i) {
            0 -> p0
            1 -> p1
            2 -> p2
            3 -> p3
            4 -> p4
            5 -> p5
            6 -> p6
            else -> throw Exception("Out of bounds: $i")
        }
    }

    fun toArray() = CharArray(7) { i -> this[i] }

    companion object {
        fun posToIndex(pos: Int) =
                when (pos) {
                    6 -> 10
                    5 -> 9
                    4 -> 7
                    3 -> 5
                    2 -> 3
                    else -> pos
                }
    }
}