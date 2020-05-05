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

    private fun allExcept( excludedPlayers: Collection < Player > ): List < Player > {
        return this.players.values.filter { false == excludedPlayers.contains( it ) }
    }

    fun count(): Int {
        return this.players.size
    }

    /**
     * See method [next] regarding comments on iteration order
     */
    fun first(): Player {
        return this.players.values.first()
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

    /**
     * Note documentation explains entry iteration order of a Map created by mutableMapOf()
     * preserves the entry iteration order
     * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/mutable-map-of.html
     */
    fun next( previousPlayer: Player ): Player {
        val previousPlayerIndex = this.players.values.indexOf( previousPlayer )
        return this.players.values.elementAt(  previousPlayerIndex + 1 )
    }

}