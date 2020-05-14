package jacobs.tycoon.view.components.board.centre

import jacobs.tycoon.clientcontroller.AuctionController
import jacobs.tycoon.clientcontroller.BoardController
import jacobs.tycoon.clientcontroller.DealController
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * It's a not a factory. Mithril diffs partly on component reference, so we need
 * to supply the exact same object references to avoid unnecessary DOM recreation. This is only
 * necessary on those components which are used for input, because it's then when there is lots of
 * diffing.
 */
class CentreCellReposifactory (kodein: Kodein ) {

    private val auctionController by kodein.instance < AuctionController > ()
    private val boardController by kodein.instance < BoardController > ()
    private val dealController by kodein.instance < DealController > ()
    private val gameName by kodein.instance < String > ( tag = "gameName" )

    private val auctionComponent = AuctionComponent()
    private val composingDealComponent = ComposingDealComponent()

    fun getCentreCell( squaresToASideExcludingCorners: Int ): CentreCellComponent {
        return when {
            this.boardController.isReadingCard() ->
                CardCentreCellComponent(
                    this.boardController.getCardBeingRead(),
                    squaresToASideExcludingCorners
                )
            this.boardController.isPlayerComposingDeal() -> {
                composingDealComponent.dealController = dealController
                composingDealComponent.squaresToASideExcludingCorners = squaresToASideExcludingCorners
                composingDealComponent
            }
            this.boardController.isTradeBeingConsidered() ->
                TradeConsiderationComponent(
                    dealController,
                    squaresToASideExcludingCorners
                )
            this.boardController.isAuctionUnderway() -> {
                auctionComponent.auctionController = auctionController
                auctionComponent.squaresToASideExcludingCorners = squaresToASideExcludingCorners
                auctionComponent
            }
            else -> LogoCentreCellComponent( gameName, squaresToASideExcludingCorners )
        }
    }

}