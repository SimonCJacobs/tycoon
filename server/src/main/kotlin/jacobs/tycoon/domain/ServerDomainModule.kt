package jacobs.tycoon.domain

import jacobs.tycoon.domain.services.auction.AuctionTimings
import jacobs.tycoon.domain.services.auction.StandardAuctionTimings
import jacobs.tycoon.domain.services.auction.Auctioneer
import jacobs.tycoon.domain.services.auction.AuctioneerWithHammer
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

fun serverDomainModule(): Kodein.Module {
    return Kodein.Module ( name = "serverState" ) {
        bind < Auctioneer >() with singleton { AuctioneerWithHammer( kodein ) }
        bind < AuctionTimings >() with instance( StandardAuctionTimings )
        bind < GameExecutor > ( tag = "wrapper" ) with singleton { GameExecutorWrapper( kodein ) }
    }
}