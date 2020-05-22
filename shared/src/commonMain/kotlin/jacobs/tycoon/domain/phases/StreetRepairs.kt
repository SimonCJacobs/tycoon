package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.Player

class StreetRepairs(
    playerWithTurn: Player,
    perHouse: CurrencyAmount,
    perHotel: CurrencyAmount
) : PaymentDue(
    playerWithTurn = playerWithTurn,
    playersOwingMoney = listOf( playerWithTurn ),
    amountDue = perHouse * playerWithTurn.totalNumberOfHouses() +
        perHotel * playerWithTurn.totalNumberOfHotels(),
    reason = "street repairs",
    playerOwedMoney = null
)