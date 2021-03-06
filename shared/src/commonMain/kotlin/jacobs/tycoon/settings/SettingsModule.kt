package jacobs.tycoon.settings

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance

fun settingsModule(): Kodein.Module {
    return Kodein.Module( name = "settings" ) {
        bind < String > ( tag = "gameName" ) with instance( "Monopolisation" )
        bind < String > ( tag = "socketPath" ) with instance( "/" )
        bind < Int > ( tag = "socketPort" ) with instance( 8882 )
        bind < Long > ( tag = "updateDelay" ) with instance( 100L )
    }
}