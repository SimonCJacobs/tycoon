package jacobs.tycoon.domain.rules

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class StandardJailRules( kodein: Kodein ) : JailRules {

    override val leaveJailFineAmount = CurrencyAmount(
        50, kodein.direct.instance()
    )
    override val mustLeaveRollCount = 3
    override val toJailDoubleCount = 3
}