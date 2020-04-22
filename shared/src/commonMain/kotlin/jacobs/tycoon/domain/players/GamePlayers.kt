package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.pieces.PlayingPiece

class GamePlayers {

    private val players: MutableList < Player > = mutableListOf()

    fun addPlayer( player: Player ) {
        this.players.add( player )
    }

    fun getPiecesInUse(): Set < PlayingPiece > {
        return this.players.map { it.piece }
            .toSet()
    }

}