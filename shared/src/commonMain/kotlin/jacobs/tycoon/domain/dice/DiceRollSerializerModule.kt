package jacobs.tycoon.domain.dice

import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun diceRollSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic( DiceRoll::class ) {
            subclass( DiceRollImpl::class, DiceRollImpl.serializer() )
            subclass( NullDiceRoll::class, NullDiceRoll.serializer() )
        }
    }
}