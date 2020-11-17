package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.clientstate.DealType
import jacobs.tycoon.domain.actions.trading.Assets
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.state.GameState
import jacobs.tycoon.view.components.board.squares.PrimarySelectableDisplay
import jacobs.tycoon.view.components.board.squares.SelectableSecondaryDisplay
import jacobs.tycoon.view.components.board.squares.SquareDisplayStrategy
import jacobs.tycoon.view.components.board.squares.UnavailableDisplay
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class DealController ( kodein: Kodein ) : UserInterfaceController( kodein ){

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()
    private val miscellaneousRules by kodein.instance < MiscellaneousRules > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    // OUTGOING REQUEST API

    fun dealResponse( response: Boolean ) {
        launch {
            outgoingRequestController.respondToTradeOffer( response )
        }
    }

    // TODO: Client-side validation
    fun buildHouses() {
        val streetsSelected = streetsSelectedForHouseBuilding()
        launch { outgoingRequestController.buildHouses(
            streetsSelected,
            streetsSelected.map { clientState.housesToBuild.getValue( it ) }
        ) }
    }

    fun offerTrade() {
        val selectedSquares = clientState.propertiesSelectedForDeal.getValue( DealType.COMPOSING_TRADE )
        playerIdentifier.doIfAPlayerOnThisMachine { offeringPlayer ->
            val offer = TradeOffer(
                offered = Assets(
                    selectedSquares.filter { it.canBeOffered() },
                    clientState.cashOffered,
                    clientState.getOutOfJailFreeCardsOffered
                ),
                wanted = Assets(
                    selectedSquares.filter { it.canBeTradedByCounterparty() },
                    clientState.cashWanted,
                    clientState.getOutOfJailFreeCardsWanted
                ),
                offeringPlayer = offeringPlayer,
                offerRecipient = clientState.playerIntendingToDealWith!!
            )
            launch { outgoingRequestController.offerTrade( offer ) }
        }
    }

    fun payOffMortgages() {
        launch { outgoingRequestController.payOffMortgages(
            clientState.propertiesSelectedForDeal.getValue( DealType.PAYING_OFF_MORTGAGES )
                .filter { it.isMortgaged() }
        ) }
    }

    fun sellHouses() {
        val streetsSelected = streetsSelectedForHouseSelling()
        launch { outgoingRequestController.sellHouses(
            streetsSelected,
            streetsSelected.map { clientState.housesToSell.getValue( it ) }
        ) }
    }

    fun takeOutMortgages() {
        launch { outgoingRequestController.takeOutMortgages(
            clientState.propertiesSelectedForDeal.getValue( DealType.MORTGAGING )
                .filter { it.canBeMortgaged() }
        ) }
    }

    // CLIENT CHANGING STATE API

    fun choosePlayerIntendingToDealWith( player: Player ) {
        this.clientState.playerIntendingToDealWith = player
    }

    /**
     * We put properties indiscriminately into the selected store, and rely on validation on the way out to
     * ensure the correct result is displayed on the screen and passed to the server
     */
    fun handleClickOnProperty( property: Property ) {
        clientState.propertiesSelectedForDeal.getValue( clientState.dealType )
            .apply {
                if ( contains( property ) )
                    remove( property )
                else
                    add( property )
            }
    }

    fun hideComposingDeal() {
        this.clientState.isComposingDeal = false
    }

    fun updateCashOffered( cashOfferedAsText: String ) {
        cashOfferedAsText.toIntOrNull()
            ?.let { clientState.cashOffered = it.toCurrency() }
    }

    fun updateCashWanted( cashWantedAsText: String ) {
        cashWantedAsText.toIntOrNull()
            ?.let { clientState.cashWanted = it.toCurrency() }
    }

    fun updateDealTypeBeingComposed( dealType: DealType ) {
        clientState.dealType = dealType
    }

    fun updateGetOutOfJailFreeCardsOffered( cardsOffered: Double ) {
        clientState.getOutOfJailFreeCardsOffered = cardsOffered.toInt()
    }

    fun updateGetOutOfJailFreeCardsWanted( cardsOffered: Double ) {
        clientState.getOutOfJailFreeCardsWanted = cardsOffered.toInt()
    }

    fun updateHousesToBuildOnStreet( street: Street, count: Double ) {
        clientState.housesToBuild.put( street, count.toInt() )
    }

    fun updateHousesToSellOnStreet( street: Street, count: Double ) {
        clientState.housesToBuild.put( street, count.toInt() )
    }

    // CLIENT QUERYING STATE API

    fun getCashOffered(): String {
        return this.clientState.cashOffered.amount.toString()
    }

    fun getCashWanted(): String {
        return this.clientState.cashWanted.amount.toString()
    }

    fun getDealingSquareDisplayStrategy( square: Square ): SquareDisplayStrategy {
        return when ( clientState.dealType ) {
            DealType.BUILD_HOUSES -> this.getBuildHousesDisplayStrategy( square )
            DealType.COMPOSING_TRADE -> this.getComposingTradeDisplayStrategy( square )
            DealType.MORTGAGING -> this.getMortgagingDisplayStrategy( square )
            DealType.PAYING_OFF_MORTGAGES -> this.getPayingOffMortgagesDisplayStrategy( square )
            DealType.SELL_HOUSES -> this.getSellHousesDisplayStrategy( square )
        }
    }

    fun getDealType(): DealType {
        return this.clientState.dealType
    }

    fun getGetOutOfJailFreeCardsOffered(): Int {
        return this.clientState.getOutOfJailFreeCardsOffered
    }

    fun getGetOutOfJailFreeCardsWanted(): Int {
        return this.clientState.getOutOfJailFreeCardsWanted
    }

    fun getMaxCardsCanOffer(): Int {
        return playerIdentifier.doIfAPlayerOnThisMachine( 0 ) {
            it.howManyGetOutOfJailFreeCards()
        }
    }

    fun getMaxCardsCanWant(): Int {
        return clientState.playerIntendingToDealWith
            ?.howManyGetOutOfJailFreeCards()
            ?: 0
    }

    fun getOfferThatHasBeenMade(): TradeOffer {
        return gameState.game().getOfferBeingConsidered()
    }

    fun housesToBuildOnStreet( street: Street ): Int {
        return clientState.housesToBuild.get( street ) ?: 0
    }

    fun housesToSellOnStreet( street: Street ): Int {
        return clientState.housesToSell.get( street ) ?: 0
    }

    fun isDealTypeBeingComposed( dealType: DealType ): Boolean {
        return clientState.dealType == dealType
    }

    fun isPlayerComposingDeal(): Boolean {
        return this.clientState.isComposingDeal
    }

    fun isPlayerIntendingToDealWith( player: Player ): Boolean {
        return this.clientState.playerIntendingToDealWith == player
    }

    fun isSelected( property: Property ): Boolean {
        return clientState.propertiesSelectedForDeal.getValue( clientState.dealType )
            .contains( property )
    }

    fun maxHousesCanBuildOnStreet( street: Street ): Int {
        return miscellaneousRules.housesToAHotel - street.numberOfHousesBuilt
    }

    fun maxHousesCanSellOnStreet( street: Street ): Int {
        return street.numberOfHousesBuilt
    }

    fun < T > mapOtherPlayers( callback: ( Player ) -> T ): List < T > {
        return this.playerIdentifier.mapOtherPlayers( callback )
    }

    fun streetsSelectedForHouseBuilding(): List < Street > {
        return clientState.propertiesSelectedForDeal.getValue( DealType.BUILD_HOUSES )
            .filterIsInstance < Street > ()
            .filter { it.hasScopeForDevelopment() }
    }

    fun streetsSelectedForHouseSelling(): List < Street > {
        return clientState.propertiesSelectedForDeal.getValue( DealType.SELL_HOUSES )
            .filterIsInstance < Street > ()
            .filter { it.hasAnyDevelopment() }
    }

    // PRIVATE VALIDITY API

    private fun getBuildHousesDisplayStrategy( square: Square ): SquareDisplayStrategy {
        return when {
            square is Street && square.hasScopeForDevelopment() -> PrimarySelectableDisplay( this, square )
            else -> UnavailableDisplay
        }
    }

    private fun getComposingTradeDisplayStrategy( square: Square ): SquareDisplayStrategy {
        return when {
            square is Property == false -> UnavailableDisplay
            square.canBeOffered() -> PrimarySelectableDisplay( this, square )
            square.canBeTradedByCounterparty() -> SelectableSecondaryDisplay( this, square )
            else -> UnavailableDisplay
        }
    }

    private fun getMortgagingDisplayStrategy( square: Square ): SquareDisplayStrategy {
        return when {
            square is Property == false -> UnavailableDisplay
            square.canBeMortgagedHere() -> PrimarySelectableDisplay( this, square )
            else -> UnavailableDisplay
        }
    }

    private fun getPayingOffMortgagesDisplayStrategy( square: Square ): SquareDisplayStrategy {
        return when {
            square is Property == false -> UnavailableDisplay
            square.mortgagedCanBePaidOffHere() -> PrimarySelectableDisplay( this, square )
            else -> UnavailableDisplay
        }
    }

    /**
     * We don't worry at this stage about even development.
     */
    private fun getSellHousesDisplayStrategy( square: Square ): SquareDisplayStrategy {
        return when {
            square is Street && square.hasOwnedDevelopment() -> PrimarySelectableDisplay( this, square )
            else -> UnavailableDisplay
        }
    }

    /**
     * Extension functions to validate squares
     *
     * These are written for more specific receiver types as to be more general would lose smart casting
     * ability in calling functions
     */

    private fun Property.canBeOffered(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse { player -> player.canTrade( this ) }
    }

    private fun Property.canBeTradedByCounterparty(): Boolean {
        return clientState.playerIntendingToDealWith
            ?.canTrade( this )
            ?: false
    }

    private fun Property.canBeMortgagedHere(): Boolean {
        return this.isOwnedByThisPlayer() && this.isMortgaged() == false
    }

    private fun Street.hasOwnedDevelopment(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse {
            this.hasOwnedDevelopment( it )
        }
    }

    private fun Street.hasScopeForDevelopment(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse {
            hasScopeForDevelopmentBy( it )
        }
    }

    private fun Property.mortgagedCanBePaidOffHere(): Boolean {
        return this.isOwnedByThisPlayer() && this.isMortgaged()
    }

    private fun Property.isOwnedByThisPlayer(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse { player -> player.owns( this ) }
    }

}