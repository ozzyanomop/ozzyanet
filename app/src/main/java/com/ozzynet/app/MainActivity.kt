package com.ozzynet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF181818)
                ) {
                    OzzynetMainScreen(config = OzzynetThemeConfig()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                VkAvatar()
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    UserTitleWithBadge(userId = 123456789L, userName = "ozzya")
                                    Text(text = "онлайн", color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            GlassMessageBubble(
                                text = "Привет! Это кастомный клиент ozzynet с эффектом Liquid Glass и партиклами.",
                                time = "20:10",
                                isDeletedLocally = true
                            )
                        }
                    }
                }
            }
        }
    }
}

val VkBlue = Color(0xFF45688E)

data class OzzynetThemeConfig(
    val primaryColor: Color = VkBlue,
    val blurRadius: Float = 16f,
    val isLiquidGlassEnabled: Boolean = true,
    val particleMode: ParticleMode = ParticleMode.SNOW,
    val hideMessageTime: Boolean = false
)

enum class ParticleMode { OFF, RAIN, SNOW }

val LocalOzzynetConfig = compositionLocalOf { OzzynetThemeConfig() }

@Composable
fun OzzynetMainScreen(
    config: OzzynetThemeConfig,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalOzzynetConfig provides config) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()

            if (config.particleMode != ParticleMode.OFF) {
                ParticleOverlay(mode = config.particleMode)
            }

            Text(
                text = "ozzya co",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .alpha(0.35f)
                    .zIndex(100f)
            )
        }
    }
}

@Composable
fun ParticleOverlay(mode: ParticleMode) {
    var particles by remember {
        mutableStateOf(List(40) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = Random.nextFloat() * 0.005f + 0.001f,
                size = Random.nextFloat() * 5f + 2f
            )
        })
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                particles = particles.map { p ->
                    val newY = if (p.y > 1f) 0f else p.y + p.speed
                    p.copy(y = newY)
                }
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val color = if (mode == ParticleMode.RAIN) Color(0x8080A0FF) else Color.White
            drawCircle(
                color = color,
                radius = particle.size,
                center = Offset(particle.x * size.width, particle.y * size.height)
            )
        }
    }
}

data class Particle(val x: Float, val y: Float, val speed: Float, val size: Float)

@Composable
fun UserTitleWithBadge(userId: Long, userName: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        if (userId == 123456789L) {
            Spacer(modifier = Modifier.width(6.dp))
            CreatorBadge()
        }
    }
}

@Composable
fun CreatorBadge() {
    val infiniteTransition = rememberInfiniteTransition(label = "creator_anim")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "gradient"
    )

    val animatedBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFFF007F), Color(0xFFFFD700), Color(0xFF00E5FF), Color(0xFFFF007F)),
        start = Offset(offset, offset),
        end = Offset(offset + 300f, offset + 300f)
    )

    Box(
        modifier = Modifier
            .border(1.dp, animatedBrush, RoundedCornerShape(4.dp))
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "👑 Creator",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun VkAvatar() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.5.dp, VkBlue, RoundedCornerShape(4.dp))
            .background(Color.DarkGray)
    )
}

@Composable
fun GlassMessageBubble(
    text: String,
    time: String,
    isDeletedLocally: Boolean = false,
    config: OzzynetThemeConfig = LocalOzzynetConfig.current
) {
    val glassShape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(if (config.isLiquidGlassEnabled) Modifier.blur(config.blurRadius.dp) else Modifier)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = glassShape
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.5f), Color.Transparent)
                ),
                shape = glassShape
            )
            .padding(16.dp)
    ) {
        Column {
            Text(text = text, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                if (!config.hideMessageTime) {
                    Text(text = time, color = Color.LightGray, fontSize = 10.sp)
                }
                if (isDeletedLocally) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "[Удалено]", color = Color(0xFFFF5252), fontSize = 10.sp)
                }
            }
        }
    }
}
