package jacobs.tycoon.servercontroller

import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class IncomingController( kodein: Kodein ) {

    private val serverMainController by kodein.instance < ServerMainController >()



}