package com.example.listadetarefas.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listadetarefas.data.TaskRepository
import com.example.listadetarefas.ui.calendar.CalendarScreen
import com.example.listadetarefas.ui.calendar.CalendarViewModel
import com.example.listadetarefas.ui.edit.EditTaskScreen
import com.example.listadetarefas.ui.edit.EditTaskViewModel
import com.example.listadetarefas.ui.home.HomeScreen
import com.example.listadetarefas.ui.home.HomeViewModel
import com.example.listadetarefas.ui.theme.Black
import com.example.listadetarefas.ui.theme.Muted
import com.example.listadetarefas.ui.theme.Navy
import com.example.listadetarefas.ui.theme.White
import java.time.LocalDate

object Routes {
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val EDIT = "edit?taskId={taskId}&day={day}"
    fun edit(taskId: Long, day: Long) = "edit?taskId=$taskId&day=$day"
}

private data class BarItem(val route: String, val label: String, val icon: ImageVector)

private val barItems = listOf(
    BarItem(Routes.HOME, "Início", Icons.Outlined.Home),
    BarItem(Routes.CALENDAR, "Calendário", Icons.Outlined.CalendarMonth)
)

@Composable
fun AppNavigation(repository: TaskRepository) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val showBar = route == Routes.HOME || route == Routes.CALENDAR

    Scaffold(
        containerColor = Black,
        bottomBar = {
            if (showBar) {
                NavigationBar(containerColor = Black) {
                    barItems.forEach { item ->
                        NavigationBarItem(
                            selected = route == item.route,
                            onClick = {
                                nav.navigate(item.route) {
                                    popUpTo(Routes.HOME) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = White,
                                selectedTextColor = White,
                                indicatorColor = Navy,
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                val vm: HomeViewModel = viewModel(factory = VmFactory(repository))
                HomeScreen(
                    viewModel = vm,
                    onAdd = { nav.navigate(Routes.edit(-1, LocalDate.now().toEpochDay())) },
                    onOpen = { nav.navigate(Routes.edit(it.id, it.dateEpochDay)) }
                )
            }
            composable(Routes.CALENDAR) {
                val vm: CalendarViewModel = viewModel(factory = VmFactory(repository))
                CalendarScreen(
                    viewModel = vm,
                    onAdd = { date -> nav.navigate(Routes.edit(-1, date.toEpochDay())) },
                    onOpen = { nav.navigate(Routes.edit(it.id, it.dateEpochDay)) }
                )
            }
            composable(
                route = Routes.EDIT,
                arguments = listOf(
                    navArgument("taskId") { type = NavType.LongType; defaultValue = -1L },
                    navArgument("day") { type = NavType.LongType; defaultValue = 0L }
                )
            ) { entry ->
                val id = entry.arguments?.getLong("taskId") ?: -1L
                val day = entry.arguments?.getLong("day") ?: 0L
                val vm: EditTaskViewModel = viewModel(factory = VmFactory(repository, id, day))
                EditTaskScreen(viewModel = vm, onBack = { nav.popBackStack() })
            }
        }
    }
}
