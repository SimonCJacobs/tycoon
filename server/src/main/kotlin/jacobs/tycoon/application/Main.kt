package jacobs.tycoon.application

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.selects.selectUnbiased

@ExperimentalCoroutinesApi
fun main( args: Array < String > ) {
    val coroutineScope = CoroutineScope( Dispatchers.IO )
    val messageProducer = coroutineScope.produce < String > {
        while ( true ) {
            println( "Start producer cycle" )
            send( "Sock me baby" )
            println( "Producer sent" )
            delay( 1000 )
            println( "done waiting" )
        }
    }
    val server = embeddedServer( Netty, 8080 ) {
        install( CORS ) {
            method( HttpMethod.Get )
            anyHost()
        }
        install( WebSockets )
        routing {
            get( "/" ) {
                call.respondText( "\"Hello, worldy unbebeeb!\"", ContentType.Application.Json )
            }
            webSocket( "/socket" ) {
                while ( true ) {
                    selectUnbiased < Unit > {
                        messageProducer.onReceive {
                            send( Frame.Text( it ) )
                        }
                        incoming.onReceive {
                            println( ( it as Frame.Text ).readText() )
                        }
                    }
                }
            }
        }
    }
    server.start( wait = true )
}