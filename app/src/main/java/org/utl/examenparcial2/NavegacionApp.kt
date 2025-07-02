package org.utl.examenparcial2

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("examen") { ExamenScreen(navController) }
        composable("resultados") { ResultadosScreen(navController) }
        composable("resumen") { ResumenScreen() }
    }
}
