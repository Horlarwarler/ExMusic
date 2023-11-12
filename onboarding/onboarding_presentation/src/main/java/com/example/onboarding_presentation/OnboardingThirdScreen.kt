package com.example.onboarding_presentation

import androidx.compose.runtime.Composable
import com.example.presentation.R

@Composable
fun OnboardThirdScreen(
    onNextClick : () -> Unit,
    navigateBack: () -> Unit

){

    OnboardScreen(
        illustrationImage = R.drawable.old_man,
        titleResource = R.string.onboard_title_three,
        descriptionResource = R.string.onboard_description_three,
        index = 2,
        onNextClick = onNextClick,
        navigateBack = navigateBack
    )
}