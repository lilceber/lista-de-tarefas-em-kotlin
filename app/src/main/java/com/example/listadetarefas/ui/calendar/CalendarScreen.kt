package com.example.listadetarefas.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.listadetarefas.data.Task
import com.example.listadetarefas.ui.components.AddButton
import com.example.listadetarefas.ui.components.TaskItem
import com.example.listadetarefas.ui.theme.Muted
import com.example.listadetarefas.ui.theme.Navy
import com.example.listadetarefas.ui.theme.NavyLight
import com.example.listadetarefas.ui.theme.White
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val PtBr = Locale("pt", "BR")

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onAdd: (LocalDate) -> Unit,
    onOpen: (Task) -> Unit
) {
    val selected by viewModel.selected.collectAsStateWithLifecycle()
    val weekTasks by viewModel.weekTasks.collectAsStateWithLifecycle()

    val start = weekStart(selected)
    val days = (0L..6L).map { start.plusDays(it) }
    val today = LocalDate.now()
    val dayTasks = weekTasks.filter { it.dateEpochDay == selected.toEpochDay() }
    val monthTitle = selected.format(DateTimeFormatter.ofPattern("MMMM 'de' yyyy", PtBr))
        .replaceFirstChar { it.uppercase() }
    val rangeFmt = DateTimeFormatter.ofPattern("d MMM", PtBr)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Calendário", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(monthTitle, color = Muted, fontSize = 14.sp)
            }
        }
        item {
            Surface(shape = RoundedCornerShape(20.dp), color = Navy, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { viewModel.shiftWeek(-1) }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Semana anterior", tint = White)
                        }
                        Text(
                            "${start.format(rangeFmt)} – ${start.plusDays(6).format(rangeFmt)}",
                            color = White, fontSize = 14.sp
                        )
                        IconButton(onClick = { viewModel.shiftWeek(1) }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Próxima semana", tint = White)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth()) {
                        days.forEach { day ->
                            DayCell(
                                day = day,
                                isSelected = day == selected,
                                isToday = day == today,
                                hasTasks = weekTasks.any { it.dateEpochDay == day.toEpochDay() },
                                onClick = { viewModel.select(day) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        item {
            Text(
                "Tarefas de ${selected.format(DateTimeFormatter.ofPattern("d 'de' MMMM", PtBr))}",
                color = White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
        }
        if (dayTasks.isEmpty()) {
            item { Text("Nenhuma tarefa neste dia.", color = Muted, fontSize = 14.sp) }
        }
        items(dayTasks, key = { it.id }) { task ->
            TaskItem(task, onToggle = { viewModel.toggle(task) }, onClick = { onOpen(task) })
        }
        item {
            Box(Modifier.fillMaxWidth().padding(top = 4.dp), contentAlignment = Alignment.Center) {
                AddButton(onClick = { onAdd(selected) })
            }
        }
    }
}

@Composable
private fun DayCell(
    day: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasTasks: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = day.dayOfWeek.getDisplayName(TextStyle.SHORT, PtBr).replace(".", "").uppercase()
    Column(
        modifier = modifier.clickable(onClick = onClick).padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = Muted, fontSize = 11.sp)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(if (isSelected) NavyLight else Color.Transparent, CircleShape)
                .border(1.dp, if (isToday && !isSelected) White else Color.Transparent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(day.dayOfMonth.toString(), color = White, fontSize = 15.sp)
        }
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier.size(5.dp).background(if (hasTasks) White else Color.Transparent, CircleShape)
        )
    }
}
