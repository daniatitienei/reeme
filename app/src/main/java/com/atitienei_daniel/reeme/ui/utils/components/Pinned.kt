package com.atitienei_daniel.reeme.ui.utils.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Pinned(
    isPinned: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.clickable {
            onCheckedChange(!isPinned)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isPinned,
            onCheckedChange = onCheckedChange
        )
        Text(text = "Pinned")
    }
}