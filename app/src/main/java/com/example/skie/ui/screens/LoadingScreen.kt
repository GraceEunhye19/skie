package com.example.skie.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skie.R

@Composable
fun LoadingScreen() {
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.05f),
        Color.White.copy(alpha = 0.15f),
        Color.White.copy(alpha = 0.05f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1B2A4A), Color(0xFF0D1B2A))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // city name placeholder
            ShimmerBox(brush = brush, modifier = Modifier.width(140.dp).height(20.dp))

            Spacer(modifier = Modifier.height(8.dp))

            // hero section
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // icon placeholder
                ShimmerBox(
                    brush = brush,
                    modifier = Modifier.width(100.dp).height(100.dp),
                    shape = RoundedCornerShape(50.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ShimmerBox(brush = brush, modifier = Modifier.width(120.dp).height(60.dp))
                    ShimmerBox(brush = brush, modifier = Modifier.width(80.dp).height(16.dp))
                    ShimmerBox(brush = brush, modifier = Modifier.width(100.dp).height(14.dp))
                    ShimmerBox(brush = brush, modifier = Modifier.width(90.dp).height(14.dp))
                }
            }

            // hourly card placeholder
            ShimmerBox(
                brush = brush,
                modifier = Modifier.height(90.dp),
                shape = RoundedCornerShape(24.dp)
            )

            // quote placeholder
            ShimmerBox(
                brush = brush,
                modifier = Modifier.height(50.dp).fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            )

            // forecast card placeholders
            repeat(5) {
                ShimmerBox(
                    brush = brush,
                    modifier = Modifier.height(64.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}

@Composable
fun ShimmerBox(
    brush: Brush,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp)
) {
    Box(
        modifier = Modifier
            .background(brush)
    )
}