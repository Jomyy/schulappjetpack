package com.jomy.jetpacklernentutorial.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController



@Composable
fun JetpacklernentutorialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val LightColorPalette = dynamicLightColorScheme(LocalContext.current)
    val DarkColorPalette = dynamicDarkColorScheme(LocalContext.current)
    val systemUiController = rememberSystemUiController()

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )



    if(!isSystemInDarkTheme()){
        systemUiController.setStatusBarColor(
            color = LightColorPalette.surface
        )

    }else{
        systemUiController.setStatusBarColor(
            color = DarkColorPalette.surface
        )

    }


}