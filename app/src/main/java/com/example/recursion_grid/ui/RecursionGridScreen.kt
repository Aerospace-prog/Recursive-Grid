package com.example.recursion_grid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recursion_grid.GridViewModel

@Composable
fun RecursionGridScreen(
    viewModel: GridViewModel = viewModel()
) {
    val cells by viewModel.cells.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 360.dp)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until GridViewModel.GRID_SIZE) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (col in 0 until GridViewModel.GRID_SIZE) {
                        val index = row * GridViewModel.GRID_SIZE + col
                        val value = cells[index]
                        GridCell(
                            value = value,
                            isLocked = viewModel.isLocked(value),
                            onClick = { viewModel.onCellClick(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
