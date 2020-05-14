package jacobs.tycoon.services.speechsynthesis

import org.w3c.dom.events.EventTarget

external class SpeechSynthesisUtterance( text: String ) : EventTarget {
    var text: String
    var voice: SpeechSynthesisVoice
    var volume: Float
}