package com.atitienei_daniel.reeme.ui.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SelectReminderCardColor(
    colors: List<Color>,
    selectedColorIndex: Int,
    onClick: (Int) -> Unit,
) {
    FlowRow(
        mainAxisSpacing = 15.dp,
        crossAxisSpacing = 10.dp,
    ) {
        repeat(colors.size) { index ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors[index])
                    .size(42.dp)
                    .clickable {
                        onClick(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedColorIndex == index)
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colors.primary
                    )
            }
        }
    }
}