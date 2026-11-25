package com.latihan.mobilelanjut.checkout.data

import com.latihan.mobilelanjut.checkout.domain.*

data class CheckoutRawData(
    val subTotalIdr: Long,
    val promoCutIdr: Long,
    val grandTotalIdr: Long
)

fun CheckoutRawData.toSummary() = CheckoutSummary(subTotalIdr, promoCutIdr, grandTotalIdr)

class OrderRepositoryImpl : OrderRepository {
    override fun submitOrder(items: List<OrderItem>, promoCode: String?): CheckoutSummary {
        val subtotal = items.sumOf { it.price * it.qty }

        // KUSTOMISASI WAJIB: Ganti kode promo & persentase diskon kreasi Anda sendiri
        val cut = if (promoCode == "PROMO20") (subtotal * 20) / 100 else 0L

        return CheckoutRawData(subtotal, cut, subtotal - cut).toSummary()
    }
}