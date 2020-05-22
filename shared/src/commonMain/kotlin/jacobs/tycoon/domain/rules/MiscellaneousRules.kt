package jacobs.tycoon.domain.rules

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount

interface MiscellaneousRules {
    val currency: Currency
    val goCreditAmount: CurrencyAmount
    val initialHotelStock: Int
    val initialHousingStock: Int
    val housesToAHotel: Int
    val initialCashCount: CurrencyAmount
    val minimumNumberOfPlayers: Int
    val numberOfTurnsHaveToChargeRent: Int
}