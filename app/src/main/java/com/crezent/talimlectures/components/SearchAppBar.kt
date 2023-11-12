package com.crezent.talimlectures.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.talimlectures.R

@Composable
fun SearchAppBar(
    closeIconVisible: Boolean,
    searchText:String,
    onValueChange:(String)->Unit,
    onSearchClick:(String)->Unit,
    onCloseActionClick:()->Unit,
    onDrawerStateButton:() -> Unit ={}
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                com.crezent.design_system.theme.mediumPadding,
                com.crezent.design_system.theme.largePadding
            ),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(
            modifier = Modifier.weight(2f),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = {
                onDrawerStateButton()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.burger_menu),
                    contentDescription = "Burger Menu",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
        TextField(
            modifier = Modifier
                .weight(8f)
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(com.crezent.design_system.theme.SearchBoxRadius)),
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
                backgroundColor = MaterialTheme.colors.primary,
                placeholderColor = MaterialTheme.colors.onPrimary,
                textColor = MaterialTheme.colors.onPrimary,
                cursorColor = MaterialTheme.colors.onPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.search),
//                    contentDescription = "Search Icon",
//                    tint = com.crezent.design_system.theme.darkOnPrimary
//                )

            },
            trailingIcon = {
                if(closeIconVisible){
                //    CloseIcon (onCloseButtonClicked = onCloseActionClick)
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



