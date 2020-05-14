package jacobs.tycoon.domain.rules

import jacobs.tycoon.domain.board.currency.CurrencyAmount

interface JailRules {
    val leaveJailFineAmount: CurrencyAmount
    val mustLeaveRollCount: Int
    val toJailDoubleCount: Int

}