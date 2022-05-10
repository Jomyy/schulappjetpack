package com.jomy.schulapp.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.jomy.schulapp.util.SettingsUtil


@Composable
fun PreferencesSwitch(
    scale: Float = 1f,
    width: Dp = 56.dp,
    height: Dp = 28.dp,
    checkedTrackColor: Color = Color(
        ColorUtils.blendARGB(

            MaterialTheme.colorScheme.tertiaryContainer.toArgb(),
            MaterialTheme.colorScheme.surfaceTint.toArgb(),
            0.43f
        )
    ),
    uncheckedTrackColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    checkedPointColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    uncheckedPointColor: Color = MaterialTheme.colorScheme.outline,
    gapBetweenThumbAndTrackEdge: Dp = 4.dp,
    title: String,
    icon: ImageVector,
    key: String,
    onChange: (newKlasse: Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    val switchON = remember {
        mutableStateOf(
            SettingsUtil.readSetting(key, context = context).toBoolean()
        ) // Initially the switch is ON
    }

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )
    Row(modifier = Modifier
        .height(65.dp)
        .clickable {
            switchON.value = !switchON.value

            SettingsUtil.writeSetting(key, switchON.value.toString(), context = context)
            onChange(switchON.value)
        }
        .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon, title, modifier = Modifier
                .padding(end = 20.dp)
                .size(30.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title, style = TextStyle(
                    fontSize = 20.sp
                ), modifier = Modifier.wrapContentHeight()
            )
        }

        Canvas(
            modifier = Modifier
                .size(width = width, height = height)
                .scale(scale = scale)

        ) {
            // Track
            drawRoundRect(
                color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
                cornerRadius = CornerRadius(x = 15.dp.toPx(), y = 15.dp.toPx()),

                )

            // Thumb
            drawCircle(
                color = if (switchON.value) checkedPointColor else uncheckedPointColor,
                radius = thumbRadius.toPx(),
                center = Offset(
                    x = animatePosition.value,
                    y = size.height / 2
                )
            )
        }


    }


}