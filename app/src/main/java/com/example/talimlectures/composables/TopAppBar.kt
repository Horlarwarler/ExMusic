package com.example.talimlectures.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talimlectures.R
import com.example.talimlectures.ui.theme.*
import com.example.talimlectures.util.SearchAction


@Composable
fun LectureAppBar(
    searchBarState:SearchAction,
    closeIconVisible: Boolean,
    searchText:String,
    onValueChange:(String)->Unit,
    onSearchClick:(String)->Unit,
    onCloseActionClick:()->Unit,
    onSearchButtonClicked: () -> Unit
){
    if(searchBarState == SearchAction.OPEN){
        SearchTopAppBar(closeIconVisible =closeIconVisible ,
            searchText =searchText ,
            onValueChange =onValueChange ,
            onSearchClick =onSearchClick,
            onCloseActionClick = onCloseActionClick)
    }
    else{
        DefaultAppBar (onSearchButtonClicked = onSearchButtonClicked)
    }
}
@Composable
fun SearchTopAppBar(
    closeIconVisible: Boolean,
    searchText:String,
    onValueChange:(String)->Unit,
    onSearchClick:(String)->Unit,
    onCloseActionClick:()->Unit
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding, largePadding),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(
            modifier = Modifier.weight(2f),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.burger_menu),
                    contentDescription = "Burger Menu",
                    tint = IconColor
                )
            }
        }
        TextField(
            modifier = Modifier
                .weight(8f)
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(SearchBoxRadius)),
            value = searchText,
            onValueChange = { onValueChange(it) },
            placeholder = {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Search Music",
                    modifier = Modifier.fillMaxSize(),

                    fontSize = 13.sp
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = SearchBackgroundColor,
                placeholderColor = SearchTextColor,
                textColor = SearchTextColor,
                cursorColor = SearchTextColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Icon",
                    tint = SearchTextColor)

            },
            trailingIcon = {
                           if(closeIconVisible){
                               CloseIcon (onCloseButtonClicked = onCloseActionClick)
                           }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {onSearchClick(searchText)}
            )


        )
    }
}



@Composable
fun PlayingAppBar(
    onBackButtonClicked: () -> Unit
){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = mediumPadding, end = mediumPadding, top = mediumPadding)
    ) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
                text = "Now Playing",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,

                color = TitleColor)
           NavigationIcon(
               modifier = Modifier.align(Alignment.CenterStart),
               onBackButtonClicked = {onBackButtonClicked()})

    }
}
@Composable
fun DefaultAppBar(
onSearchButtonClicked: () -> Unit
){
    TopAppBar(
        modifier = Modifier.padding(start = mediumPadding, end = mediumPadding, top = mediumPadding),
        elevation = 0.dp,
        navigationIcon = { BurgerIcon(onBurgerButtonClicked = {}) },
        actions = { SearchIcon(onSearchButtonClicked)},
        title = {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                text = "Lectures",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,

                color = TitleColor)
        },
        backgroundColor = BackgroundColor

    )
}

@Composable
fun CloseIcon(
    onCloseButtonClicked:()->Unit
){
    IconButton(onClick = { onCloseButtonClicked() }) {
        Icon(
            painter = painterResource(id = R.drawable.close),
            contentDescription = "Close Icon",
            modifier = Modifier.size(25.dp),
            tint = SearchTextColor)
    }
}

@Composable
fun SearchIcon( onSearchButtonClicked:()->Unit
){
    IconButton(onClick = { onSearchButtonClicked() }) {
        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search Icon",
            modifier = Modifier.size(25.dp),
            tint = SearchTextColor)
    }
}
@Composable
fun BurgerIcon(
    onBurgerButtonClicked:()->Unit
){
    IconButton(onClick = { onBurgerButtonClicked() }) {
        Icon(
            painter = painterResource(id = R.drawable.burger_menu),
            contentDescription = "Drawer Navigation",
            tint = IconColor
        )
    }
}
@Composable
fun NavigationIcon(
    onBackButtonClicked:()->Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = { onBackButtonClicked() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
            contentDescription = "Drawer Navigation",
            tint = IconColor
        )
    }
}

@Preview
@Composable
fun previewAppBar(){
  PlayingAppBar {

  }
}
