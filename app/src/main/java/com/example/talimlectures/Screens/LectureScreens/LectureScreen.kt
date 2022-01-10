package com.example.talimlectures.Screens.LectureScreens

import android.graphics.Color.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talimlectures.R
import com.example.talimlectures.ui.theme.BackgroundColor
import com.example.talimlectures.ui.theme.IconColor
import com.example.talimlectures.ui.theme.TalimLecturesTheme
import com.example.talimlectures.ui.theme.TitleColor

@Composable
fun LectureScreen(){
    Scaffold(
       modifier =Modifier.fillMaxSize(),

        topBar = {
            LecturesDefaultAppBar()
        },
        content = {

        }
    )
}

@Composable
fun LecturesDefaultAppBar(

){
    TopAppBar(
        elevation = 0.dp,
        navigationIcon = { NavigationIcon(onBackButtonClicked = {})},
        actions = { DefaultAppBarActions(
            onSearchButtonClicked = { /*TODO*/ },
            onMoreButtonClicked = {}
        )

        },
        title = {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                text = "Lectures",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,

                color = MaterialTheme.colors.TitleColor)
        },
        backgroundColor = MaterialTheme.colors.BackgroundColor

    )
}

@Composable
fun NavigationIcon(
    onBackButtonClicked:()->Unit
){
    IconButton(onClick = { onBackButtonClicked() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
            contentDescription = "Back Button",
            tint = MaterialTheme.colors.IconColor
        )
    }
}
@Composable
fun DefaultAppBarActions(
    onSearchButtonClicked: () -> Unit,
    onMoreButtonClicked: () -> Unit
){
    SearchIcon (onSearchButtonClicked =onSearchButtonClicked )
    MoreIcon(onMoreButtonClicked =  onMoreButtonClicked)
}
@Composable
fun SearchIcon( onSearchButtonClicked:()->Unit
){
    IconButton(onClick = { onSearchButtonClicked() }) {
        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search Button",
            tint = MaterialTheme.colors.IconColor

        )
    }
}
@Composable
fun MoreIcon( onMoreButtonClicked:()->Unit
){
    IconButton(onClick = { onMoreButtonClicked() }) {
        Icon(
            painter = painterResource(id = R.drawable.more),
            contentDescription = "More Button",
            tint = MaterialTheme.colors.IconColor
        )
    }
}
@Composable
fun DefaultBar(){

}

@Preview
@Composable
private fun PreveiwTab(){
    TalimLecturesTheme(darkTheme = false) {
        LectureScreen()
    }

}
