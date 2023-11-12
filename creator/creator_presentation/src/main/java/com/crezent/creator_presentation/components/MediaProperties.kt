package com.crezent.creator_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.common.util.MediaAttribute
import com.crezent.common.util.currentTime
import com.crezent.design_system.theme.ExMusicTheme

@Composable
fun MediaProperties(
    mediaAttribute: MediaAttribute
){
    Column {
        if (mediaAttribute.name != null){
            Text(
                text = mediaAttribute.name!!,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color =MaterialTheme.colorScheme.onBackground
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mediaAttribute.size != null){
                val sizeInString = mediaAttribute.size!!.toString()
                val dotIndex = sizeInString.indexOf(".")
                val roundUp = sizeInString.substring(0, dotIndex+3)
                Text(
                    text = "$roundUp mb",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color =MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Divider(
                modifier = Modifier
                    .height(20.dp)
                    .width(3.dp)
                ,
                thickness = 3.dp
            )

            Spacer(modifier = Modifier.width(10.dp))
            if (mediaAttribute.duration != null){
                val durationString = mediaAttribute.duration!!.toLong().currentTime()
//                if (durationString[0] == '0'){
//
//                }
                Text(
                    text = durationString,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color =MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(){
    ExMusicTheme(darkTheme = false) {
        val mediaAttribute = MediaAttribute(
            name = "Filename",
            size = 1.2,
            duration = 22.4
        )
        MediaProperties(mediaAttribute = mediaAttribute)
    }
}