package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun currencySerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic ( Currency::class ) {
            subclass( NullCurrency::class, NullCurrency.serializer() )
            subclass( PoundsSterling::class, PoundsSterling.serializer() )
        }
    }
}