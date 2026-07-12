package mx.utng.smarthealthmonitor.tv.ui.screens

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.tv.material3.*

@OptIn(UnstableApi::class)
@ExperimentalTvMaterial3Api
@Composable
fun TvPlaybackScreen(navController: NavController) {
    val context = LocalContext.current
    val TAG = "SmartHealthVideo"
    
    // Crear y recordar ExoPlayer con detector de errores
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    
    // Usar un efecto para los listeners y carga
    LaunchedEffect(exoPlayer) {
        val videoUrl = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
        val mediaItem = MediaItem.fromUri(videoUrl)
        
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e(TAG, "Error de reproducción: ${error.message}")
            }
            override fun onPlaybackStateChanged(state: Int) {
                Log.d(TAG, "Estado del reproductor: $state")
            }
        })
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d(TAG, "Liberando reproductor")
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view: PlayerView ->
                view.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        )

        // Botón Volver
        Surface(
            onClick = { 
                exoPlayer.stop()
                navController.popBackStack() 
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(32.dp),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = Color.White.copy(alpha = 0.8f),
                focusedContainerColor = Color.White
            )
        ) {
            Text(
                "← Volver", 
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
