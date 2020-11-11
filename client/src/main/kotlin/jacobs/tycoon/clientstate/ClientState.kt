package jacobs.tycoon.clientstate

import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Player
import jacobs.websockets.SocketId

class ClientState {

    /**
     * Server
     */
    var isWaitingForServer = true
    var maybeSocket: SocketId? = null
    val socketNotNull: SocketId
        get() = maybeSocket!!

    /**
     * Drag and drop
     */
    var pieceBeingDragged: PlayingPiece? = null

    /**
     * Dealing
     */
    var cashOffered: CurrencyAmount = CurrencyAmount.NULL
    var cashWanted: CurrencyAmount = CurrencyAmount.NULL
    var dealType: DealType = DealType.COMPOSING_TRADE
    var getOutOfJailFreeCardsOffered: Int = 0
    var getOutOfJailFreeCardsWanted: Int = 0
    var housesToBuild: MutableMap < Street, Int > = mutableMapOf()
    var housesToSell: MutableMap < Street, Int > = mutableMapOf()
    var isComposingDeal: Boolean = false
    var playerIntendingToDealWith: Player? = null
    var propertiesSelectedForDeal: Map < DealType, MutableList < Property > > =
            DealType.values().associate { Pair( it, mutableListOf < Property > () ) }

    /**
     * Admin
     */
    var authorisedToAdministrate = false

}