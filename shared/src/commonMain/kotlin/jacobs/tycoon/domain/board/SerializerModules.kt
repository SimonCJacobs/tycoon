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
        polymorphic( Square::class, Property::class, ActionSquare::class ) {
            CardSquare::class with CardSquare.serializer()
            FreeParkingSquare::class with FreeParkingSquare.serializer()
            GoSquare::class with GoSquare.serializer()
            GoToJailSquare::class with GoToJailSquare.serializer()
            JailSquare::class with JailSquare.serializer()
            JustVisitingJailSquare::class with JustVisitingJailSquare.serializer()
            NullSquare::class with NullSquare.serializer()
            Station::class with Station.serializer()
            Street::class with Street.serializer()
            TaxSquare::class with TaxSquare.serializer()
            Utility::class with Utility.serializer()
        }
    }
}