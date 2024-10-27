package com.alexnemyr.testtaska.ui.element

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.alexnemyr.testtaska.R

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    url: String,
) {
    AsyncImage(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        model = url,
        placeholder = painterResource(id = R.drawable.avatar_circle_24),
        contentDescription = null,
    )
}