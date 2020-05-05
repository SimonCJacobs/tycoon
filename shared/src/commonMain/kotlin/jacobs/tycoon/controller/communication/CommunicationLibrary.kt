package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.board.boardSerializerModule
import jacobs.tycoon.domain.board.squareSerializerModule
import jacobs.tycoon.domain.pieces.pieceSetSerializerModule
import jacobs.tycoon.domain.actions.GameActionCollection
import jacobs.tycoon.domain.dice.diceRollSerializerModule
import jacobs.websockets.content.SerializationLibrary

class CommunicationLibrary {

    val serializationLibrary = SerializationLibrary.build {

        ClientWelcomeMessage::class serializedBy ClientWelcomeMessage.serializer()
        GameActionCollection::class serializedBy GameActionCollection.serializer()
        OpenActionRequest::class serializedBy OpenActionRequest.serializer()
        PositionalActionRequest::class serializedBy PositionalActionRequest.serializer()

        serialModule( boardSerializerModule() )
        serialModule( diceRollSerializerModule() )
        serialModule( pieceSetSerializerModule() )
        serialModule( squareSerializerModule() )

    }

}