package jacobs.tycoon.domain.phases.results

import jacobs.tycoon.domain.dice.DiceRoll
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

@Serializable
abstract class RollForMoveFromJailResult {

    companion object {
        val NULL = NonDiceJailResult( JailOutcome.REMAIN_IN_JAIL )
    }

    abstract val diceRoll: DiceRoll
    abstract val jailOutcome: JailOutcome
    abstract val rollForMoveResult: RollForMoveResult
}

@Serializable
class FailedEscapeResult(
    override val jailOutcome: JailOutcome,
    override val diceRoll: DiceRoll
) : RollForMoveFromJailResult() {
    override val rollForMoveResult: RollForMoveResult = RollForMoveResult.NULL
}

@Serializable
class MovingOutOfJailWithDiceResult(
    override val rollForMoveResult: RollForMoveResult,
    override val jailOutcome: JailOutcome
) : RollForMoveFromJailResult() {
    override val diceRoll: DiceRoll
        get() = rollForMoveResult.diceRoll
}

@Serializable
class NonDiceJailResult(
    override val jailOutcome: JailOutcome
) : RollForMoveFromJailResult() {
    override val diceRoll = DiceRoll.NULL
    override val rollForMoveResult: RollForMoveResult = RollForMoveResult.NULL
}

fun jailResultSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic( RollForMoveFromJailResult::class ) {
            subclass( FailedEscapeResult::class, FailedEscapeResult.serializer() )
            subclass( MovingOutOfJailWithDiceResult::class, MovingOutOfJailWithDiceResult.serializer() )
            subclass( NonDiceJailResult::class, NonDiceJailResult.serializer() )
        }
    }
}
