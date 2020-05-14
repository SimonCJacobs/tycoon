package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.services.wrapFromExcludingFirstMatch

class GamePlayers {

    private val playersAndIfInGame: MutableMap < Player, Boolean > = mutableMapOf()

    fun activeCount(): Int {
        return this.activeList().size
    }

    fun addPlayer( player: Player ): Boolean {
        this.playersAndIfInGame.put( player, true )
        return true
    }

    fun activeList(): List < Player > {
        return this.playersAndIfInGame.filterValues { it }.keys.sorted()
    }

    fun countAll(): Int {
        return this.playersAndIfInGame.size
    }

    fun doesAnyoneOwnProperty( property: Property ): Boolean {
        return this.playersAndIfInGame.keys.any { it.owns( property ) }
    }

    fun eliminatePlayerFromGame( player: Player ) {
        this.playersAndIfInGame.put( player, false )
    }

    fun getByName( name: String ): Player? {
        return this.playersAndIfInGame.keys.firstOrNull { name == it.name }
    }

    fun getPiecesInUse(): Set < PlayingPiece > {
        return this.activeList().map { it.piece }
            .toSet()
    }

    fun getPlayerAtPosition( position: SeatingPosition ): Player {
        return this.playersAndIfInGame.keys.first { it.position == position }
    }

    fun hasPlayerInPositionSignedUp( position: SeatingPosition ): Boolean {
        return this.playersAndIfInGame.keys.any { it.position == position }
    }

    fun isPlayerStillInGame( player: Player ): Boolean {
        return this.playersAndIfInGame.containsKey( player )
    }

    fun isThereAPlayerOfName( name: String ): Boolean {
        return this.playersAndIfInGame.keys.any { name == it.name }
    }

    fun nextActive( previousPlayer: Player ): Player {
        val wrappedEntriesWithoutPrevious =
            this.playersAndIfInGame.entries.toList()
                .wrapFromExcludingFirstMatch { previousPlayer == it.key }
        return wrappedEntriesWithoutPrevious.first { it.value }.key
    }

    fun playersToLeftExcluding( excludedPlayer: Player ): List < Player > {
        return this.activeList().wrapFromExcludingFirstMatch { it == excludedPlayer }
    }

}
