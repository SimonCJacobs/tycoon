package jacobs.tycoon.domain.services.auction

import jacobs.tycoon.domain.GameExecutor
import jacobs.tycoon.domain.actions.auction.AuctionNotification
import jacobs.tycoon.domain.actions.auction.ConcludeAuction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class AuctioneerWithHammer( kodein: Kodein ) : Auctioneer( kodein ) {

    private val auctionTimings by kodein.instance < AuctionTimings >()
    private val coroutineScope by kodein.instance < CoroutineScope > ()
    private val gameExecutor by kodein.instance < GameExecutor > ( tag = "wrapper" )

    private var timerJob: Job? = null

    override fun notifyOfBid() {
        this.coroutineScope.launch { notifyAuctionOfPhaseChange( AuctionPhase.BID_MADE ) }
        this.restartTimer()
    }

    override fun startAuctionTimer() {
        this.restartTimer()
    }

    private fun restartTimer() {
        this.timerJob?.apply { cancel() }
        this.timerJob = this.coroutineScope.launch { auctionTimer() }
    }

    private suspend fun auctionTimer() {
        delay( auctionTimings.goingOnceMs )
        this.notifyAuctionOfPhaseChange( AuctionPhase.GOING_ONCE )
        delay( auctionTimings.goingTwiceMs )
        this.notifyAuctionOfPhaseChange( AuctionPhase.GOING_TWICE )
        delay( auctionTimings.goingTwiceMs )
        this.completeAuction()
    }

    private suspend fun notifyAuctionOfPhaseChange( auctionPhase: AuctionPhase ) {
        this.gameExecutor.execute( AuctionNotification( auctionPhase ) )
    }

    private suspend fun completeAuction() {
        this.gameExecutor.execute(ConcludeAuction())
    }

}
