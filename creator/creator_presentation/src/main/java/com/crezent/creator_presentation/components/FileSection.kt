package com.crezent.creator_presentation.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.creator_presentation.R
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.smallPadding

@Composable
fun FileSection(

    enable: Boolean = true,
    choseFile: () -> Unit = {},
    onButtonClick : () -> Unit,
    filePreview : @Composable () -> Unit = {},
    buttonText:String? = "Preview",
    selectedByteArray: ByteArray? = null


    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(smallPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val borderColor = MaterialTheme.colorScheme.primaryContainer

        Box(
            modifier = Modifier
                .height(206.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .drawBehind {
                   if (selectedByteArray == null){
                       val stroke = Stroke(
                           width = 8f,
                           pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 40f, 40f), 10f)
                       )
                       drawRoundRect(
                           color = borderColor,
                           cornerRadius = CornerRadius(20f),
                           style = stroke
                       )
                   }
                }
                .clickable(enable) {
                    choseFile()
                },

            contentAlignment = Alignment.Center
        ) {
            if (selectedByteArray != null){
                val imageBitmap = BitmapFactory.decodeByteArray(selectedByteArray, 0, selectedByteArray.size).asImageBitmap()
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                    bitmap = imageBitmap,
                    contentDescription =  "Selected Thumbnail"
                )
            }
            else {
                Icon(
                    modifier = Modifier
                        .rotate(30f)
                        .size(width = 78.dp, height = 70.dp),
                    painter = painterResource(
                        id = R.drawable.baseline_attach_file_24
                    ),
                    contentDescription = "Attach File",
                    tint = MaterialTheme.colorScheme.onBackground,
                )

            }

        }
        if (buttonText != null){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                filePreview()

                TextButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onButtonClick
                ) {
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun Preview(){
    ExMusicTheme(
        darkTheme = false
    ) {
        FileSection(onButtonClick = { /*TODO*/ })
    }
}