package jacobs.tycoon.view

import jacobs.tycoon.view.components.board.BoardComponent
import jacobs.tycoon.view.components.board.SquareComponentFactory
import jacobs.tycoon.view.components.console.Console
import jacobs.tycoon.view.components.pages.EntryPage
import jacobs.tycoon.view.components.pages.MainPage
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

val viewModule = Kodein.Module( "view" ) {
    bind < BoardComponent >() with singleton { BoardComponent( kodein ) }
    bind < Console >() with singleton { Console( kodein ) }
    bind < EntryPage >() with singleton { EntryPage( kodein ) }
    bind < MainPage >() with singleton { MainPage( kodein ) }
    bind < PageWrapper >() with singleton { PageWrapper( kodein ) }
    bind < SquareComponentFactory >() with singleton { SquareComponentFactory() }
    bind < View >() with singleton { View( kodein ) }
}