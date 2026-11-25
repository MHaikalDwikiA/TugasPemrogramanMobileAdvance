package com.latihan.mobilelanjut.catalog.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CatalogScreen(vm: CatalogViewModel = viewModel()) {
    // KUSTOMISASI WAJIB: Palet warna tema sendiri (Biru Indigo)
    val accent = Color(0xFF4F46E5)
    val state by vm.uiState.collectAsStateWithLifecycle()
    val snackHost = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.messages.collect { msg ->
            if (msg is CatalogMessage.ShowSnackbar) {
                snackHost.showSnackbar(msg.text)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackHost) },
        containerColor = Color(0xFFF8FAFC)
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Kartu Identitas & Badge Keranjang
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = accent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mohamad Haikal Dwiki Akbar - RPL 5A",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Katalog Buku IT Reaktif",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White
                    ) {
                        Text(
                            text = "Cart: ${state.cartCount}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Kolom Pencarian + Tombol Reset Keranjang
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = vm::onQueryChange,
                    label = { Text("Cari buku atau kategori...") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedButton(
                    onClick = { vm.onResetCart() },
                    enabled = state.cartCount > 0,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reset", color = if (state.cartCount > 0) accent else Color.Gray)
                }
            }

            // Status Layar (Loading, Empty, Success)
            when (val content = state.content) {
                CatalogContentState.Loading -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = accent)
                }

                CatalogContentState.Empty -> Text(
                    text = "Produk tidak ditemukan.",
                    color = Color.Gray
                )

                is CatalogContentState.Success -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(content.items, key = { it.id }) { item ->
                        ElevatedCard(
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(item.name, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "${item.category} • ${item.price}",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }
                                Button(
                                    onClick = { vm.onAddToCart(item) },
                                    colors = ButtonDefaults.buttonColors(containerColor = accent),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("+ Beli")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}