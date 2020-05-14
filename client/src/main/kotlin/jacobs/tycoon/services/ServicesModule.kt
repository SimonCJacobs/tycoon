package jacobs.tycoon.services

import jacobs.tycoon.services.speechsynthesis.VoiceSynthesiser
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

fun servicesModule(): Kodein.Module {
    return Kodein.Module( "clientServices" ) {
        bind < ActionWriter >() with singleton { ActionWriter( kodein ) }
        bind < Network > () with singleton { Network( kodein ) }
        bind < PlayerIdentifier > () with singleton { PlayerIdentifier( kodein ) }
        bind < VoiceSynthesiser > () with singleton { VoiceSynthesiser() }
    }
}