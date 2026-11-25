package com.latihan.mobilelanjut.catalog.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
    // KUSTOMISASI: Palet Warna Merah Marun
    val accent = Color(0xFF9F1239)
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
        containerColor = Color(0xFFFFF1F2)
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Kartu Identitas Mahasiswa & Badge Keranjang
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
                            text = "Haikal Coffee & Bites",
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

            // 2. Kolom Pencarian + Tombol Reset Keranjang
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = vm::onQueryChange,
                    label = { Text("Cari menu...") },
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

            // 3. Filter Kategori (Terhubung ke ViewModel)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.categories) { category ->
                    val isSelected = state.selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { vm.onCategorySelect(category) },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accent,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // 4. Daftar Produk (Loading / Empty / Success)
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
                    text = "Menu tidak ditemukan.",
                    color = Color.Gray
                )

                is CatalogContentState.Success -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(content.items, key = { it.id }) { item ->
                        ElevatedCard(
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
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