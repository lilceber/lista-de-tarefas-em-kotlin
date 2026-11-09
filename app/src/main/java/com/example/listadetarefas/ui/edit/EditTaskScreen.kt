package com.example.listadetarefas.ui.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listadetarefas.ui.components.Categories
import com.example.listadetarefas.ui.components.TaskIcons
import com.example.listadetarefas.ui.components.toHHmm
import com.example.listadetarefas.ui.theme.Muted
import com.example.listadetarefas.ui.theme.Navy
import com.example.listadetarefas.ui.theme.NavyLight
import com.example.listadetarefas.ui.theme.White
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(viewModel: EditTaskViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val date = LocalDate.ofEpochDay(viewModel.dateEpochDay)
    val dateText = date.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("pt", "BR")))

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = White,
        unfocusedTextColor = White,
        focusedBorderColor = NavyLight,
        unfocusedBorderColor = Navy,
        focusedLabelColor = White,
        unfocusedLabelColor = Muted,
        cursorColor = White,
        errorBorderColor = Color(0xFFE57373),
        errorLabelColor = Color(0xFFE57373),
        errorTextColor = White
    )

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = White)
            }
            Text(
                if (viewModel.isEditing) "Detalhes da tarefa" else "Nova tarefa",
                color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold
            )
        }

        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it; viewModel.showTitleError = false },
                label = { Text("Título") },
                isError = viewModel.showTitleError,
                supportingText = { if (viewModel.showTitleError) Text("Informe um título") },
                singleLine = true,
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Descrição") },
                minLines = 3,
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> viewModel.dateEpochDay = LocalDate.of(y, m + 1, d).toEpochDay() },
                            date.year, date.monthValue - 1, date.dayOfMonth
                        ).show()
                    },
                    border = BorderStroke(1.dp, NavyLight),
                    modifier = Modifier.weight(1f)
                ) { Text(dateText, color = White, fontSize = 13.sp) }

                OutlinedButton(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, h, min -> viewModel.timeMinutes = h * 60 + min },
                            viewModel.timeMinutes / 60, viewModel.timeMinutes % 60, true
                        ).show()
                    },
                    border = BorderStroke(1.dp, NavyLight)
                ) { Text(viewModel.timeMinutes.toHHmm(), color = White) }
            }

            Text("Categoria", color = Muted, fontSize = 13.sp)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Categories.forEach { cat ->
                    FilterChip(
                        selected = viewModel.category == cat,
                        onClick = { viewModel.category = cat },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Transparent,
                            labelColor = Muted,
                            selectedContainerColor = NavyLight,
                            selectedLabelColor = White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = viewModel.category == cat,
                            borderColor = Navy,
                            selectedBorderColor = NavyLight
                        )
                    )
                }
            }

            Text("Ícone", color = Muted, fontSize = 13.sp)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TaskIcons.all.forEach { (key, icon) ->
                    val selected = viewModel.iconKey == key
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (selected) NavyLight else Navy)
                            .border(1.dp, if (selected) White else Color.Transparent, CircleShape)
                            .clickable { viewModel.iconKey = key },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = key, tint = White)
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Button(
                onClick = { viewModel.save(onBack) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyLight, contentColor = White),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Salvar", fontSize = 16.sp) }

            if (viewModel.isEditing) {
                TextButton(
                    onClick = { viewModel.delete(onBack) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Excluir tarefa", color = Color(0xFFE57373)) }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
