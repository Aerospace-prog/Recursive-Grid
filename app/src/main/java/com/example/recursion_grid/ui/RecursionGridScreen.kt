package com.example.recursion_grid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recursion_grid.GridViewModel
import com.example.recursion_grid.ui.theme.OddNavy

@Composable
fun RecursionGridScreen(
    viewModel: GridViewModel = viewModel()
) {
    val cells by viewModel.cells.collectAsState()
    val tapCount by viewModel.tapCount.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 360.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Recursive Grid",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = OddNavy
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tap counter
            Text(
                text = "Taps: $tapCount",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Grid
            Column(
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

            Spacer(modifier = Modifier.height(24.dp))

            // Reset button
            OutlinedButton(
                onClick = { viewModel.resetGrid() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = OddNavy
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Reset Grid",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
