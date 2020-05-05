package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.services.wrapFromExcludingFirstMatch

class GamePlayers {

    private val players: MutableMap < String, Player > = mutableMapOf()

    fun addPlayer( player: Player ) {
        this.players.put( player.name, player )
    }

    fun asSortedList(): List < Player > {
        return this.players.values.toList().sorted()
    }

    fun count(): Int {
        return this.players.size
    }

    fun first(): Player {
        return this.asSortedList().first()
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
        val previousPlayerIndex = this.asSortedList().indexOf( previousPlayer )
        return this.asSortedList().elementAt(  previousPlayerIndex + 1 )
    }

    fun playersToLeftExcluding( excludedPlayer: Player ): List < Player > {
        return this.asSortedList().wrapFromExcludingFirstMatch { it == excludedPlayer }
    }

}
