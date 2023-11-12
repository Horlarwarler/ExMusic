package com.crezent.user_presentation.homeScreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.design_system.ExMusicIcons

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    title:String,
    onClick : ()-> Unit

){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextButton(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
            ) {
            Text(
                text = "See More",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                lineHeight = 12.sp
            )
            Icon(
                imageVector = ExMusicIcons.nextNavigationArrow,
                modifier = Modifier.size(20.dp),
                tint =  MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = "Expand")
        }

    }
}