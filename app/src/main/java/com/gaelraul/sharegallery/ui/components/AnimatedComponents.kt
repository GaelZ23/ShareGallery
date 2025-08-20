package com.gaelraul.sharegallery.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaelraul.sharegallery.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AnimatedSuccessCard(
    message: String,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        delay(3000)
        visible = false
        delay(300)
        onDismiss()
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300, easing = EaseInBack)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = successColor.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✅",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = message,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AnimatedLoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var buttonScale by remember { mutableStateOf(1f) }
    
    LaunchedEffect(isLoading) {
        if (isLoading) {
            buttonScale = 0.95f
        } else {
            buttonScale = 1f
        }
    }
    
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier
            .graphicsLayer {
                scaleX = buttonScale
                scaleY = buttonScale
            },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Procesando...",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun AnimatedGradientCard(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            gradientStart.copy(alpha = 0.1f + animatedProgress * 0.1f),
                            gradientEnd.copy(alpha = 0.1f + animatedProgress * 0.1f)
                        )
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
fun AnimatedPulseIcon(
    icon: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Text(
        text = icon,
        fontSize = 24.sp,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    )
}

@Composable
fun AnimatedFadeInContent(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(500, easing = EaseOut)
        ) + expandVertically(
            animationSpec = tween(500, easing = EaseOut)
        ),
        exit = fadeOut(
            animationSpec = tween(300, easing = EaseIn)
        ) + shrinkVertically(
            animationSpec = tween(300, easing = EaseIn)
        )
    ) {
        content()
    }
} 