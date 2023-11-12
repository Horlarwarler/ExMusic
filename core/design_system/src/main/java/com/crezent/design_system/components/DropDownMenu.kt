package com.crezent.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun <T>DropDownMenu(
    modifier: Modifier = Modifier,
    elements: List<T>,
    onClick: (value: Int) -> Unit,
    selectedIndex:Int = -1,
    background: Color
){
   val listState = rememberLazyListState()
    var expanded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true,){
        if (selectedIndex != -1){
            listState.scrollToItem(selectedIndex)
        }
    }
    if (expanded){
        Surface(
            modifier = modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Dialog(onDismissRequest = {
                expanded = false
            }) {
                LazyColumn(
                    state = listState
                ){
                    itemsIndexed(elements){
                        index: Int, item: T ->

                        DropDownMenuItem(
                            text = item.toString(),
                            background = background,
                            onClick = {
                                onClick(index)
                            }
                            )
                    }
                }
            }
        }
    }


}

@Composable
fun DropDownMenuItem(
    text:String,
    onClick :() -> Unit,
    background:Color
){
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .clip(RoundedCornerShape(5.dp))
            .background(background)
            .fillMaxWidth(),
    ){
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}