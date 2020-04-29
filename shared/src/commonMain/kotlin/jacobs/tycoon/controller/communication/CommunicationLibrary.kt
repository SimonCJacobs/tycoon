package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.pieces.PlayingPieceList
import jacobs.websockets.content.ContentClassCollection

class CommunicationLibrary {

    val contentClasses = ContentClassCollection.build {
        PlayingPieceList::class serializedBy PlayingPieceList.serializer()
        SimpleRequestWrapper::class serializedBy SimpleRequestWrapper.serializer()
    }

}