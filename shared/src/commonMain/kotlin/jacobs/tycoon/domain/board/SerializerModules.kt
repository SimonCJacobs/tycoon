package jacobs.tycoon.domain.board

import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

fun boardSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( Board::class, StandardBoard::class ) {
            LondonBoard::class with LondonBoard.serializer()
        }
    }
}

fun squareSerializerModule(): SerialModule {
    return SerializersModule {
        polymorphic( Square::class, Property::class ) {
            ActionSquare::class with ActionSquare.serializer()
            Street::class with Street.serializer()
            Station::class with Station.serializer()
            Utility::class with Utility.serializer()
        }
    }
}