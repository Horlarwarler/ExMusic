package com.crezent.design_system.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () ->Unit,
    onConfirm: () ->Unit,
    title: String,
    description:String,
    positiveText:String = "Okay",
    cancelText:String = "Cancel",


){
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Text(
                modifier = Modifier
                    .clickable {
                        onConfirm()
                    }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = positiveText,
                fontSize = 15.sp
            )
        },
        dismissButton = {
            Text(
                modifier = Modifier
                    .clickable {
                        onDismissRequest()
                    }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = cancelText,
                fontSize = 15.sp
            )
        },
        title = {
            Text(

                text = title,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = description,
                fontSize = 15.sp
            )
        },
    )
}