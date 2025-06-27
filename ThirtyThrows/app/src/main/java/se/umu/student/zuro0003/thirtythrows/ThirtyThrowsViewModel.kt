package se.umu.student.zuro0003.thirtythrows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

data class ScoreOption(val title: String, var score: Int, var locked: Boolean)

class ThirtyThrowsViewModel : ViewModel() {
    private val LOW_SUM = 3
    val dices = MutableLiveData<Array<Int>>(Array<Int>(6) { 1 })
    var rolls = 3
    var round = 1

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
    val scoreBoard = MutableLiveData<Array<ScoreOption>>(
        Array(scoreNames.size) { i ->
            ScoreOption(scoreNames[i], score = 0, locked = false)
        }
    )

    fun resetGame() {
        rolls = 3
        round = 1
        dices.value = Array<Int>(6) { 1 }
        scoreBoard.value = Array<ScoreOption>(scoreNames.size) { i ->
            ScoreOption(scoreNames[i], score = 0, locked = false)
        }
    }

    fun nextRound() {
        val currentArray = dices.value ?: return
        val newArray = currentArray.copyOf()
        for (i in newArray.indices)
            if (newArray[i] < 0) newArray[i] *= -1
        dices.value = newArray
        rolls = 3
        round++
    }

    fun isGameOver(): Boolean {
        return round > scoreNames.size
    }

    fun getFinalScore(): Int {
        val currentArray = scoreBoard.value ?: return 0
        var totalScore = 0
        for (option in currentArray) {
            totalScore += option.score
        }
        return totalScore
    }

    fun toggleDice(index: Int) {
        if (rolls == 3) return
        val currentArray = dices.value ?: return

        val newArray = currentArray.copyOf()
        newArray[index] *= -1
        dices.value = newArray
    }

    suspend fun rollDice() {
        val currentArray = dices.value ?: return
        if (rolls > 0) rolls-- else return
        val newArray = currentArray.copyOf()
        repeat(10) {
            for (i in newArray.indices) {
                if (newArray[i] > 0)
                    newArray[i] = (1..6).random()
            }
            delay(50)
            dices.value = newArray
        }
    }

    fun updateScoreBoard(): Array<ScoreOption> {
        val currentScoreBoard = scoreBoard.value ?: return scoreBoard.value
        val updatedScoreBoard = currentScoreBoard.copyOf()
        for (i in updatedScoreBoard.indices) {
            if (updatedScoreBoard[i].locked) continue
            val target = if (i == 0) 0 else i + LOW_SUM
            updatedScoreBoard[i].score = getScore(target)
        }
        scoreBoard.value = updatedScoreBoard
        return scoreBoard.value
    }

    fun getScore(target: Int): Int {
        val currentArray = dices.value ?: return -1
        // Convert locked/negative dice to regular dice
        val original = currentArray.copyOf().map { kotlin.math.abs(it) }.toTypedArray()
        // Handle "Lows" which is the sum of Ones, Twos and Threes
        if (target == 0) {
            var score = 0
            for (num in original) {
                score += if (num <= LOW_SUM) num else 0
            }
            return score
        }

        // Algorithm to calculate the best valid combinations
        // Sort the list as dices with highest value is more important
        val sortedDice = original.sortedDescending().toMutableList()
        val combinations = mutableListOf<List<Int>>()
        var i = 0

        // Go through the list and add the next equal or lower value to combination
        // If the sum is less than target, add another equal or lower value
        // If the sum is greater than target, replace the last added value with equal or lower value
        // If the sum is equal to target, add to combinations and exclude values for the next run
        while (i < sortedDice.size) {
            val currentCombination = mutableListOf(sortedDice[i])
            var j = i + 1
            var success = false

            while (j < sortedDice.size) {
                val sum = currentCombination.sum()

                when {
                    sum < target -> {
                        currentCombination.add(sortedDice[j])
                    }

                    sum > target -> {
                        if (currentCombination.isNotEmpty()) {
                            currentCombination.removeAt(currentCombination.lastIndex)
                        }
                        currentCombination.add(sortedDice[j])
                    }
                }

                if (currentCombination.sum() == target) {
                    combinations.add(currentCombination.toList())
                    success = true
                    for (num in currentCombination) {
                        sortedDice.remove(num)
                    }
                    break
                }

                j++
            }
            if (!success) i++
        }
        return combinations.size * target
    }
}