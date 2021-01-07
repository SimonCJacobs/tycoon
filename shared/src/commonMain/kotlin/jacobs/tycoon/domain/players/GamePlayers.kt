package jacobs.tycoon.domain.players

import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.services.wrapFromExcludingFirstMatch
import kotlin.random.Random

class GamePlayers {

    private val playerStatuses: MutableMap < Player, PlayerStatus > = mutableMapOf()

    fun activeCount(): Int {
        return this.activeOrderedList().size
    }

    fun addPlayer( player: Player ): SeatingPosition {
        val newPosition = this.getNextSeatingPosition()
        val newStatus = PlayerStatus( newPosition )
        this.playerStatuses.put( player, newStatus )
        return newPosition
    }

    fun activeOrderedList(): List < Player > {
        return this.playerStatuses.entries
            .sortedBy { it.value.position }
            .filter { it.value.isInGame }
            .map { it.key }
    }

    fun countAll(): Int {
        return this.playerStatuses.size
    }

    fun doesAnyoneOwnProperty( property: Property ): Boolean {
        return this.playerStatuses.keys.any { it.owns( property ) }
    }

    fun eliminatePlayerFromGame( player: Player ) {
        this.playerStatuses.getValue( player ).isInGame = false
    }

    fun getActualPlayer( player: Player ): Player {
        return this.playerStatuses.keys.find { actualPlayers -> actualPlayers == player }!!
    }

    fun getByName( name: String ): Player? {
        return this.playerStatuses.keys.firstOrNull { name == it.name }
    }

    fun getPiecesInUse(): Set < PlayingPiece > {
        return this.activeOrderedList().map { it.piece }
            .toSet()
    }

    fun getPlayerAtPosition( position: SeatingPosition ): Player {
        return this.playerStatuses.entries.first { it.value.position == position }.key
    }

    fun getSoleActivePlayer(): Player {
        return this.activeOrderedList().single()
    }

    fun hasPlayerInPositionSignedUp( position: SeatingPosition ): Boolean {
        return this.playerStatuses.entries.any { it.value.position == position }
    }

    fun isThereAPlayerOfName( name: String ): Boolean {
        return this.playerStatuses.keys.any { name == it.name }
    }

    fun isPlayerInGame( player: Player ): Boolean {
        return this.playerStatuses.getValue( player ).isInGame
    }

    fun nextActive( previousPlayer: Player ): Player {
        val wrappedEntriesWithoutPrevious =
            this.playerStatuses.entries.sortedBy { it.value.position }
                .wrapFromExcludingFirstMatch { previousPlayer == it.key }
        return wrappedEntriesWithoutPrevious.first().key
    }

    fun playersToLeftExcluding( excludedPlayer: Player ): List < Player > {
        return this.activeOrderedList().wrapFromExcludingFirstMatch { it == excludedPlayer }
    }

    private fun getNextSeatingPosition(): SeatingPosition {
        val newPositionIndex =
            if ( this.playerStatuses.isEmpty() )
                0
            else
                this.playerStatuses.values.map { it.position }.maxOrNull()!!.index + 1
        return SeatingPosition( newPositionIndex )
    }

}
