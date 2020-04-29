package jacobs.tycoon.domain.pieces

import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable

@Serializable
class PlayingPieceList (
    private val playingPieceList: List < PlayingPiece >
) : List < PlayingPiece > by playingPieceList, MessageContent {

}