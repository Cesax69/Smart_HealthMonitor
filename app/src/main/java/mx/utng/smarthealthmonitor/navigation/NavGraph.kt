package mx.utng.smarthealthmonitor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.utng.smarthealthmonitor.ui.screens.AlertaScreen
import mx.utng.smarthealthmonitor.ui.screens.DashboardScreen
import mx.utng.smarthealthmonitor.ui.screens.HistorialScreen
import mx.utng.smarthealthmonitor.ui.screens.LoginScreen

@Composable
fun SmartHealthNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onHistorialClick = { navController.navigate(Screen.Historial.route) }
            )
        }
        composable(Screen.Historial.route) {
            HistorialScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Alerta.route) {
            AlertaScreen(
                fc = 145, 
                onDismiss = { navController.popBackStack() },
                onConfirmar = { _ ->
                    navController.popBackStack()
                }
            )
        }
    }
}
