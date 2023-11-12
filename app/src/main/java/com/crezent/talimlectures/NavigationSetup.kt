package com.crezent.talimlectures

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.ImageLoader
import com.crezent.common.util.Constant
import com.crezent.creator_presentation.upload_dashboard.UploadDashboardScreen
import com.crezent.creator_presentation.upload_dashboard.UploadDashboardViewModel
import com.crezent.creator_presentation.upload_file.UploadFileScreen
import com.crezent.creator_presentation.upload_file.UploadFileViewModel
import com.crezent.talimlectures.util.Constant.HOME_SCREEN_NAME
import com.crezent.talimlectures.util.Constant.LOGIN_SCREEN
import com.crezent.talimlectures.util.Constant.MUSIC_SCREEN_NAME
import com.crezent.talimlectures.util.Constant.ONBOARD_SCREEN
import com.crezent.talimlectures.util.Constant.PROFILE_SCREEN_NAME
import com.crezent.talimlectures.util.Constant.PROFILE_UPDATE_SCREEN
import com.crezent.talimlectures.util.Constant.SIGN_UP_SCREEN
import com.crezent.talimlectures.util.Constant.SPLASH_SCREEN
import com.crezent.talimlectures.util.Constant.UPLOAD_DASHBOARD_SCREEN
import com.crezent.talimlectures.util.Constant.UPLOAD_FILE_SCREEN
import com.crezent.talimlectures.util.Screen
import com.crezent.user_presentation.homeScreen.HomeScreen
import com.crezent.user_presentation.homeScreen.HomeScreenModel
import com.crezent.user_presentation.music.MusicScreen
import com.crezent.user_presentation.music.MusicViewModel
import com.crezent.user_presentation.playerScreen.PlayerScreenModel
import com.crezent.user_presentation.profile.ProfileScreen
import com.crezent.user_presentation.profile.ProfileViewModel
import com.crezent.user_presentation.profile_update.ProfileUpdateScreen
import com.crezent.user_presentation.profile_update.ProfileUpdateViewModel
import com.crezent.user_presentation.signIn.SignInScreen
import com.crezent.user_presentation.signIn.SignInScreenModel
import com.crezent.user_presentation.singup.SignUpScreen
import com.crezent.user_presentation.singup.SignUpScreenModel
import com.crezent.user_presentation.splashScreen.SplashScreen
import com.crezent.user_presentation.splashScreen.SplashScreenViewModel
import com.example.onboarding_presentation.OnboardFirstScreen
import com.example.onboarding_presentation.OnboardSecondScreen
import com.example.onboarding_presentation.OnboardThirdScreen
import com.example.onboarding_presentation.OnboardViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable

// Declaring a  navigation settings that contains our Composable


