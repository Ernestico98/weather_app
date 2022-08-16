package com.ernestico.weather.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ernestico.weather.MainViewModel
import com.ernestico.weather.R

@Composable
fun AboutScreen(
    mainViewModel: MainViewModel,
    navController: NavController
) {
    mainViewModel.setTopBarText("About")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.weather_logo),
            contentDescription = "About Image",
            modifier = Modifier
                .size(300.dp)
                .scale(1.3f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            elevation = 10.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "This is the first version of Weather app",

                )
                Spacer(modifier = Modifier.height(15.dp))

                Row {
                    Text(text = "Built for you with ")
                    Icon(Icons.Filled.Favorite, contentDescription = "Heart icon")
                }

                Spacer(modifier = Modifier.height(15.dp))

                val mUriHandler = LocalUriHandler.current
                Row {
                    Text(text = "Project Repository")
                    Image(
                        painter = painterResource(id = R.drawable.github),
                        contentDescription = "Github icon",
                        modifier = Modifier.size(26.dp).padding(5.dp)
                            .clickable {
                                mUriHandler.openUri("https://github.com/Ernestico98/weather_app")
                            }
                            .size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row {
                    Text(text = "Check out my")
                    Image(
                        painter = painterResource(id = R.drawable.github),
                        contentDescription = "Github icon",
                        modifier = Modifier.size(26.dp).padding(5.dp)
                    )
                    HyperLinkText(text = "Ernestico98", urlValue = "https://github.com/Ernestico98")
                    Text(text = " for cool projects")
                }
            }
        }
    }
}

@Composable
fun HyperLinkText(
    text: String,
    urlValue: String,
) {
    val mAnnotatedLinkString = buildAnnotatedString {
        val mStartIndex = 0
        val mEndIndex = text.length

        append(text)
        addStyle(
            style = SpanStyle(
                color = Color(0xFF9CCC65),
                textDecoration = TextDecoration.Underline
            ), start = mStartIndex, end = mEndIndex
        )
        addStringAnnotation(
            tag = "URL",
            annotation = urlValue,
            start = mStartIndex,
            end = mEndIndex
        )

    }

    val mUriHandler = LocalUriHandler.current
    ClickableText(
        text = mAnnotatedLinkString,
        onClick = {
            mAnnotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    mUriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}
