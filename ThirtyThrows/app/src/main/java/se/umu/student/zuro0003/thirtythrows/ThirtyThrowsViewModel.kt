package se.umu.student.zuro0003.thirtythrows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class ThirtyThrowsViewModel : ViewModel() {
    val dices = MutableLiveData<Array<Int>>(arrayOf(-1, 1, 1, 2, 6, -5))
    var rolls = 3

    fun toggleDice(index: Int) {
        val currentArray = dices.value ?: return

        val newArray = currentArray.copyOf()
        newArray[index] *= -1
        dices.value = newArray
    }

    suspend fun roll() {
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
}