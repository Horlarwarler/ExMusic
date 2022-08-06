package com.example.talimlectures.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talimlectures.R
import com.example.talimlectures.ui.theme.IconColor
import com.example.talimlectures.ui.theme.MiniPlayerBackgroundColor
import com.example.talimlectures.ui.theme.TextColor

@Composable
fun DisplayDownloaddDialog(
    display:Boolean,
    onConfirmClicked:()->Unit,
    onCloseClicked:()->Unit,
    lectureName:String
) {
    if(display){

        AlertDialog(
            onDismissRequest = onCloseClicked,
            backgroundColor = MiniPlayerBackgroundColor,
            title = {
                Text(
                    text = "Download lecture $lectureName",
                    fontSize = 16.sp,
                    color = TextColor,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.dialog_description),
                    fontSize = 15.sp,
                    color = TextColor
                )
            },
            buttons = {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ignore",
                        color = MiniPlayerBackgroundColor,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .clickable {
                                onCloseClicked()
                            }
                            .clip(RoundedCornerShape(5.dp))
                            .background(IconColor)
                            .padding(horizontal = 17.dp, vertical = 7.dp)

                    )
                    Text(
                        text = "Download",
                        color = MiniPlayerBackgroundColor,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .clickable {
                                onConfirmClicked()
                            }
                            .clip(RoundedCornerShape(5.dp))
                            .background(IconColor)
                            .padding(horizontal = 17.dp, vertical = 7.dp)

                    )
                }

            }



        )
    }

}

@Preview
@Composable
private fun DialogBar(

){
    DisplayDownloaddDialog(
        true,
        {},
        {},
        "Waoh i finally get it"
    )
}
