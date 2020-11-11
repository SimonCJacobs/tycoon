package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.EntryPageController
import jacobs.tycoon.clientstate.EntryPageState
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.VNode
import org.js.mithril.selectedIndex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.HTMLInputElement

class EntryPage( kodein: Kodein ) : Page {

    private val controller by kodein.instance < EntryPageController > ()
    private val pageState by kodein.instance < EntryPageState > ()

    override fun view(): VNode {
        return when {
            this.controller.isClientWaitingForServer() -> this.displayWaitingScreen()
            else -> this.getPlayerEntryForm()
        }
    }

    private fun displayWaitingScreen(): VNode {
        return m( Tag.h1 ){ content( "Waiting for server" ) }
    }

    private fun getPlayerEntryForm() : VNode {
        return m( Tag.div ) {
            children(
                getPlayerEntryFields(),
                getSubmitButton(),
                getAnyMessage()
            )
        }
    }

    private fun getPlayerEntryFields() : VNode {
        return m( Tag.form ) {
            children(
                getPlayerNameEntry(),
                getPlayerPieceSelection()
            )
        }
    }

    @Suppress( "unused" )
    private fun getPlayerNameEntry(): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.label ) {
                    content( "What's your name?" )
                },
                m( Tag.input ) {
                    attributes ( object {
                        val type = "text"
                        val value = pageState.playerNameInProgress
                    } )
                    eventHandlers {
                        onInputExt = { pageState.playerNameInProgress = ( it.target as HTMLInputElement ).value }
                    }
                }
            )
        }
    }

    private fun getPlayerPieceSelection(): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.label ) {
                    content( "What piece would you like?" )
                },
                getPieceDropdown()
            )
        }
    }

    private fun getPieceDropdown(): VNode {
        pageState.pieceOptionList = this.controller.getAvailablePieces()
        pageState.selectedPiece = this.controller.getSelectedPiece( pageState.pieceOptionList )
        return m( Tag.select ) {
            eventHandlers {
                onInputExt = selectedIndex { pageState.selectedPiece = pageState.pieceOptionList[ it ] }
            }
            children(
                pageState.pieceOptionList.map { getSinglePieceOptionElement( it, pageState.selectedPiece!! ) }
            )
        }
    }

    @Suppress( "unused" )
    private fun getSinglePieceOptionElement( thisPiece: PlayingPiece, selectedPiece: PlayingPiece ): VNode {
        return m( Tag.option ) {
            attributes ( object {
                val value = thisPiece.name
                val selected = if ( thisPiece == selectedPiece ) "selected" else ""
            } )
            content( thisPiece.name )
        }
    }

    @Suppress( "unused" )
    private fun getSubmitButton(): VNode {
        return m( Tag.button ) {
            attributes ( object {
                val type = "button"
            } )
            eventHandlers {
                onclick = { controller.onEntryPageButtonClick() }
            }
            content( "Play the game already!" )
        }
    }

    private fun getAnyMessage(): VNode? {
        if ( pageState.showNoGameEntry )
            return m( Tag.h4 ) {
                content( "Sorry. You can't join the game now" )
            }
        return null
    }

}