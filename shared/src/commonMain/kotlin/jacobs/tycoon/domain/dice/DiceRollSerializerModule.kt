package jacobs.tycoon.domain.dice

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun diceRollSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( DiceRoll::class ) {
            DiceRollImpl::class with DiceRollImpl.serializer()
            NullDiceRoll::class with NullDiceRoll.serializer()
        }
    }
}