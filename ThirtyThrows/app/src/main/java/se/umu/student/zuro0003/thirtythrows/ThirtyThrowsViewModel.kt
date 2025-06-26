package se.umu.student.zuro0003.thirtythrows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThirtyThrowsViewModel : ViewModel() {
    val dices = MutableLiveData<Array<Int>>(arrayOf(-1, 1, 1, 2, 6, -5))

    fun toggleDice(index: Int) {
        val currentArray = dices.value ?: return

        val newArray = currentArray.copyOf()
        newArray[index] *= -1
        dices.value = newArray
    }
}