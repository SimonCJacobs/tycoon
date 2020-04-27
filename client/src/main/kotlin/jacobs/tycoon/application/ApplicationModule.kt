package jacobs.tycoon.application

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton
import org.w3c.dom.Element
import kotlin.browser.document

val applicationModule = Kodein.Module( "application" ) {
    bind < Application >() with singleton { Application( kodein ) }
    bind < CoroutineScope >() with singleton { MainScope() }
    bind < Element >( tag = "main" ) with singleton { document.getElementById( "main" )!! }
}