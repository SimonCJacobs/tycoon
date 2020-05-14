package jacobs.tycoon.domain.services.auction

object StandardAuctionTimings : AuctionTimings {
    override val goingOnceMs: Long = 12000
    override val goingTwiceMs: Long = 5000
    override val goneMs: Long = 5000
}