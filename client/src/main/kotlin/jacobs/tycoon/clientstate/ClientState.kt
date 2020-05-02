package jacobs.tycoon.clientstate

import jacobs.tycoon.view.ViewState

class ClientState {
    var isWaitingForServer = true
    var viewState: ViewState = ViewState.SIGN_UP
}