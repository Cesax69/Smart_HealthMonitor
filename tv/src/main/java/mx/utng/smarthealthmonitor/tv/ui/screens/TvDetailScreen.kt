package mx.utng.smarthealthmonitor.tv.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.tv.material3.Text

@Composable
fun TvDetailScreen(lecturaId: Int, navController: NavController) {
    Text("Detail Screen for ID: $lecturaId")
}
