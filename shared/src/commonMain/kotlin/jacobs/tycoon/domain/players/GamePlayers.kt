package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.pieces.PlayingPiece

class GamePlayers {

    private val players: MutableMap < String, Player > = mutableMapOf()

    fun addPlayer( player: Player ) {
        this.players.put( player.name, player )
    }

    fun allExcept( excludedPlayer: Player ): List < Player > {
        return this.players.values.filter { it != excludedPlayer }
    }

    fun count(): Int {
        return this.players.size
    }

    fun getByName( playerName: String ): Player {
        return this.players.getValue( playerName )
    }

    fun getPiecesInUse(): Set < PlayingPiece > {
        return this.players.values.map { it.piece }
            .toSet()
    }

    fun getPlayerAtPosition( position: Position ): Player {
        return this.players.values.first { it.position == position }
    }

    fun hasPlayerInPosition( position: Position ): Boolean {
        return this.players.values.any { it.position == position }
    }

    fun isThereAPlayerOfName( name: String ): Boolean {
        return this.players.contains( name )
    }

}