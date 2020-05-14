package jacobs.tycoon.domain.services.auction

import org.kodein.di.Kodein

/**
 * Supplied for clients who are relying on server to monitor auction, but need to plug gap in domain
 * calls for such monitoring
 */
class DumbAuctioneer( kodein: Kodein ) : Auctioneer( kodein ) {

    override fun notifyOfBid() {
        // Nothing to do on client side
    }

    override fun startAuctionTimer() {
        // Nothing to do on client side
    }

}