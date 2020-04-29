package jacobs.tycoon.domain

import jacobs.tycoon.domain.logs.ActionLog
import jacobs.tycoon.domain.logs.PlayerJoined
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.pieces.PlayingPieceList
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Game( kodein: Kodein ) {

    private val logger by kodein.instance < ActionLog > ()

    fun addPlayer( name: String, piece: PlayingPiece ): Player {
        return Player( name, piece )
            .also {
                logger.log( PlayerJoined( it ) )
            }
    }

    fun getAvailablePieces( pieceSet: PieceSet, players: GamePlayers ): PlayingPieceList {
        return PlayingPieceList( pieceSet.getAvailablePieces( players ) )
    }

}