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
    val selectedCategory: String = "Semua",
    val cartCount: Int = 0,
    val content: CatalogContentState = CatalogContentState.Loading
)

sealed interface CatalogMessage {
    data class ShowSnackbar(val text: String) : CatalogMessage
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CatalogViewModel : ViewModel() {
    private val _query = MutableStateFlow("")
    private val _category = MutableStateFlow("Semua")
    private val _uiState = MutableStateFlow(CatalogScreenState())
    val uiState: StateFlow<CatalogScreenState> = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<CatalogMessage>()
    val messages: SharedFlow<CatalogMessage> = _messages.asSharedFlow()

    val categories = listOf("Semua", "Kopi", "Non-Kopi", "Pastry")

    // KUSTOMISASI: 6 produk unik tema Kedai Kopi & Pastry
    private val sampleCatalog = listOf(
        ProductUi("1", "Kopi Susu Gula Aren", "Kopi", "Rp 22.000"),
        ProductUi("2", "Americano Iced", "Kopi", "Rp 18.000"),
        ProductUi("3", "Matcha Latte", "Non-Kopi", "Rp 25.000"),
        ProductUi("4", "Chocolate Hazelnut", "Non-Kopi", "Rp 24.000"),
        ProductUi("5", "Croissant Butter", "Pastry", "Rp 20.000"),
        ProductUi("6", "Almond Danish", "Pastry", "Rp 23.000")
    )

    init {
        combine(
            _query.debounce(400).distinctUntilChanged(),
            _category
        ) { key, category -> key to category }
            .flatMapLatest { (key, category) ->
                flow {
                    emit(CatalogContentState.Loading)
                    delay(300)
                    val filtered = sampleCatalog.filter { item ->
                        val matchQuery = item.name.contains(key, ignoreCase = true) ||
                                item.category.contains(key, ignoreCase = true)
                        val matchCategory = category == "Semua" || item.category == category
                        matchQuery && matchCategory
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

    // FITUR TAMBAHAN 1: Filter Kategori via ViewModel
    fun onCategorySelect(category: String) {
        _category.value = category
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onAddToCart(item: ProductUi) {
        _uiState.update { it.copy(cartCount = it.cartCount + 1) }
        viewModelScope.launch {
            _messages.emit(CatalogMessage.ShowSnackbar("${item.name} masuk keranjang"))
        }
    }

    // FITUR TAMBAHAN 2: Reset Keranjang via ViewModel
    fun onResetCart() {
        if (_uiState.value.cartCount > 0) {
            _uiState.update { it.copy(cartCount = 0) }
            viewModelScope.launch {
                _messages.emit(CatalogMessage.ShowSnackbar("Keranjang berhasil dikosongkan"))
            }
        }
    }
}