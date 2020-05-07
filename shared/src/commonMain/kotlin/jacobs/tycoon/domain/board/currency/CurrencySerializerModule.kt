package jacobs.tycoon.domain.board.currency

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun currencySerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic ( Currency::class ) {
            NullCurrency::class with NullCurrency.serializer()
            PoundsSterling::class with PoundsSterling.serializer()
        }
    }
}