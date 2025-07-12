package se.umu.student.zuro0003.thirtythrows

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScoreOption(
    val title: String,
    var score: Int,
    var locked: Boolean
) : Parcelable

class ThirtyThrowsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val DICES_KEY = "DICES_KEY"
        private const val ROLLS_KEY = "ROLLS_KEY"
        private const val ROUND_KEY = "ROUND_KEY"
        private const val SCOREBOARD_KEY = "SCOREBOARD_KEY"
    }

    private val lowSum = 3
    private val _dices = savedStateHandle.getLiveData(DICES_KEY, Array(6) { 1 })
    val dices: LiveData<Array<Int>> = _dices
    var rolls: Int
        get() = savedStateHandle[ROLLS_KEY] ?: 3
        set(value) = savedStateHandle.set(ROLLS_KEY, value)

    // Max round is determined by scoreNames size
    var round: Int
        get() = savedStateHandle[ROUND_KEY] ?: 1
        set(value) = savedStateHandle.set(ROUND_KEY, value)

    val scoreNames = listOf<String>(
        "Lows",
        "Fours",
        "Fives",
        "Sixes",
        "Sevens",
        "Eights",
        "Nines",
        "Tens",
        "Elevens",
        "Twelves"
    )

    private val _scoreBoard = savedStateHandle.getLiveData(
        SCOREBOARD_KEY,
        scoreNames.map { name -> ScoreOption(name, 0, false) }
    )
    val scoreBoard: LiveData<List<ScoreOption>> = _scoreBoard

    fun resetGame() {
        rolls = 3
        round = 1
        _dices.value = Array<Int>(6) { 1 }
        _scoreBoard.value = scoreNames.map { name -> ScoreOption(name, 0, false) }
    }

    //    Toggles all locked dies to neutral and resets the rolls
    fun nextRound() {
        val currentArray = dices.value ?: return
        val newArray = currentArray.copyOf()
        for (i in newArray.indices)
            if (newArray[i] < 0) newArray[i] *= -1
        _dices.value = newArray
        rolls = 3
        round++
    }

    //    Checks if round 11 is bigger than 10
    fun isGameOver(): Boolean {
        return round > scoreNames.size
    }

    //    Adds all scores from the scoreboard together
    fun getFinalScore(): Int {
        val currentArray = scoreBoard.value ?: return 0
        var totalScore = 0
        for (item in currentArray) {
            totalScore += item.score
        }
        return totalScore
    }

    //    Locked dice is represented by negative value, hence multiplied by -1
    fun toggleDice(index: Int) {
        if (rolls == 3) return // Ensures that the player cannot toggle dies from last round
        val currentArray = dices.value ?: return

        val newArray = currentArray.copyOf()
        newArray[index] *= -1
        _dices.value = newArray
    }

    //    Asynchronous dice roll, will randomize all unlocked dice for 0.5 seconds
    suspend fun rollDice() {
        val currentArray = dices.value ?: return
        if (rolls > 0) rolls-- else return
        val newArray = currentArray.copyOf()
        repeat(10) {
            for (i in newArray.indices) {
                if (newArray[i] > 0) // If dice unlocked
                    newArray[i] = (1..6).random()
            }
            delay(50)
            _dices.value = newArray
        }
    }

    //    Update all unlocked scores with the sum of current dice
    fun updateScoreBoard() {
        val currentScoreBoard = scoreBoard.value ?: return
        val updatedScoreBoard = currentScoreBoard.toMutableList()

        for (i in updatedScoreBoard.indices) {
            if (updatedScoreBoard[i].locked) continue
            // "Lows" is considered as '0' and "Fours" is considered as '1+3'
            val target = if (i == 0) 0 else i + lowSum
            updatedScoreBoard[i].score = getScore(target)
        }
        _scoreBoard.value = updatedScoreBoard
        return
    }

    //    Magical algorithm, finds the best combinations and then multiplies it with the sum in question
//    1,2,3,4,5,6 where target=6 will give [6] [5,1] [4,2] which is 3*6=18
    fun getScore(target: Int): Int {
        val currentArray = dices.value ?: return -1
        // Convert locked/negative dice to regular dice
        val original = currentArray.copyOf().map { kotlin.math.abs(it) }.toIntArray()
        // Handle "Lows" which is the sum of Ones, Twos and Threes
        if (target == 0) {
            var score = 0
            for (num in original) {
                score += if (num <= lowSum) num else 0
            }
            return score
        }

        // Handle all other sums
        val combinations = combinationSumCounts(original, target)
        return combinations.size * target
    }

    /** Finds all potential combinations and then removes combinations based on the limited resources. */
    fun combinationSumCounts(candidates: IntArray, target: Int): List<List<Int>> {
        val potentials = mutableListOf<List<Int>>()
        val combination = mutableListOf<Int>()
        val candidates = candidates.toMutableList()
        candidates.sort()
        fun backtrack(start: Int, remain: Int) {
            // Combination == target sum
            if (remain == 0) {
                potentials.add(ArrayList(combination))
                return
            }
            // Combination > target sum
            if (remain < 0) return

            // Combination < target sum
            // Add all potential values recursively to include backtracking
            for (i in start until candidates.size) {
                val current = candidates[i]
                combination.add(current)
                backtrack(i + 1, remain - current)
                combination.removeAt(combination.size - 1)
            }
        }
        backtrack(0, target)

        // Sort potential combinations by size
        potentials.sortWith(Comparator { lis1: List<Int>, lis2: List<Int> -> lis1.size - lis2.size })
        val result = mutableListOf<List<Int>>()

        // Go through each potential combination
        val iterator = potentials.iterator()
        while (iterator.hasNext()) {
            val potentialCombination = iterator.next()
            val tempCandidates = candidates.toMutableList()
            var isValid = true

            // Check if all values exist in original candidates
            for (value in potentialCombination) {
                if (!tempCandidates.remove(value)) {
                    isValid = false
                    break
                }
            }

            // If yes, remove all selected values from the pool
            if (isValid) {
                for (value in potentialCombination) {
                    candidates.remove(value)
                }
                iterator.remove()
                result.add(potentialCombination)
            }
        }
        return result
    }
}