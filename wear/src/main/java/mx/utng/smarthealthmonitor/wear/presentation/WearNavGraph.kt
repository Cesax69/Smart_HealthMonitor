package mx.utng.smarthealthmonitor.wear.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

object WearScreens {
    const val DASHBOARD = "dash"
    const val ALERTA    = "alert"
    const val HISTORIAL = "hist"
}

@Composable
fun SmartHealthWearNavGraph() {
    val navController = rememberSwipeDismissableNavController()
    val context = LocalContext.current

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = WearScreens.DASHBOARD
    ) {
        composable(route = WearScreens.DASHBOARD) {
            WearDashboardScreen(
                onAlertClick = {
                    navController.navigate(WearScreens.ALERTA)
                },
                onHistoryClick = {
                    Toast.makeText(context, "Ejecutando navigate...", Toast.LENGTH_SHORT).show()
                    navController.navigate(WearScreens.HISTORIAL)
                }
            )
        }
        
        composable(route = WearScreens.ALERTA) {
            val vm: WearDashboardViewModel = viewModel()
            val fc by vm.fc.collectAsState()
            WearAlertaScreen(
                fc = fc,
                onConfirmar = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(route = WearScreens.HISTORIAL) {
            WearHistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
