package jacobs.tycoon.services

import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.gameadmin.AddPlayer
import jacobs.tycoon.domain.actions.cards.ReadCard
import jacobs.tycoon.domain.actions.gameadmin.CompleteSignUp
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.auction.AuctionBid
import jacobs.tycoon.domain.actions.auction.AuctionNotification
import jacobs.tycoon.domain.actions.auction.ConcludeAuction
import jacobs.tycoon.domain.actions.cards.PlayGetOutOfJailFreeCard
import jacobs.tycoon.domain.actions.gameadmin.NewGame
import jacobs.tycoon.domain.actions.moving.PieceMoved
import jacobs.tycoon.domain.actions.property.RentCharge
import jacobs.tycoon.domain.actions.property.RespondToPropertyOffer
import jacobs.tycoon.domain.actions.moving.RollForMove
import jacobs.tycoon.domain.actions.moving.RollForOrderAction
import jacobs.tycoon.domain.actions.gameadmin.SetBoard
import jacobs.tycoon.domain.actions.gameadmin.SetPieces
import jacobs.tycoon.domain.actions.property.Build
import jacobs.tycoon.domain.actions.property.MortgageProperty
import jacobs.tycoon.domain.actions.property.PayOffMortgage
import jacobs.tycoon.domain.actions.property.SellBuildings
import jacobs.tycoon.domain.actions.results.MoveOutcome
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForOrderOutcome
import jacobs.tycoon.domain.actions.trading.OfferTrade
import jacobs.tycoon.domain.actions.trading.RespondToTradeOffer
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.domain.services.auction.AuctionPhase
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ActionWriter( kodein: Kodein ) : ActionProcessor < String >, ActionVisitor < String? > {

    private val gameName by kodein.instance < String > ( tag = "gameName" )
    private val playerIdentifier by kodein.instance < PlayerIdentifier > ()

    override fun process( gameAction: GameAction ): String? {
        if ( false == gameAction.successful )
            return null
        return gameAction.accept( this )
    }

    override fun visit( addPlayer: AddPlayer): String {
        return "Player ${ addPlayer.playerName } has joined using piece ${ addPlayer.playingPiece.name }"
    }

    override fun visit( auctionBid: AuctionBid ): String? {
        // Not used. The players get good information elsewhere during the auction.
        return null
    }

    override fun visit( auctionNotification: AuctionNotification ): String? {
        // Not used. The players get good information elsewhere during the auction.
        return null
    }

    override fun visit( build: Build ): String {
        return "Nice new pads you have there ${ build.actorPosition.name() }"
    }

    override fun visit( completeSignUp: CompleteSignUp): String {
        return "The game sign-up stage is complete. Let's roll the dice to see the order of play :)"
    }

    override fun visit( concludeAuction: ConcludeAuction ): String {
        if ( concludeAuction.status.areThereBids() )
            return "It's all over. Sold for ${ concludeAuction.status.leadingBidNotNull } to " +
                "${ concludeAuction.status.leadingBidderNotNull.name }. Hearty congratulations"
        else
            return "Well that's disappointing. No-one wanted it :( Let's move along now"
    }

    override fun visit( mortgageProperty: MortgageProperty ): String {
        return "Tough times eh? ${ mortgageProperty.actorPosition.name() } mortgaged " +
            "${ mortgageProperty.property }"
    }

    override fun visit( newGame: NewGame): String {
        return "A new game of $gameName has begun!"
    }

    override fun visit( offerTrade: OfferTrade ): String {
        return "Hang on a sec! ${ offerTrade.actorPosition.name() } offered a trade to " +
            offerTrade.tradeOffer.offerRecipient.name
    }

    override fun visit( payOffMortgage: PayOffMortgage ): String {
        return "Well done, ${ payOffMortgage.actorPosition.name() }. That's " +
            "${ payOffMortgage.property.name } back in play "
    }

    override fun visit( pieceMoved: PieceMoved): String {
        return "Welcome to ${ pieceMoved.result.destinationSquare.name }, ${ pieceMoved.actorPosition.name() }! " +
            this.getSnippetForMoveOutcome( pieceMoved.result.outcome )
    }

    private fun getSnippetForMoveOutcome( moveOutcome: MoveOutcome ): String {
        return when ( moveOutcome ) {
            MoveOutcome.CARD_READING -> "Please take a card"
            MoveOutcome.FREE_PARKING -> "Rest a wee while"
            MoveOutcome.GO_TO_JAIL -> "Time to straighten you out. In the name of God, go!"
            MoveOutcome.JAIL -> "Let this be a lesson to you"
            MoveOutcome.JUST_VISITING -> "\"Just visiting\", eh?"
            MoveOutcome.ON_GO_SQUARE -> "Woo hoo!"
            MoveOutcome.ON_MORTGAGED_PROPERTY -> "This property is mortgaged. No charge for this one"
            MoveOutcome.ON_OWN_PROPERTY -> "Oh! This looks familiar. You putting on the kettle?"
            MoveOutcome.POTENTIAL_PURCHASE -> "Would you like to buy it?"
            MoveOutcome.POTENTIAL_RENT -> ""
            MoveOutcome.TAX -> "You have a bill to pay"
        }
    }

    override fun visit( playGetOutOfJailFreeCard: PlayGetOutOfJailFreeCard ): String {
        return "Where did you get that nugget? Ok, you are free to go."
    }


    override fun visit( readCard: ReadCard): String {
        return "The card said: \"${ readCard.result.cardText }\""
    }

    override fun visit( respondToPropertyOffer: RespondToPropertyOffer): String {
        return when ( respondToPropertyOffer.decidedToBuy ) {
            true -> "Congratulations on your purchase, ${ respondToPropertyOffer.actorPosition.name() }. " +
                "May it bring you much happiness"
            false -> "Something wrong with the goods, pal? Ok, it's going under the hammer"
        }
    }

    override fun visit( respondToTradeOffer: RespondToTradeOffer ): String {
        val response = if ( respondToTradeOffer.response )
            " YES! Let's switch things around."
        else
            "... no. As you were then."
        return "And the answer is:${ response }"
    }

    override fun visit( rollForOrder: RollForOrderAction ): String {
        return "${ rollForOrder.actorPosition.name() } rolled " +
            "${ rollForOrder.result.diceRoll.inWordAndNumber() }. " + this.getRollForOrderAddendum( rollForOrder )
    }

    private fun getRollForOrderAddendum( rollForOrder: RollForOrderAction ): String {
        return when( rollForOrder.result.nextPhase ) {
            RollForOrderOutcome.ROLLING -> "Let's keep rolling."
            RollForOrderOutcome.COMPLETE ->
                "So... ${ rollForOrder.result.winner.name } got the highest and will start the game"
            RollForOrderOutcome.ROLL_OFF -> "It's a roll off!"
        }
    }

    override fun visit( rollForMove: RollForMove): String {
        return "${ rollForMove.actorPosition.name() } rolled " +
            "${ rollForMove.result.diceRoll.inWordAndNumber() }. " + this.getRollForMoveAddendum( rollForMove )
    }

    private fun getRollForMoveAddendum( rollForMove: RollForMove): String {
        return when( rollForMove.result.outcome ) {
            RollForMoveOutcome.GO_TO_JAIL -> "That's a third double in a row... which means some time in the can."
            RollForMoveOutcome.MOVE_TO_SQUARE -> this.getRandomMovingPhrase()
            RollForMoveOutcome.BANKRUPTCY_PROCEEDINGS -> "Uh oh. Your time's up"
            RollForMoveOutcome.REMAIN_IN_JAIL -> "You stay where you are, pal"
        }
    }

    private fun getRandomMovingPhrase(): String {
        return listOf( "On yer bike!", "Well what are you waiting for?", "Moving time.", "Chop chop",
            "Please proceed to your destination", "Start those engines" ).random()
    }

    override fun visit( rentCharge: RentCharge): String {
        return "That'll be ${ rentCharge.result.rentDue } please for your visit. Ain't a free country you know"
    }

    override fun visit( sellBuildings: SellBuildings ): String {
        return "Fair enough, ${ sellBuildings.actorPosition.name() }. Hope you find something nice to " +
            "spend the money on"
    }

    override fun visit( setBoard: SetBoard): String {
        return "Using board with locations in ${ setBoard.board.location }"
    }

    override fun visit( setPieces: SetPieces): String {
        return "Using \"${ setPieces.pieceSet.name }\" piece set"
    }

    private fun SeatingPosition.name(): String {
        return playerIdentifier.getPlayerFromSeatingPosition( this ).name
    }

}