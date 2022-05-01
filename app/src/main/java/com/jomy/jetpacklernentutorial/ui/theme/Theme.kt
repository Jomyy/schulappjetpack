package com.jomy.jetpacklernentutorial.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils
import com.google.accompanist.systemuicontroller.rememberSystemUiController


fun supportsDynamic(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun JetpacklernentutorialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val lightThemeColors = lightColorScheme(

    )
    val darkThemeColors = darkColorScheme(

    )

    val systemUiController = rememberSystemUiController()


    val colors = if (supportsDynamic()) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) darkThemeColors else lightThemeColors
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )



    if (!isSystemInDarkTheme()) {
        systemUiController.setStatusBarColor(
            color = Color(
                ColorUtils.blendARGB(
                    colors.surface.toArgb(),
                    colors.primary.toArgb(),
                    0.09f
                )
            )
        )
        systemUiController.setNavigationBarColor(
            color = Color(
                ColorUtils.blendARGB(
                    colors.surface.toArgb(),
                    colors.primary.toArgb(),
                    0.085f
                )
            )
        )
    } else {
        systemUiController.setStatusBarColor(
            color = Color(
                ColorUtils.blendARGB(
                    colors.surface.toArgb(),
                    colors.primary.toArgb(),
                    0.09f
                )
            )
        )
        systemUiController.setNavigationBarColor(
            color = Color(
                ColorUtils.blendARGB(
                    colors.surface.toArgb(),
                    colors.primary.toArgb(),
                    0.085f
                )
            )

        )

    }


}