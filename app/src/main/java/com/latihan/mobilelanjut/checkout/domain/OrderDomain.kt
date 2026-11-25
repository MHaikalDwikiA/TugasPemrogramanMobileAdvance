package com.latihan.mobilelanjut.checkout.domain

data class OrderItem(
    val id: String,
    val name: String,
    val price: Long,
    val qty: Int
)

data class CheckoutSummary(
    val subtotal: Long,
    val discount: Long,
    val totalPayable: Long
)

interface OrderRepository {
    fun submitOrder(items: List<OrderItem>, promoCode: String?): CheckoutSummary
}

class ProcessCheckoutUseCase(private val repository: OrderRepository) {
    operator fun invoke(items: List<OrderItem>, promoCode: String?): CheckoutSummary {
        require(items.isNotEmpty()) { "Daftar pesanan tidak boleh kosong" }
        return repository.submitOrder(items, promoCode?.trim()?.uppercase())
    }
}