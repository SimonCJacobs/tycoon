package jacobs.tycoon.controller.communication

import jacobs.tycoon.controller.communication.application.ApplicationRequest
import jacobs.tycoon.controller.communication.application.applicationActionSerializerModule
import jacobs.tycoon.domain.board.boardSerializerModule
import jacobs.tycoon.domain.pieces.pieceSetSerializerModule
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.actions.gameActionSerializerModule
import jacobs.tycoon.domain.board.cards.cardsSerializerModule
import jacobs.tycoon.domain.board.currency.currencySerializerModule
import jacobs.tycoon.domain.board.squares.squareSerializerModule
import jacobs.tycoon.domain.dice.diceRollSerializerModule
import jacobs.tycoon.domain.phases.results.jailResultSerializerModule
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.websockets.content.SerializationLibrary

class CommunicationLibrary {

    val serializationLibrary = SerializationLibrary.build {

        ApplicationRequest::class serializedBy ApplicationRequest.serializer()
        BinaryGameActionRequest::class serializedBy BinaryGameActionRequest.serializer()
        ClientWelcomeMessage::class serializedBy ClientWelcomeMessage.serializer()
        GameActionCollection::class serializedBy GameActionCollection.serializer()
        GameActionRequestWithResponse::class serializedBy GameActionRequestWithResponse.serializer()

        SeatingPosition::class serializedBy SeatingPosition.serializer()

        serialModule( applicationActionSerializerModule() )
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