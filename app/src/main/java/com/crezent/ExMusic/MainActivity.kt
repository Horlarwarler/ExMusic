package com.crezent.ExMusic

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import coil.ImageLoader
import com.crezent.common.preference.Preference
import com.crezent.data.util.internet.NetworkMonitorInterface
import com.crezent.design_system.theme.ExMusicTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
  //  private  lateinit var navController: NavHostController
   // val sharedViewModel:SharedViewModel by viewModels()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)

    @Inject
    lateinit var networkMonitor: NetworkMonitorInterface

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var  preference: Preference
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      val shouldShowOnboarding = preference.shouldShowOnboarding()

      setContent {
          ExMusicTheme {
              NavigationSetup(
                  shouldShowOnboarding = shouldShowOnboarding,
                  imageLoader = imageLoader
              )
          }

        }
    }
}
