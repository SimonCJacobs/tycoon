package jacobs.tycoon.domain.rules

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class MiscellaneousRules ( kodein: Kodein ) {

    private val currency by kodein.instance < Currency > ()

    val goCreditAmount: CurrencyAmount = CurrencyAmount( 200, currency )
    val initialHotelStock = 12
    val initialHousingStock = 32
    val housesToAHotel = 5
    val initialCashCount: CurrencyAmount = CurrencyAmount( 1500, currency )
    val minimumNumberOfPlayers = 2

}