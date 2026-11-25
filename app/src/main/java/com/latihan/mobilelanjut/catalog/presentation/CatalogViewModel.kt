package com.latihan.mobilelanjut.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProductUi(
    val id: String,
    val name: String,
    val category: String,
    val price: String
)

sealed interface CatalogContentState {
    data object Loading : CatalogContentState
    data object Empty : CatalogContentState
    data class Success(val items: List<ProductUi>) : CatalogContentState
}

data class CatalogScreenState(
    val query: String = "",
    val cartCount: Int = 0,
    val content: CatalogContentState = CatalogContentState.Loading
)

sealed interface CatalogMessage {
    data class ShowSnackbar(val text: String) : CatalogMessage
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CatalogViewModel : ViewModel() {
    private val _query = MutableStateFlow("")
    private val _uiState = MutableStateFlow(CatalogScreenState())
    val uiState: StateFlow<CatalogScreenState> = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<CatalogMessage>()
    val messages: SharedFlow<CatalogMessage> = _messages.asSharedFlow()

    // KUSTOMISASI WAJIB: Minimal 5 produk tema sendiri (Tema Buku IT)
    private val sampleCatalog = listOf(
        ProductUi("1", "Clean Architecture", "Arsitektur", "Rp 185.000"),
        ProductUi("2", "Kotlin in Action", "Pemrograman", "Rp 210.000"),
        ProductUi("3", "Jetpack Compose Essentials", "Android", "Rp 165.000"),
        ProductUi("4", "Design Patterns Explained", "Arsitektur", "Rp 195.000"),
        ProductUi("5", "Pragmatic Programmer", "Software Eng", "Rp 220.000")
    )

    init {
        _query
            .debounce(400)
            .distinctUntilChanged()
            .flatMapLatest { key ->
                flow {
                    emit(CatalogContentState.Loading)
                    delay(300)
                    val filtered = sampleCatalog.filter {
                        it.name.contains(key, ignoreCase = true) ||
                                it.category.contains(key, ignoreCase = true)
                    }
                    emit(
                        if (filtered.isEmpty()) CatalogContentState.Empty
                        else CatalogContentState.Success(filtered)
                    )
                }
            }
            .onEach { c -> _uiState.update { it.copy(content = c) } }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(q: String) {
        _query.value = q
        _uiState.update { it.copy(query = q) }
    }

    fun onAddToCart(item: ProductUi) {
        _uiState.update { it.copy(cartCount = it.cartCount + 1) }
        viewModelScope.launch {
            _messages.emit(CatalogMessage.ShowSnackbar("${item.name} masuk keranjang"))
        }
    }

    // FITUR TAMBAHAN KREASI SENDIRI: Reset Keranjang via ViewModel
    fun onResetCart() {
        if (_uiState.value.cartCount > 0) {
            _uiState.update { it.copy(cartCount = 0) }
            viewModelScope.launch {
                _messages.emit(CatalogMessage.ShowSnackbar("Keranjang berhasil dikosongkan"))
            }
        }
    }
}