package jacobs.tycoon.domain

import jacobs.tycoon.domain.services.auction.Auctioneer
import jacobs.tycoon.domain.services.auction.DumbAuctioneer
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun clientDomainModule(): Kodein.Module {
    return Kodein.Module( "clientDomain" ) {
        bind < Auctioneer >() with singleton { DumbAuctioneer( kodein ) }
    }
}
