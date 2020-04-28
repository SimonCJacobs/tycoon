package jacobs.websockets.engine

import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class SocketScope < T : WebSocketParameters < T > > : Scope < T > {

    private val registries: MutableMap < T, ScopeRegistry > = mutableMapOf()

    fun deleteRegistry( context: T ) {
        this.registries.remove( context )
    }

    override fun getRegistry( context: T ): ScopeRegistry {
        if ( this.registries.contains( context ) )
            return this.registries.getValue( context )
        else
            return StandardScopeRegistry()
                .also { this.registries.set( context, it ) }
    }

}