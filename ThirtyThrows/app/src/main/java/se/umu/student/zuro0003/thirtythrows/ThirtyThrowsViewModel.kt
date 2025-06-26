package se.umu.student.zuro0003.thirtythrows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class ThirtyThrowsViewModel : ViewModel() {
    val dices = MutableLiveData<Array<Int>>(arrayOf(-1, 1, 1, 2, 6, -5))
    var rolls = 3

    var SCORE_OPTIONS = listOf<String>(
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

    fun toggleDice(index: Int) {
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

    fun getScore(target: Int): Int {
        val currentArray = dices.value ?: return -1
        // Convert locked/negative dice to regular dice
        val original = currentArray.copyOf().map { kotlin.math.abs(it) }.toTypedArray()
        // Handle "Lows" which is the sum of Ones, Twos and Threes
        if (target == 0) {
            var score = 0
            for (num in original) {
                score += if (num <= 3) num else 0
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