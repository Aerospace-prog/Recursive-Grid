package com.example.recursion_grid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recursion_grid.ui.theme.EvenGray
import com.example.recursion_grid.ui.theme.LockedRed
import com.example.recursion_grid.ui.theme.OddNavy


@Composable
fun GridCell(
    value: Int,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor: Color
    val textColor: Color

    when {
        isLocked -> {
            backgroundColor = LockedRed
            textColor = Color.White
        }
        value % 2 != 0 -> {
            backgroundColor = OddNavy
            textColor = Color.White
        }
        else -> {
            backgroundColor = EvenGray
            textColor = Color.Black
        }
    }

    val shape = RoundedCornerShape(4.dp)
    val shadowColorVal = Color.Black
    val shadowOffsetDp = 2.dp

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .drawBehind {
                val offsetPx = shadowOffsetDp.toPx()
                val cornerPx = 4.dp.toPx()
                drawRoundRect(
                    color = shadowColorVal,
                    topLeft = Offset(offsetPx, offsetPx),
                    size = Size(size.width, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerPx, cornerPx)
                )
            }
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (isLocked) Modifier else Modifier.clickable { onClick() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            color = textColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
