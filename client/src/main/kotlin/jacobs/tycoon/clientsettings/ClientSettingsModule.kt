package jacobs.tycoon.clientsettings

import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance

fun clientSettingsModule(): Kodein.Module {
    return Kodein.Module( name = "clientSettings" ) {
        bind < String > ( tag = "adminPath" ) with instance( "admin.html" )
        bind < String > ( tag = "socketHostname" ) with instance( js( "SOCKET_HOSTNAME" ) as String )
    }
}