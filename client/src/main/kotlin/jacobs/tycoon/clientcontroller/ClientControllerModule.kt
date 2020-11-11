package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.players.Player
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.eagerSingleton
import org.kodein.di.erased.factory
import org.kodein.di.erased.singleton

fun clientControllerModule(): Kodein.Module {
    return Kodein.Module( "clientController" ) {
        bind < AdminController > () with singleton { AdminController( kodein ) }
        bind < AuctionController > () with eagerSingleton { AuctionController( kodein ) }
        bind < BoardController > () with singleton { BoardController( kodein ) }
        bind < ChangeListener > () with singleton { ChangeListener( kodein ) }
        bind < DealController > () with singleton { DealController( kodein ) }
        bind < DiceController > () with singleton { DiceController( kodein ) }
        bind < EntryPageController > () with singleton { EntryPageController( kodein ) }
        bind < IncomingController > () with singleton { IncomingController( kodein ) }
        bind < MainPageController > () with singleton { MainPageController( kodein ) }
        bind < OutgoingRequestController > () with singleton { OutgoingRequestController( kodein ) }
        bind < PieceController > () with singleton { PieceController( kodein ) }
        bind < PlayerActionController > () with
            factory { player: Player -> PlayerActionController( kodein, player ) }
        bind < SquareController > () with singleton { SquareController( kodein ) }
        bind < StateSynchroniser > () with singleton { StateSynchroniser( kodein ) }
        bind < UserInterfaceController > () with singleton { UserInterfaceController( kodein ) }
    }
}