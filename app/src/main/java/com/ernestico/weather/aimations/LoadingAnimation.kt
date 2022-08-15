package com.ernestico.weather.aimations

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ernestico.weather.ui.theme.DarkColors
import com.ernestico.weather.ui.theme.LightColors
import kotlinx.coroutines.delay


@Composable
fun LoadingAnimation() {

    var circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
    )

    circles.forEachIndexed { index, animatable ->  
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )

        }
    }

    val circlesValues = circles.map{ it.value }
    val distance = with(LocalDensity.current) { 20.dp.toPx() }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        circlesValues.forEachIndexed { index, it ->
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .graphicsLayer {
                        translationY = -it * distance
                    }
                    .background(
                        color = if (isSystemInDarkTheme()) DarkColors.onBackground else LightColors.onBackground,
                        shape = CircleShape,
                    )
            )

            if (index + 1 < circlesValues.size)
                Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
