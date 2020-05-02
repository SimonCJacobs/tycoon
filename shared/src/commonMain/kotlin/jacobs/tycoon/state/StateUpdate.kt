package jacobs.tycoon.state

import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable

@Serializable
class StateUpdate private constructor() : MessageContent {

    private val updateCallLists: MutableList < List < UpdateCall > > = mutableListOf()

    companion object {
        fun empty(): StateUpdate {
            return StateUpdate()
        }
        fun fromList( updateCallList: List < UpdateCall > ): StateUpdate {
            return StateUpdate()
                .also { it.updateCallLists.add( updateCallList.toList() ) }
        }
    }

    fun combine( otherUpdate: StateUpdate ): StateUpdate {
        this.updateCallLists.addAll( otherUpdate.updateCallLists )
        return this
    }

    fun getUpdates(): List < UpdateCall > {
        return this.updateCallLists.flatten()
    }

    fun isEmpty(): Boolean {
        return this.updateCallLists.isEmpty()
    }

}