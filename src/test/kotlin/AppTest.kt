import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {

    @Test
    fun testDay01() {
        assertDay(Day01(), "7", "5")
    }

    @Test
    fun testDay02() {
        assertDay(Day02(), "150", "900")
    }

    @Test
    fun testDay03() {
        assertDay(Day03(), "198", "230")
    }

    @Test
    fun testDay04() {
        assertDay(Day04(), "4512", "1924")
    }

    @Test
    fun testDay05() {
        assertDay(Day05(), "5", "12")
    }

    @Test
    fun testDay06() {
        assertDay(Day06(), "5934", "26984457539")
    }

    @Test
    fun testDay07() {
        assertDay(Day07(), "37", "168")
    }

    @Test
    fun testDay08() {
        assertDay(Day08(), "26", "61229")
    }

    @Test
    fun testDay09() {
        assertDay(Day09(), "15", "1134")
    }

    @Test
    fun testDay10() {
        assertDay(Day10(), "26397", "288957")
    }

    @Test
    fun testDay11() {
        assertDay(Day11(), "1656", "195")
    }

    @Test
    fun testDay12() {
        assertDay(Day12(), "19", "103")
    }

    @Test
    fun testDay13() {
        assertDay(Day13(), "17", "#####\n#   #\n#   #\n#   #\n#####")
    }

    @Test
    fun testDay14() {
        assertDay(Day14(), "1588", "2188189693529")
    }
    
    @Test
    fun testDay15() {
        assertDay(Day15(), "40", "315")
    }

    @Test
    fun testDay16() {
        assertDay(Day16(), "14", "3")
    }

    @Test
    fun testDay17() {
        assertDay(Day17(), "45", "112")
    }

    @Test
    fun testDay18() {
        assertDay(Day18(), "4140", "3993")
    }

    @Test
    fun testDay19() {
        assertDay(Day19(), "79", "3621")
    }

    @Test
    fun testDay20() {
        assertDay(Day20(), "35", "3351")
    }

    @Test
    fun testDay21() {
        assertDay(Day21(), "739785", "444356092776315")
    }

    private fun assertDay(day: Solvable, expectedA: String, expectedB: String) {
        assertEquals(expectedA, day.debugA())
        assertEquals(expectedB, day.debugB())
    }
}
