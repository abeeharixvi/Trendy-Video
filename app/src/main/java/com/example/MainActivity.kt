package com.example

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.player.TrendyPlayerManager
import com.example.ui.navigation.TrendyAppRoot
import com.example.ui.permission.PermissionScreen
import com.example.ui.permission.getRequiredVideoPermission
import com.example.ui.theme.TrendyDeepBlack
import com.example.ui.theme.TrendyTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var playerManager: TrendyPlayerManager
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as TrendyApplication
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(app.repository, app.preferences)
        )[MainViewModel::class.java]

        playerManager = TrendyPlayerManager(this, lifecycleScope)

        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val lifecycleOwner = LocalLifecycleOwner.current

            // Check permission initially and whenever lifecycle resumes
            var hasPermission by remember {
                mutableStateOf(checkVideoPermission())
            }

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> {
                            val granted = checkVideoPermission()
                            hasPermission = granted
                            viewModel.updatePermissionStatus(granted)
                            playerManager.resume()
                        }
                        Lifecycle.Event.ON_PAUSE -> {
                            playerManager.pause()
                        }
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            LaunchedEffect(hasPermission) {
                viewModel.updatePermissionStatus(hasPermission)
            }

            TrendyTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = TrendyDeepBlack
                ) {
                    if (hasPermission) {
                        TrendyAppRoot(
                            viewModel = viewModel,
                            playerManager = playerManager
                        )
                    } else {
                        PermissionScreen(
                            onPermissionGranted = {
                                hasPermission = true
                                viewModel.updatePermissionStatus(true)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun checkVideoPermission(): Boolean {
        val perm = getRequiredVideoPermission()
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }
}
