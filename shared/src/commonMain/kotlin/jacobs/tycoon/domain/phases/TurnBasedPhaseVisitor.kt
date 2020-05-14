package jacobs.tycoon.domain.phases

interface TurnBasedPhaseVisitor {
    fun visit( auctionProperty: AuctionProperty )
    fun visit( bankruptcyProceedings: BankruptcyProceedings )
    fun visit( cardReading: CardReading )
    fun visit( crownTheVictor: CrownTheVictor )
    fun visit( movingAPiece: MovingAPiece )
    fun visit( potentialPurchase: PotentialPurchase )
    fun visit( potentialRentCharge: PotentialRentCharge )
    fun visit( rollingForMove: RollingForMove )
    fun visit( rollingForOrder: RollingForOrder )
    fun visit( tradeBeingConsidered: TradeBeingConsidered )
}