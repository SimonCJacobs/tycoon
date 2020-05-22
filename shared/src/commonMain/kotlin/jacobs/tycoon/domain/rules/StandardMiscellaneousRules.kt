package jacobs.tycoon.domain.rules

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class StandardMiscellaneousRules ( kodein: Kodein ) : MiscellaneousRules {

    override val currency by kodein.instance < Currency > ()

    override val goCreditAmount: CurrencyAmount = CurrencyAmount( 200, currency )
    override val housesToAHotel = 5
    override val initialHotelStock = 12
    override val initialHousingStock = 32
    override val initialCashCount: CurrencyAmount = CurrencyAmount( 1500, currency )
    override val minimumNumberOfPlayers = 2
    override val numberOfTurnsHaveToChargeRent: Int = 2

}