package com.example.listadetarefas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector

/** Ícones que o usuário pode escolher ao criar/editar uma tarefa. */
object TaskIcons {
    val all: Map<String, ImageVector> = linkedMapOf(
        "study" to Icons.Outlined.AutoStories,
        "gym" to Icons.Outlined.FitnessCenter,
        "laptop" to Icons.Outlined.Laptop,
        "book" to Icons.Outlined.Book,
        "work" to Icons.Outlined.Work,
        "school" to Icons.Outlined.School,
        "code" to Icons.Outlined.Code,
        "home" to Icons.Outlined.Home,
        "cart" to Icons.Outlined.ShoppingCart,
        "food" to Icons.Outlined.Restaurant,
        "music" to Icons.Outlined.MusicNote,
        "art" to Icons.Outlined.Brush,
        "travel" to Icons.Outlined.Flight,
        "pets" to Icons.Outlined.Pets,
        "relax" to Icons.Outlined.SelfImprovement,
        "love" to Icons.Outlined.FavoriteBorder,
        "star" to Icons.Outlined.Star
    )

    fun get(key: String): ImageVector = all[key] ?: Icons.Outlined.Star
}

val Categories = listOf("Estudos", "Pessoal", "Trabalho", "Saúde", "Outros")

fun Int.toHHmm(): String = "%02d:%02d".format(this / 60, this % 60)
