package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.AuctionState
import jacobs.tycoon.domain.actions.auction.AuctionNotification
import jacobs.tycoon.domain.actions.auction.ConcludeAuction
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.services.auction.AuctionPhase
import jacobs.tycoon.domain.services.auction.AuctionStatus
import jacobs.tycoon.domain.services.auction.BidWarning
import jacobs.tycoon.services.speechsynthesis.VoiceSynthesiser
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.pieces.PieceDisplayStrategy
import kotlinx.coroutines.launch
import org.js.mithril.Component
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class AuctionController ( kodein: Kodein ) : UserInterfaceController( kodein ) {

    private val auctionState by kodein.instance < AuctionState > ()
    private val changeListener by kodein.instance < ChangeListener > ()
    private val currency by kodein.instance < Currency > ()
    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()
    private val pieceDisplayStrategy by kodein.instance < PieceDisplayStrategy  > ()
    private val voiceSynthesiser by kodein.instance < VoiceSynthesiser > ()

    init {
        changeListener.registerPhaseChangeListener { newPhase ->
            if ( newPhase is AuctionProperty ) {
                announceAuction()
                auctionState.reset( currency )
            }
        }
        changeListener.registerActionListener { gameAction ->
            if ( gameAction is AuctionNotification ) doNotificationAnnouncements()
            if ( gameAction is ConcludeAuction ) announceConclusionOfAuction( gameAction.status )
        }
    }

    @Suppress( "MemberVisibilityCanBePrivate" )
    fun announceAuction() {
        voiceSynthesiser.say( this.getAuctionTitle() )
    }

    @Suppress( "MemberVisibilityCanBePrivate" )
    fun announceConclusionOfAuction( auctionEndStatus: AuctionStatus ) {
        if ( false == auctionEndStatus.areThereBids() )
            voiceSynthesiser.say( auctionEndStatus.property.name + "goes unsold" )
        else
            voiceSynthesiser.say( "Sold! To ${ auctionEndStatus.leadingBidderNotNull.name } for " +
                auctionEndStatus.leadingBidNotNull.wordsToSay() )
    }

    @Suppress( "MemberVisibilityCanBePrivate" )
    fun doNotificationAnnouncements() {
        when( val phase = status().phase ) {
            AuctionPhase.GOING_ONCE -> voiceSynthesiser.say( phase.toWords() )
            AuctionPhase.GOING_TWICE -> voiceSynthesiser.say( phase.toWords() )
            AuctionPhase.BID_MADE -> voiceSynthesiser.say(
                "Bid of ${ status().leadingBidNotNull.wordsToSay() } from " +
                    status().leadingBidderNotNull.name
            )
            else -> return
        }
    }

    fun areThereBids(): Boolean {
        return status().areThereBids()
    }

    fun bidBeingConsideredAsString(): String {
        return auctionState.bidBeingConsidered.amount.toString()
    }

    fun getAuctionTitle(): String {
        return "Auction for ${ getPropertyName() }"
    }

    fun getCurrencySymbol(): String {
        return currency.prefix
    }

    fun getLeadingBidderPieceDisplayComponent(): Component? {
        return if ( status().areThereBids() )
                pieceDisplayStrategy.getPieceDisplayComponent( status().leadingBidderNotNull.piece )
            else
                null
    }

    fun getPhaseDescription(): String {
        return when( val phase = status().phase ) {
            AuctionPhase.BIDDING_STARTED -> "In progress"
            AuctionPhase.BID_MADE -> "In progress"
            AuctionPhase.GOING_ONCE -> phase.toWords()
            AuctionPhase.GOING_TWICE -> phase.toWords()
            AuctionPhase.SOLD -> throw Error( "Should not get here during auction" )
            AuctionPhase.UNSOLD -> throw Error( "Should not get here during auction" )
        }
    }

    fun getWarning(): BidWarning {
        return this.auctionState.bidWarning!!
    }

    private fun isBidValid(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse { player ->
            status().validateBid(
                auctionState.bidBeingConsidered,
                player
            )
                .let {
                    auctionState.bidWarning = it.warning
                    it.isValid
                }
        }
    }

    fun isWarning(): Boolean {
        return null != this.auctionState.bidWarning
    }

    fun leadingBidNotNull(): CurrencyAmount {
        return status().leadingBidNotNull
    }

    fun leadingBidderNameNotNull(): String {
        return status().leadingBidderNotNull.name
    }

    fun makeBid() {
       if ( this.isBidValid() )
            launch { outgoingRequestController.makeBid(
                auctionState.bidBeingConsidered
            ) }
    }

    fun updateBidBeingConsidered( newBidAsString: String ) {
        this.auctionState.bidWarning = null
        this.auctionState.bidBeingConsidered =
            ( newBidAsString.toIntOrNull() ?: 0 )
                .let { CurrencyAmount( it, currency ) }
    }

    private fun status(): AuctionStatus {
        return gameState.game().getAuctionStatus()
    }

    private fun getPropertyName(): String {
        return status().property.name
    }

    private fun CurrencyAmount.wordsToSay(): String {
        return "$amount  ${ currency.inWords }"
    }

}