fun  NavigationSetup(
    shouldShowOnboarding:Boolean = true,
    imageLoader: ImageLoader,
) {
    val navController = rememberAnimatedNavController()


    val snackBarHostState = remember {
        SnackbarHostState()
    }


    val startDestination = if (shouldShowOnboarding) Constant.ONBOARD_FIRST_SCREEN else Constant.SPLASH_SCREEN
   // val startDestination = UPLOAD_DASHBOARD_SCREEN

    // Declaring the current screen of our app
    // Using remember to save the state of the screen
    val screen = remember {
        Screen(navController = navController)
    }
    val navigateTo: (String) -> Unit = {
        when (it) {
            PROFILE_SCREEN_NAME -> {

            }

            MUSIC_SCREEN_NAME -> {
                screen.navigateToMusic()
            }

            HOME_SCREEN_NAME -> {
                screen.navigateToHome()
            }

            UPLOAD_DASHBOARD_SCREEN -> {
                screen.navigateToUploadDashboard()
            }

        }
    }



    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        topBar =  {

        },


    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            composable(
                route = Constant.ONBOARD_FIRST_SCREEN,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(700)
                    )
                },
                exitTransition =  {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)

                    )
                },

            ){
               OnboardFirstScreen(
                   navigateBack = screen.navigateBack,
                   onNextClick = screen.navigateToSecondOnboardScreen
               )
            }
            composable(
                route = Constant.ONBOARD_SECOND_SCREEN,
             ){
                OnboardSecondScreen(
                    navigateBack = screen.navigateBack,
                    onNextClick = screen.navigateToThirdOnboardScreen
                )
            }
            composable(route = Constant.ONBOARD_THIRD_SCREEN){
                val viewModel : OnboardViewModel = hiltViewModel()

                OnboardThirdScreen(
                    navigateBack = screen.navigateBack,
                    onNextClick = {
                        viewModel::saveShowOnboarding.invoke()
                        screen.navigateToSignUp(
                            Constant.ONBOARD_FIRST_SCREEN,
                            true
                        )
                    }
                )
            }
            composable(
                route = Constant.SPLASH_SCREEN,
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(
                            durationMillis = 3000
                        )
                    )
                }
            ) {
                val viewModel: SplashScreenViewModel = hiltViewModel()
                val isAuthenticated  by viewModel.isAuthenticated.collectAsState()
                SplashScreen(){
                    if (isAuthenticated){
                        screen.navigateToHome()
                    }
                    else {
                        screen.navigateToLogin(
                            SPLASH_SCREEN, true
                        )
                    }
                }
            }

            composable(
                route = Constant.HOME_SCREEN_NAME,
            ) {
                val viewModel: HomeScreenModel = hiltViewModel()
                val state = viewModel.state.collectAsState().value
                val event = viewModel::handleChangeEvent
                HomeScreen(
                    navigateTo = navigateTo,
                    navigateToProfile = screen.navigateToProfile,
                    state = state,
                    event = event,
                    imageLoader = imageLoader,
                    snackBarHostState = snackBarHostState
                )
            }


            // Player Screen Composable

            composable(
                route = Constant.PLAYER_SCREEN_NAME,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(
                            durationMillis = Constant.NAVIGATION_ANIMATION_DURATION,
                            easing = LinearOutSlowInEasing

                        )
                    )

                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(
                            durationMillis = Constant.NAVIGATION_ANIMATION_DURATION,
                            easing = FastOutLinearInEasing
                        )
                    )
                }
            ) {
                val viewModel: PlayerScreenModel = hiltViewModel()
//                val state = viewModel.playerScreenState.collectAsState().value
//                val event = viewModel::musicAction
//                val playerScreenEvent = viewModel::handlePlayScreenAction
//                PlayerScreen(
//
//                    navigateBack = screen.navigateBack,
//                    downloadEvent = playerScreenEvent,
//                    snackBarHostState = snackBarHostState,
//                    imageLoader = imageLoader,
//                    onNavigate = {
//
//                    },
//                    route = Constant.PLAYER_SCREEN_NAME
//
//
//                )
            }



            composable(
                route = "userScreen/{uniqueId}",
                arguments = listOf(navArgument("uniqueId") {
                    type = NavType.StringType
                })
            ) {

            }

            composable(
                route = "categoryScreen/{uniqueId}",
                arguments = listOf(navArgument("uniqueId") {
                    type = NavType.StringType
                    nullable

                }
                )
            ) {

            }


            composable(
                route = ONBOARD_SCREEN
            ) {
//                OnBoardingScreen(
//                    navigateToLogin = {
//                        screen.navigateToLogin(ONBOARD_SCREEN, false)
//                    },
//                    navigateToSignUp = {
//                        screen.navigateToSignUp(ONBOARD_SCREEN)
//                    }
//
//
//                )
            }

            composable(
                route = LOGIN_SCREEN
            ) {
                val viewModel: SignInScreenModel = hiltViewModel()
                val event = viewModel::handleUiEvent
                val state = viewModel.state.collectAsState().value
                val activity = LocalContext.current as? Activity



                SignInScreen(
                    event = event,
                    state = state,
                    navigateToHome = screen.navigateToHome,
                    snackbarHostState = snackBarHostState,
                    navigateToSignUp = {
                        screen.navigateToSignUp(
                            SIGN_UP_SCREEN,
                            true
                        )
                    },
                    onBackClick =  {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        val shouldQuit = previousScreen != SIGN_UP_SCREEN
                        if (shouldQuit){
                            activity?.finish()
                        }
                        else {
                            //Sign UP, SignIn,
                            screen.navigateToSignUp(LOGIN_SCREEN, true)
                        }
                    }
                )
            }

            composable(
                route = SIGN_UP_SCREEN
            ) {
                val viewModel: SignUpScreenModel = hiltViewModel()
                val event = viewModel::handleUiEvent
                val state = viewModel.state.collectAsState().value
                SignUpScreen(
                    event = event,
                    state = state,
                    navigateToLogin = {
                        screen.navigateToLogin(
                            SIGN_UP_SCREEN,
                            false
                        )
                    },

                )
            }
            composable(
                route = PROFILE_SCREEN_NAME,
                arguments = listOf(navArgument("username") {
                    type = NavType.StringType
                })
            ) {
                val viewModel: ProfileViewModel = hiltViewModel()
                val state = viewModel.state.collectAsState().value
                ProfileScreen(
                    state = state,
                    event = viewModel::handleUiEvent,
                    navigateBack = screen.navigateBack,
                    updateProfile = screen.navigateToProfileUpdate,
                    imageLoader = imageLoader
                )

            }
            composable(route = MUSIC_SCREEN_NAME) {
                val viewModel: MusicViewModel = hiltViewModel()
                val state = viewModel.state.collectAsState().value
                val event = viewModel::handleUiEvent
                MusicScreen(
                    event = event,
                    state = state,
                    onNavigate = navigateTo,
                    navigateToProfile = screen.navigateToProfile,
                    navigateToPlayer = screen.navigateToPlayer,
                    currentRoute = MUSIC_SCREEN_NAME,
                    imageLoader = imageLoader

                )
            }

            composable(
                route = PROFILE_UPDATE_SCREEN
            ) {
                val viewModel: ProfileUpdateViewModel = hiltViewModel()
                val event = viewModel::handleUiEvent
                val state = viewModel.state.collectAsState().value
                ProfileUpdateScreen(
                    event = event,
                    state = state,
                    navigateToLogin = {
                        screen.navigateToLogin(ONBOARD_SCREEN, true)
                    },
                    navigateToOnboard = screen.navigateBack,
                    snackBarHostState = snackBarHostState,
                    imageLoader = imageLoader
                )
            }


            composable(
                route = UPLOAD_DASHBOARD_SCREEN
            ) {
                val viewModel: UploadDashboardViewModel = hiltViewModel()
                val state = viewModel.state.collectAsState().value
                UploadDashboardScreen(
                    state = state,
                    event = viewModel::handleEvent,
                    snackbarHostState = snackBarHostState
                )
            }


            composable(
                route = UPLOAD_FILE_SCREEN
            ) {
                val viewModel: UploadFileViewModel = hiltViewModel()
                val state = viewModel.state.collectAsState().value
               UploadFileScreen(
                    state = state,
                    navigateToProfile = {

                    },
                    navigateToSettings = {

                    },
                    event = viewModel::handleEvent,
                    navigateBack = screen.navigateBack,
                   onNavigate = {

                   },
                   snackBarHostState = snackBarHostState
                )
            }


        }
    }


}
