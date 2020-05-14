package jacobs.tycoon.services.speechsynthesis

external interface SpeechSynthesisVoice {
    val default: Boolean
    val localService:  Boolean
    val name: String
}
