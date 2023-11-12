package com.example.onboarding_presentation

import androidx.compose.runtime.Composable
import com.example.presentation.R

@Composable
fun OnboardSecondScreen(
    onNextClick : () -> Unit,
    navigateBack: () -> Unit

){

    OnboardScreen(
        illustrationImage = R.drawable.playing,
        titleResource = R.string.onboard_title_two,
        descriptionResource = R.string.onboard_description_two,
        index = 1,
        onNextClick = onNextClick,
        navigateBack = navigateBack
    )
}