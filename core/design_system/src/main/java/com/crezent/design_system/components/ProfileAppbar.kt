package com.crezent.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.crezent.design_system.theme.smallSpacer


@Composable
fun ProfileAppbar(
    modifier: Modifier =Modifier,
    onBackButtonPressed: (() -> Unit)? = null,
    onProfileSelected: () -> Unit,
    onSettingsPressed:() -> Unit,
    text:String,
    profileUrl:String? = null
){
  Row(
      modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colors.background)
      .padding(vertical = smallSpacer, horizontal = smallSpacer),
      verticalAlignment = Alignment.CenterVertically
  ) {
      if (onBackButtonPressed != null){
          IconButton(
              modifier = Modifier.padding(end = 1.dp),
              onClick = onBackButtonPressed
          ) {
//              Icon(
//                  painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
//                  contentDescription ="Back Button",
//                  tint = MaterialTheme.colors.onBackground
//              )
          }
      }
      Text(
          modifier = Modifier.weight(1f),
          text = text,
          style = MaterialTheme.typography.h3,
          color = MaterialTheme.colors.onBackground
      )
//      val painter : Painter = if (profileUrl == null) painterResource(
//          id = R.drawable.logo
//      ) else{
//          rememberAsyncImagePainter(model = profileUrl)
//      }
      val painter = rememberAsyncImagePainter(model = profileUrl)

          Image(
          contentScale = ContentScale.Crop,
          modifier = Modifier
              .padding(horizontal = 3.dp)
              .size(50.dp)
              .clip(CircleShape),
          painter =painter,
          contentDescription = "Thumbnail",
      )
      IconButton(
          onClick = onSettingsPressed
      ) {
//          Icon(
//              painter = painterResource(id = R.drawable.settings_round),
//              contentDescription ="Settings Button",
//              tint = MaterialTheme.colors.onBackground
//          )
      }
  }

}
@Preview()
@Composable

private fun Preview(){
//    ExMusicTheme (darkTheme = true){
//        ProfileAppbar(
//            onProfileSelected = { /*TODO*/ },
//            onSettingsPressed = { /*TODO*/ },
//            text = "Upload" ,
//            onBackButtonPressed =  {}
//            )
//    }
}