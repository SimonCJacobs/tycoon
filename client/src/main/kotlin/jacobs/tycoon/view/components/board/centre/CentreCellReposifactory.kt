package jacobs.tycoon.view.components.board.centre

import jacobs.tycoon.clientcontroller.AuctionController
import jacobs.tycoon.clientcontroller.BoardController
import jacobs.tycoon.clientcontroller.DealController
import jacobs.tycoon.clientstate.DealType
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * It's a not a factory. Mithril diffs partly on component reference, so we need
 * to supply the exact same object references to avoid unnecessary DOM recreation. This is only
 * necessary on those components which are used for input, because it's then when there is lots of
 * diffing.
 */
class CentreCellReposifactory ( kodein: Kodein ) {

    private val auctionController by kodein.instance < AuctionController > ()
    private val boardController by kodein.instance < BoardController > ()
    private val dealController by kodein.instance < DealController > ()
    private val gameName by kodein.instance < String > ( tag = "gameName" )

    private val auctionComponent = AuctionComponent()
    private var buildingComponent: BuildingComponent? = null
    private var sellingHousesComponent: SellingHousesComponent? = null
    private var tradingComponent: ComposingTradeComponent? = null

    fun getCentreCell( squaresToASideExcludingCorners: Int ): CentreCellComponent {
        return when {
            this.boardController.isThereAWinner() ->
                WinnerDisplayComponent(
                    this.boardController.getTheWinner(),
                    squaresToASideExcludingCorners
                )
            this.boardController.isBankruptcyUnderway() ->
                BankruptcyDisplayComponent(
                    this.boardController.getBankruptPlayer(),
                    { this.boardController.carryOutBankruptcy() },
                    squaresToASideExcludingCorners
                )
            this.boardController.isReadingCard() ->
                CardCentreCellComponent(
                    this.boardController.getCardBeingRead(),
                    squaresToASideExcludingCorners
                )
            this.boardController.isTradeBeingConsidered() ->
                TradeConsiderationComponent(
                    dealController,
                    squaresToASideExcludingCorners
                )
            this.boardController.isTradeBeingConsideredBySomeoneElse() ->
                TradeConsiderationHoldingComponent(
                    dealController,
                    squaresToASideExcludingCorners
                )
            this.boardController.isAuctionUnderway() -> {
                auctionComponent.auctionController = auctionController
                auctionComponent.squaresToASideExcludingCorners = squaresToASideExcludingCorners
                auctionComponent
            }
                // All the others are exclusive states: this should come here as the others
                // take priority.
            this.dealController.isPlayerComposingDeal() ->
                this.getDealingCentreCell( squaresToASideExcludingCorners )
            else -> LogoCentreCellComponent( gameName, squaresToASideExcludingCorners )
        }
    }

    private fun getDealingCentreCell( squaresToASideExcludingCorners: Int ): CentreCellComponent {
        return when ( dealController.getDealType() ) {
            DealType.BUILD_HOUSES -> buildingComponent ?:
                BuildingComponent( dealController, squaresToASideExcludingCorners )
                    .also { buildingComponent = it }
            DealType.COMPOSING_TRADE -> tradingComponent ?:
                ComposingTradeComponent( dealController, squaresToASideExcludingCorners )
                    .also { tradingComponent = it }
            DealType.SELL_HOUSES -> sellingHousesComponent ?:
                SellingHousesComponent( dealController, squaresToASideExcludingCorners )
                    .also { sellingHousesComponent = it }
            DealType.MORTGAGING ->
                MortgagingComponent( dealController, squaresToASideExcludingCorners )
            DealType.PAYING_OFF_MORTGAGES ->
                PayingOffMortgagesComponent( dealController, squaresToASideExcludingCorners )
        }
    }

}