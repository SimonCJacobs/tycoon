package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

fun squareSerializerModule(): SerializersModule {
    return SerializersModule {
        polymorphic( Square::class ) {
            subclass( CardSquare::class, CardSquare.serializer() )
            subclass( FreeParkingSquare::class, FreeParkingSquare.serializer() )
            subclass( GoSquare::class, GoSquare.serializer() )
            subclass( GoToJailSquare::class, GoToJailSquare.serializer() )
            subclass( JailSquare::class, JailSquare.serializer() )
            subclass( JustVisitingJailSquare::class, JustVisitingJailSquare.serializer() )
            subclass( NullProperty::class, NullProperty.serializer() )
            subclass( NullSquare::class, NullSquare.serializer() )
            subclass( Station::class, Station.serializer() )
            subclass( Street::class, Street.serializer() )
            subclass( TaxSquare::class, TaxSquare.serializer() )
            subclass( Utility::class, Utility.serializer() )
        }
    }
}