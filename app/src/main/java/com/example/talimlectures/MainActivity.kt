package com.example.talimlectures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.talimlectures.Destination.NavigationSetup
import com.example.talimlectures.ui.theme.TalimLecturesTheme
import com.example.talimlectures.viewModels.SharedViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private  lateinit var navController: NavHostController
    val sharedViewModel:SharedViewModel by viewModels()
    @OptIn(ExperimentalAnimationApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                navController =     rememberAnimatedNavController()
               NavigationSetup(navController =navController, sharedViewModel = sharedViewModel)
        }
    }
}
