package jacobs.websockets.engine

import org.kodein.di.Kodein
import org.kodein.di.erased.instance

/**
 * This class cannot have dependencies on any of the other major classes in the dependency chain
 * (it's like a stand-in for a proxy for OutgoingCommunicationDispatcher really
 */
internal class ResponseDirector( kodein: Kodein ) {

    private val messageIdRepository by kodein.instance < MessageIdRepository > ()
    private lateinit var outgoingResponseDispatchLambda : ( Response ) ->  Unit

    fun dispatchIncomingResponse( response: Response ) {
        if ( false == this.messageIdRepository.isRecognisedResponse( response ) )
            throw Error( "Received unrecognised response. Message: ${ response.content }" )
        this.messageIdRepository.getResponseRouteToOrigin( response )
            .invoke( response.content )
    }

    fun dispatchOutgoingResponse( response: Response ) {
        this.outgoingResponseDispatchLambda( response )
    }

    fun setOutgoingResponsePath( outgoingResponsePath: ( Response ) ->  Unit ) {
        this.outgoingResponseDispatchLambda = outgoingResponsePath
    }

}