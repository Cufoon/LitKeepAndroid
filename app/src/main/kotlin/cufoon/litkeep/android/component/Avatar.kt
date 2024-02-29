package cufoon.litkeep.android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Avatar(model: Any?, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .border(1.dp, Color(0x30A0A0A0), CircleShape)
                .clip(CircleShape)
                .alpha(0.5f)
                .blur(10.dp)
                .size(125.dp)
                .background(Color.White)
        ) {}
        AsyncImage(
            model = model, contentDescription = "头像",
            Modifier
                .shadow(
                    25.dp, ambientColor = Color(0x20000000), spotColor = Color(0x10000000)
                )
                .clip(CircleShape)
                .size(120.dp), contentScale = ContentScale.Crop
        )
    }
}
