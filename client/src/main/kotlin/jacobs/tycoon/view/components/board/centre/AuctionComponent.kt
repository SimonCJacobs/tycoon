package jacobs.tycoon.view.components.board.centre

import jacobs.mithril.HyperScriptBuilder
import jacobs.mithril.m
import jacobs.mithril.Tag
import jacobs.tycoon.clientcontroller.AuctionController
import jacobs.tycoon.domain.services.auction.BidWarning
import org.js.mithril.VNode
import org.w3c.dom.HTMLInputElement

class AuctionComponent : CentreCellComponent() {

    lateinit var auctionController: AuctionController
    override var squaresToASideExcludingCorners: Int = -1

    override fun getContent(): HyperScriptBuilder.Details.() -> Unit {
        return {
            children(
                m( Tag.h4 ) {
                    content( auctionController.getAuctionTitle() )
                },
                getAuctionStatus(),
                getLeadingBid(),
                getBidEntry(),
                getPossibleWarningPanel()
            )
        }
    }

    override fun getStyleObject(): CentreCellStylesBase {
        return object : CentreCellStylesBase() {

        }
    }

    private fun getAuctionStatus(): VNode {
        return m( Tag.h5 ) {
            content( "Auction status: " + auctionController.getPhaseDescription() )
        }
    }

    @Suppress( "unused" )
    private fun getLeadingBid(): VNode {
        return m( Tag.div ) {
            attributes( object {
                val style = object {
                    val display = "inline-block"
                }
            } )
            children(
                m( Tag.h6 ) { content( getLeadingBidText() ) },
                getPieceDisplayed()
            )
        }
    }

    private fun getLeadingBidText(): String {
        if ( this.auctionController.areThereBids() )
            return "Leading bid: ${ auctionController.leadingBidNotNull() } from " +
                auctionController.leadingBidderNameNotNull()
        else
            return "No bids"
    }

    private fun getPieceDisplayed(): VNode? {
        return auctionController.getLeadingBidderPieceDisplayComponent()
            ?.let { m( it ) }
    }

    @Suppress( "unused" )
    private fun getBidEntry(): VNode {
        val inputId = "bidInput"
        return m( Tag.form ){
            children(
                m( Tag.label ) {
                    attributes ( object {
                        val `for` = inputId
                    } )
                        content( "Enter bid: " + auctionController.getCurrencySymbol() )
                },
                m( Tag.input ) {
                    attributes ( object {
                        val id = inputId
                        val type = "text"
                        val value = auctionController.bidBeingConsideredAsString()
                    } )
                    eventHandlers {
                        onInputExt = { event ->
                            ( event.target as HTMLInputElement ).value
                                .let { auctionController.updateBidBeingConsidered( it ) }
                        }
                    }
                },
                m( Tag.input ){
                    attributes ( object {
                        val type = "submit"
                        val value = "Submit bid"
                    } )
                    eventHandlers {
                        onclick = {
                            it.preventDefault()
                            auctionController.makeBid()
                        }
                    }
                }
            )
        }
    }

    private fun getPossibleWarningPanel(): VNode? {
        if ( this.auctionController.isWarning() )
            return this.getWarningPanel()
        else
            return null
    }

    @Suppress( "unused" )
    private fun getWarningPanel(): VNode {
        return m( Tag.h6 ) {
            attributes( object {
                val style = object {
                    val textColor = "red"
                }
            } )
            content( getWarningText() )
        }
    }

    private fun getWarningText(): String {
        return when ( this.auctionController.getWarning() ) {
            BidWarning.HAVE_TOP_BID -> "You already have the leading bid"
            BidWarning.INSUFFICIENT_CASH -> "You don't have enough cash for that bid"
            BidWarning.TOO_LOW -> "You must be higher than the current leading bid"
        }
    }

}
