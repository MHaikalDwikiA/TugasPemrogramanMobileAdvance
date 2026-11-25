package com.application.mohamadhaikaldwikiakbar

import com.latihan.mobilelanjut.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DashboardP1Screen()
            }
        }
    }
}

@Composable
fun DashboardP1Screen() {
    // 2. KUSTOMISASI WAJIB: Palet Warna Hijau Emerald
    val emeraldPrimary = Color(0xFF059669)
    val emeraldSoftBg = Color(0xFFECFDF5)

    // 1. KUSTOMISASI WAJIB: Identitas Mahasiswa dari strings.xml
    val nama = stringResource(R.string.student_name)
    val kelas = stringResource(R.string.student_class)

    var klikCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Kartu Identitas Mahasiswa
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = emeraldPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nama.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = nama,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = kelas,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Kartu Info Proyek
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Informasi Proyek Latihan P1",
                    fontWeight = FontWeight.Bold,
                    color = emeraldPrimary
                )
                Text(
                    text = "Package: com.latihan.mobilelanjut",
                    fontSize = 13.sp
                )
                Text(
                    text = "Status Tombol Uji: Diklik $klikCount kali",
                    fontSize = 13.sp
                )
            }
        }

        // Tombol Uji Klik & Logcat
        Button(
            onClick = {
                klikCount++
                Log.d("LatihanP1", "Tombol diklik ke-$klikCount oleh $nama")
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = emeraldPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Uji Klik & Catat ke Logcat ($klikCount)")
        }

        // 3. KUSTOMISASI WAJIB: Kartu Target Belajar di bawah tombol
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = emeraldSoftBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Target Belajar & Ide Aplikasi",
                    fontWeight = FontWeight.Bold,
                    color = emeraldPrimary,
                    fontSize = 15.sp
                )
                Text(
                    text = "Ide Aplikasi: Smart IoT & Cloud Monitoring",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF064E3B)
                )
                Text(
                    text = "Membangun aplikasi Android dengan Clean Architecture dan Jetpack Compose yang terintegrasi dengan layanan Cloud serta sensor IoT secara real-time, lengkap dengan dukungan mode offline-first.",
                    fontSize = 13.sp,
                    color = Color(0xFF065F46)
                )
            }
        }
    }
}