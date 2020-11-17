package jacobs.tycoon.application

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

fun applicationModule( mode: ApplicationMode ): Kodein.Module {
    return Kodein.Module( "application" ) {
        bind < Application >() with singleton { Application( kodein ) }
        bind < ApplicationMode >() with instance( mode )
        bind < CoroutineScope >() with singleton { MainScope() }
    }
}