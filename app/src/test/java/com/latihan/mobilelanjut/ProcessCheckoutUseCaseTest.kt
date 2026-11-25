package com.latihan.mobilelanjut

import com.latihan.mobilelanjut.checkout.data.OrderRepositoryImpl
import com.latihan.mobilelanjut.checkout.domain.OrderItem
import com.latihan.mobilelanjut.checkout.domain.ProcessCheckoutUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class ProcessCheckoutUseCaseTest {
    @Test
    fun `promo PROMO20 memotong total sebesar 20 persen`() {
        val useCase = ProcessCheckoutUseCase(OrderRepositoryImpl())
        val items = listOf(OrderItem("1", "Produk Uji", 100_000L, 1))
        val res = useCase(items, "promo20")

        assertEquals(20_000L, res.discount)
        assertEquals(80_000L, res.totalPayable)
    }
}