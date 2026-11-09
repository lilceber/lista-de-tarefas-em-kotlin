package com.example.listadetarefas.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.listadetarefas.data.Task
import com.example.listadetarefas.ui.components.AddButton
import com.example.listadetarefas.ui.components.TaskItem
import com.example.listadetarefas.ui.theme.Black
import com.example.listadetarefas.ui.theme.Muted
import com.example.listadetarefas.ui.theme.Navy
import com.example.listadetarefas.ui.theme.NavyDark
import com.example.listadetarefas.ui.theme.NavyLight
import com.example.listadetarefas.ui.theme.White
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAdd: () -> Unit,
    onOpen: (Task) -> Unit
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val done = tasks.count { it.done }
    val total = tasks.size
    val target = if (total == 0) 0f else done.toFloat() / total
    val progress by animateFloatAsState(target, label = "progress")
    val dateText = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM", Locale("pt", "BR")))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(dateText.uppercase(), color = Muted, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "O que vamos fazer hoje?",
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            Surface(shape = RoundedCornerShape(20.dp), color = Navy, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Seu progresso", color = White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text("${(progress * 100).toInt()}%", color = White, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
                        color = NavyLight,
                        trackColor = Black
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Você concluiu $done de $total tarefas.",
                        color = Muted,
                        fontSize = 12.sp
                    )
                }
            }
        }
        item {
            Text("Minhas tarefas", color = White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
        if (tasks.isEmpty()) {
            item { Text("Nenhuma tarefa para hoje.", color = Muted, fontSize = 14.sp) }
        }
        items(tasks, key = { it.id }) { task ->
            TaskItem(task, onToggle = { viewModel.toggle(task) }, onClick = { onOpen(task) })
        }
        item {
            Box(Modifier.fillMaxWidth().padding(top = 4.dp), contentAlignment = Alignment.Center) {
                AddButton(onClick = onAdd)
            }
        }
    }
}
