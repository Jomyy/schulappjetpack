package com.jomy.jetpacklernentutorial.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController


fun supportsDynamic() : Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
@Composable
fun JetpacklernentutorialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val LightThemeColors = lightColorScheme(

    )
    val DarkThemeColors = darkColorScheme(

    )

    val systemUiController = rememberSystemUiController()


    val colors = if (supportsDynamic()) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkThemeColors else LightThemeColors
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )



    if(!isSystemInDarkTheme()){
        systemUiController.setStatusBarColor(
            color = colors.surface
        )

    }else{
        systemUiController.setStatusBarColor(
            color = colors.surface
        )

    }


}