package com.example.listadetarefas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listadetarefas.data.Task
import com.example.listadetarefas.ui.theme.Muted
import com.example.listadetarefas.ui.theme.Navy
import com.example.listadetarefas.ui.theme.NavyLight
import com.example.listadetarefas.ui.theme.White

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Navy,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.done,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = NavyLight,
                    uncheckedColor = Muted,
                    checkmarkColor = White
                )
            )
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = if (task.done) Muted else White,
                    fontSize = 17.sp,
                    textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = "${task.timeMinutes.toHHmm()} · ${task.category}",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = TaskIcons.get(task.iconKey),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/** Botão "+" de nova tarefa. */
@Composable
fun AddButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = NavyLight, contentColor = White),
        modifier = modifier.width(120.dp)
    ) {
        Text("+", fontSize = 24.sp)
    }
}
