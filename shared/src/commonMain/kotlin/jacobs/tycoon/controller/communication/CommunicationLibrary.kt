package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.board.boardSerializerModule
import jacobs.tycoon.domain.pieces.pieceSetSerializerModule
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.actions.gameActionSerializerModule
import jacobs.tycoon.domain.board.cards.cardsSerializerModule
import jacobs.tycoon.domain.board.currency.currencySerializerModule
import jacobs.tycoon.domain.board.squares.squareSerializerModule
import jacobs.tycoon.domain.dice.diceRollSerializerModule
import jacobs.tycoon.domain.phases.results.jailResultSerializerModule
import jacobs.websockets.content.SerializationLibrary

class CommunicationLibrary {

    val serializationLibrary = SerializationLibrary.build {

        ActionRequest::class serializedBy ActionRequest.serializer()
        ClientWelcomeMessage::class serializedBy ClientWelcomeMessage.serializer()
        GameActionCollection::class serializedBy GameActionCollection.serializer()

        serialModule( boardSerializerModule() )
        serialModule( cardsSerializerModule() )
        serialModule( currencySerializerModule() )
        serialModule( diceRollSerializerModule() )
        serialModule( gameActionSerializerModule() )
        serialModule( jailResultSerializerModule() )
        serialModule( pieceSetSerializerModule() )
        serialModule( squareSerializerModule() )

    }

}