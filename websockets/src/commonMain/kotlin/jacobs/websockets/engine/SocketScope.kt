package jacobs.websockets.engine

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class SocketScope : Scope < SocketContext >
{

    private val registries: MutableMap < SocketContext, ScopeRegistry > = mutableMapOf()

    fun deleteRegistry( context: SocketContext ) {
        this.registries.remove( context )
    }

    override fun getRegistry( context: SocketContext ): ScopeRegistry {
        if ( this.registries.contains( context ) )
            return this.registries.getValue( context )
        else
            return StandardScopeRegistry()
                .also { this.registries.set( context, it ) }
    }

}