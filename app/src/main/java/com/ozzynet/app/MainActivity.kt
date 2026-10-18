package com.ozzynet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Твои API ключи Telegram
        val apiId = 24673165
        val apiHash = "711c4e4d79051755a353de509e4b643f"

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF181818)
                ) {
                    OzzynetMainScreen {
                        AuthScreen(apiId = apiId, apiHash = apiHash)
                    }
                }
            }
        }
    }
}

// Константы бренда
val VkBlue = Color(0xFF45688E)
val MyTelegramId = 123456789L // Замени на свой реальный Telegram ID для бейджа Creator

@Composable
fun OzzynetMainScreen(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()

        // Ватермарка ozzya co в углу
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

// Экран авторизации в клиенте ozzynet
@Composable
fun AuthScreen(apiId: Int, apiHash: String) {
    var phoneNumber by remember { mutableStateOf("") }
    var authState by remember { mutableStateOf("Ожидание ввода номера телефона...") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Шапка профиля / Логотип
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.5.dp, VkBlue, RoundedCornerShape(4.dp))
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text("oz", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "ozzynet client", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    CreatorBadge()
                }
                Text(text = "API ID: $apiId", color = Color.Gray, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Статус подключения TDLib
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Статус TDLib:", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = authState, color = Color(0xFF00E5FF), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Поле ввода телефона
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Номер телефона (+...)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VkBlue,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = VkBlue,
                cursorColor = VkBlue
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка отправки
        Button(
            onClick = {
                if (phoneNumber.isNotBlank()) {
                    authState = "Отправка номера $phoneNumber в TDLib..."
                    // Здесь будет инициализация клиента TDLib с apiId и apiHash
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = VkBlue),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Далее", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Эксклюзивный анимированный бейдж Creator
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
