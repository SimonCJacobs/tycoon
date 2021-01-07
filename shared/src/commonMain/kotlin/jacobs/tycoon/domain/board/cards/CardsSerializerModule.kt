package jacobs.tycoon.domain.board.cards

import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun cardsSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic ( CardSet::class ) {
            subclass( ChanceCards::class, ChanceCards.serializer() )
            subclass( CommunityChestCards::class, CommunityChestCards.serializer() )
        }
    }
}