package com.tugasin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tugasin.app.data.TaskRepository
import com.tugasin.app.ui.AddTaskScreen
import com.tugasin.app.ui.EditTaskScreen
import com.tugasin.app.ui.HomeScreen
import com.tugasin.app.ui.theme.TugasinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TugasinTheme {
                Box(
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
                    }
                ) {
                    val navController = rememberNavController()
                    val repository = remember { TaskRepository() }

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                repository = repository,
                                onNavigateToAdd = { navController.navigate("add_task") },
                                onNavigateToEdit = { taskId ->
                                    navController.navigate("edit_task/$taskId")
                                }
                            )
                        }
                        composable("add_task") {
                            AddTaskScreen(
                                repository = repository,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "edit_task/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
                            EditTaskScreen(
                                taskId = taskId,
                                repository = repository,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}