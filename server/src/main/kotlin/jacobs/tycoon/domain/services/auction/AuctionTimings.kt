package jacobs.tycoon.domain.services.auction

interface AuctionTimings {
    val goingOnceMs: Long
    val goingTwiceMs: Long
    val goneMs: Long
}