package com.latihan.mobilelanjut.checkout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.latihan.mobilelanjut.checkout.data.OrderRepositoryImpl
import com.latihan.mobilelanjut.checkout.domain.OrderItem
import com.latihan.mobilelanjut.checkout.domain.ProcessCheckoutUseCase

@Composable
fun CheckoutScreen() {
    // 1. KUSTOMISASI WAJIB: Ubah warna tema (contoh: Hijau Emerald 0xFF059669)
    val accent = Color(0xFF059669)
    val useCase = remember { ProcessCheckoutUseCase(OrderRepositoryImpl()) }

    // 2. KUSTOMISASI WAJIB: Minimal 3 item produk sesuai tema usaha pilihan Anda
    val items = remember {
        listOf(
            OrderItem("1", "Kopi Susu Gula Aren", 22_000L, 2),
            OrderItem("2", "Croissant Butter", 18_000L, 1),
            OrderItem("3", "Matcha Latte", 25_000L, 1)
        )
    }

    var promoInput by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf(useCase(items, null)) }
    var promoStatus by remember { mutableStateOf("") } // Untuk syarat kustomisasi ke-4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 3. KUSTOMISASI WAJIB: Ganti dengan Nama Lengkap dan Kelas Anda
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = accent)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                Text("Nama Lengkap Anda - Kelas RPL-6A", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Simulasi Checkout Terstruktur", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
            }
        }

        items.forEach { item ->
            ElevatedCard(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.name} (x${item.qty})", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Text("Rp ${item.price * item.qty}", color = accent, fontWeight = FontWeight.Bold)
                }
            }
        }

        OutlinedTextField(
            value = promoInput,
            onValueChange = { promoInput = it },
            label = { Text("Kode Promo (coba: PROMO20)") }, // Sesuaikan dengan kode promo Anda
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        // 4. KUSTOMISASI WAJIB: Teks keterangan status promo
        if (promoStatus.isNotEmpty()) {
            Text(
                text = promoStatus,
                fontSize = 12.sp,
                color = if (summary.discount > 0) Color(0xFF059669) else Color(0xFFDC2626),
                fontWeight = FontWeight.Medium
            )
        }

        Button(
            onClick = {
                summary = useCase(items, promoInput)
                promoStatus = when {
                    promoInput.isBlank() -> ""
                    summary.discount > 0 -> "Kode promo valid! Diskon berhasil diterapkan."
                    else -> "Kode promo tidak berlaku."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = accent),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hitung Total Pembayaran")
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                Text("Subtotal: Rp ${summary.subtotal}")
                Text("Diskon  : Rp ${summary.discount}", color = Color(0xFF0F766E))
                Text("Total   : Rp ${summary.totalPayable}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}