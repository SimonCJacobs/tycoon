package jacobs.tycoon.domain.services.auction

enum class AuctionPhase {
    BIDDING_STARTED,
    BID_MADE,
    GOING_ONCE,
    GOING_TWICE,
    SOLD,
    UNSOLD;

    fun toWords(): String {
        return when ( this ) {
            BIDDING_STARTED -> "Bidding started"
            BID_MADE -> "Bid made"
            GOING_ONCE -> "Going once"
            GOING_TWICE -> "Going twice"
            SOLD -> "Sold"
            UNSOLD -> "Unsold"
        }
    }
}