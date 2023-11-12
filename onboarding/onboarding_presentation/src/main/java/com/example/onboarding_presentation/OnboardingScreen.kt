package com.example.onboarding_presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding
import com.crezent.design_system.theme.smallSpacer
import com.example.presentation.R

@Composable
fun OnboardScreen(
    modifier: Modifier = Modifier,
    illustrationImage:Int,
    titleResource:Int,
    descriptionResource: Int,
    index:Int = 0,
    onNextClick: () -> Unit,
    navigateBack: () -> Unit
){

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = smallPadding, vertical = mediumPadding)
    ) {

        Box(
            modifier = modifier
                .weight(0.5F)
            ,
            contentAlignment = Alignment.Center
            )
        {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id =illustrationImage ),
                contentDescription = "Onboard Image",
                contentScale = ContentScale.Fit
                )
        }
        Column(
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = mediumPadding, bottom = smallPadding),
                text = stringResource(id = titleResource),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 35.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = smallPadding),
                text = stringResource(id = descriptionResource ),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 24.sp
            )
            OnBoardButton(
                onNextClick =onNextClick,
                index = index
            )
        }


    }
    Box(
        modifier = Modifier
            .padding(top = mediumPadding)
            .fillMaxHeight(0.8f)
            .fillMaxWidth(0.5f)
            .background(Color.Transparent)
            .clickable {
                navigateBack()
            }
    )
}

@Composable
private fun OnBoardButton(
    onNextClick: () -> Unit,
    index: Int = 0
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding)
        ,
        verticalAlignment = Alignment.CenterVertically,

    ) {
       DotIcon(
           atIndex = index == 0,
       )
       DotIcon(
           atIndex = index == 1,
       )
       DotIcon(
           atIndex = index == 2,
       )
        Spacer(modifier = Modifier.weight(1f))

        NextButton(
            index = index,
            onNextClick = onNextClick
        )

    }
}
@Composable
private fun DotIcon(
    modifier: Modifier= Modifier,
    atIndex:Boolean = false
){
    val backgroundColor = if (atIndex) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primaryContainer
  Box(
      modifier = modifier
          .padding(end = smallSpacer)
          .size(10.dp)
          .background(backgroundColor, CircleShape)
  )
}

@Composable
private fun NextButton(
    modifier: Modifier= Modifier,
    index: Int  =0,
    onNextClick: () -> Unit
){
    val backgroundColor = when(index) {
        0 -> {
            MaterialTheme.colorScheme.tertiary
        }
        1 ->{
            MaterialTheme.colorScheme.primary
        }

        else -> {
            MaterialTheme.colorScheme.secondary
        }
    }
    Box(
        modifier = modifier
            .clickable { onNextClick() }
            .size(40.dp)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = ExMusicIcons.nextNavigationArrow,
            contentDescription = "Next",
            tint = MaterialTheme.colorScheme.onPrimary
            )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun Preview(){
    ExMusicTheme(darkTheme = false) {
        OnboardScreen(
            illustrationImage = R.drawable.old_man,
            titleResource = R.string.onboard_title_two,
            descriptionResource = R.string.onboard_description_two,
            index = 1,
            navigateBack = {},
            onNextClick = {}
        )
    }
}
