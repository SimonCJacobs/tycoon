package jacobs.tycoon.domain.phases

interface TurnBasedPhaseVisitor {
    fun visit( acceptingFunds: AcceptingFunds )
    fun visit( auctionProperty: AuctionProperty )
    fun visit( bankruptcyProceedings: BankruptcyProceedings )
    fun visit( cardReading: CardReading )
    fun visit( crownTheVictor: CrownTheVictor )
    fun visit( dealingWithMortgageInterestOnTransfer: DealingWithMortgageInterestOnTransfer )
    fun visit( movingAPiece: MovingAPiece )
    fun visit( payingFineOrTakingCard: PayingFineOrTakingCard )
    fun visit( paymentDue: PaymentDue )
    fun visit( potentialPurchase: PotentialPurchase )
    fun visit( rollingForMove: RollingForMove )
    fun visit( rollingForMoveFromJail: RollingForMoveFromJail )
    fun visit( rollingForOrder: RollingForOrder )
    fun visit( tradeBeingConsidered: TradeBeingConsidered )
}