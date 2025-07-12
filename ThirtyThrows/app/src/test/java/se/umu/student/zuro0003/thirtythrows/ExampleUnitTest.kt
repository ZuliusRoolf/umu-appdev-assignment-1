package se.umu.student.zuro0003.thirtythrows

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private lateinit var game: ThirtyThrowsViewModel

    @Before
    fun setup() {
        game = ThirtyThrowsViewModel(SavedStateHandle())
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun a() {
        assertEquals(5, getScore(5, intArrayOf(1, 1, 1, 1, 1, 1)))
        assertEquals(6, getScore(6, intArrayOf(1, 1, 1, 1, 1, 1)))
        assertEquals(6, getScore(3, intArrayOf(1, 1, 1, 1, 1, 1)))
        assertEquals(30, getScore(5, intArrayOf(5, 5, 5, 5, 5, 5)))

        assertEquals(8, getScore(4, intArrayOf(5, 4, 3, 2, 1, 1)))
        assertEquals(16, getScore(8, intArrayOf(5, 4, 3, 2, 1, 1)))

        assertEquals(14, getScore(7, intArrayOf(3, 3, 2, 2, 2, 2)))
        assertEquals(16, getScore(8, intArrayOf(6, 4, 3, 2, 2, 2)))
    }

    fun getScore(target: Int, intArray: IntArray): Int {
        return game.combinationSumCounts(intArray, target).size * target
    }
}