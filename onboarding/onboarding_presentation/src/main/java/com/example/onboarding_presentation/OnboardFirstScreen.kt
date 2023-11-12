package com.example.onboarding_presentation

import androidx.compose.runtime.Composable
import com.example.presentation.R

@Composable
fun OnboardFirstScreen(
    onNextClick : () -> Unit,
    navigateBack: () -> Unit
){

    OnboardScreen(
        illustrationImage = R.drawable.guiter_man,
        titleResource = R.string.onboard_title_one,
        descriptionResource = R.string.onboard_description_one,
        index = 0,
        onNextClick = onNextClick,
        navigateBack = navigateBack
    )
}