package jacobs.tycoon.domain.board.cards

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun cardsSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic ( CardSet::class ) {
            ChanceCards::class with ChanceCards.serializer()
            CommunityChestCards::class with CommunityChestCards.serializer()
        }
    }
}