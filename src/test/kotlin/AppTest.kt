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

    private fun assertDay(day: Solvable, expectedA: String, expectedB: String) {
        assertEquals(expectedA, day.debugA())
        assertEquals(expectedB, day.debugB())
    }
}
