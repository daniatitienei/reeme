package com.atitienei_daniel.reeme.ui.utils.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.ui.theme.Red900

@Composable
fun CreateCategoryAlertDialog(
    newCategoryTitle: String,
    onValueChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Text(text = "Create reminder")
                }

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = newCategoryTitle,
                    onValueChange = onValueChange,
                    placeholder = {
                        Text(
                            text = "Category name",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel", color = Red900)
                    }

                    TextButton(
                        onClick = onSaveClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Create", color = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    )
}