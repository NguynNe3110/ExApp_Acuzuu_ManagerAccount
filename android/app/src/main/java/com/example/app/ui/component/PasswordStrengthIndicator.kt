package com.example.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app.core.ui.*
import com.example.app.domain.model.PasswordLevel

// ─────────────────────────────────────────────────────────────
// PasswordStrengthIndicator.kt
//
// Thanh chỉ báo sức mạnh mật khẩu — nhìn như một viên nang
// dọc gồm 4 đoạn màu từ dưới lên:
//   Đỏ → Cam → Vàng → Xanh
// Mức càng cao → càng nhiều đoạn có màu (từ dưới lên)
// ─────────────────────────────────────────────────────────────

@Composable
fun PasswordStrengthIndicator(
    level: PasswordLevel,
    modifier: Modifier = Modifier
) {
    // 4 đoạn, đoạn dưới = mức thấp, đoạn trên = mức cao
    val segments = listOf(
        StrengthRed,
        StrengthOrange,
        StrengthYellow,
        StrengthGreen
    )
    // Số đoạn được "fill" = level.value (1→1 đoạn đỏ, 4→ cả 4)
    val filledCount = level.value.coerceIn(1, 4)

    Column(
        modifier = modifier
            .width(8.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp)),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        // Vẽ từ trên xuống (chỉ số 3..0)
        for (i in 3 downTo 0) {
            val isFilled = i < filledCount
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(if (isFilled) segments[i] else Color(0xFFE0E0E0))
            )
        }
    }
}
