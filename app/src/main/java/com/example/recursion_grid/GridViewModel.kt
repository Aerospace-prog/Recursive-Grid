package com.example.recursion_grid

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GridViewModel : ViewModel() {

    companion object {
        const val GRID_SIZE = 3
        const val TOTAL_CELLS = GRID_SIZE * GRID_SIZE
        const val LOCK_THRESHOLD = 15
    }

    private val _cells = MutableStateFlow(List(TOTAL_CELLS) { 0 })
    val cells: StateFlow<List<Int>> = _cells.asStateFlow()

    private val _tapCount = MutableStateFlow(0)
    val tapCount: StateFlow<Int> = _tapCount.asStateFlow()

    fun isLocked(value: Int): Boolean = value >= LOCK_THRESHOLD

    fun onCellClick(index: Int) {
        if (index !in 0 until TOTAL_CELLS) return

        val current = _cells.value.toMutableList()

        if (isLocked(current[index])) return

        current[index] = current[index] + 1
        val newValue = current[index]
        _tapCount.value = _tapCount.value + 1

        // Rule A – divisible by 3 (positive only: 3, 6, 9…) → decrement right neighbor
        val column = index % GRID_SIZE
        if (newValue > 0 && newValue % 3 == 0 && column < GRID_SIZE - 1) {
            val rightIndex = index + 1
            if (!isLocked(current[rightIndex])) {
                current[rightIndex] = current[rightIndex] - 1
            }
        }

        // Rule B – divisible by 5 (positive only: 5, 10…) → increment below neighbor by 2
        val row = index / GRID_SIZE
        if (newValue > 0 && newValue % 5 == 0 && row < GRID_SIZE - 1) {
            val belowIndex = index + GRID_SIZE
            if (!isLocked(current[belowIndex])) {
                current[belowIndex] = current[belowIndex] + 2
            }
        }

        _cells.value = current
    }

    fun resetGrid() {
        _cells.value = List(TOTAL_CELLS) { 0 }
        _tapCount.value = 0
    }
}
