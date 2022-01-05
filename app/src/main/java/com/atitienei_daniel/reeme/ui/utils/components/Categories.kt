package com.atitienei_daniel.reeme.ui.utils.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow

@ExperimentalMaterialApi
@Composable
fun Categories(
    categories: List<String>,
    selectedCategories: List<String>,
    onCategoryClick: (index: Int, isSelected: Boolean) -> Unit,
    onCreateCategoryClick: () -> Unit,
) {
    Column {
        Text(text = "Categories")

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            crossAxisSpacing = 10.dp,
            mainAxisSpacing = 10.dp
        ) {
            repeat(categories.size) { index ->
                val isSelected = selectedCategories.contains(categories[index])

                val backgroundColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background)

                val textColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.primary)

                Card(
                    onClick = {
                        onCategoryClick(index, isSelected)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(backgroundColor)
                            .padding(horizontal = 15.dp, vertical = 5.dp),
                    ) {
                        Text(
                            text = categories[index],
                            color = textColor
                        )
                    }
                }
            }

            CreateNewCategoryButton(onClick = onCreateCategoryClick)
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CreateNewCategoryButton(
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 15.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Create new category",
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                Icons.Rounded.Add,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}