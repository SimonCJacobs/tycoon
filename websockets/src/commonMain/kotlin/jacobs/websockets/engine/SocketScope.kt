package jacobs.websockets.engine

import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class SocketScope
    < TParameters : WebSocketParameters < TParameters >, TContext : SocketContext < TParameters > >
    : Scope < TContext >
{

    private val registries: MutableMap < TContext, ScopeRegistry > = mutableMapOf()

    fun deleteRegistry( context: TContext ) {
        this.registries.remove( context )
    }

    override fun getRegistry( context: TContext ): ScopeRegistry {
        if ( this.registries.contains( context ) )
            return this.registries.getValue( context )
        else
            return StandardScopeRegistry()
                .also { this.registries.set( context, it ) }
    }

}