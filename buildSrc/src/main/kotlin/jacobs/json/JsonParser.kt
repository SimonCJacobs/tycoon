package jacobs.json

import Settings
import com.beust.klaxon.Klaxon
import java.io.File

class JsonParser {

    fun parseSettings( settingsFile: File ): Settings {
        return Klaxon().parse < Settings >( settingsFile.readText() )!!
    }

}