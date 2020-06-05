const path = require ( "path" )
const webpack = require( "webpack" )

const projectDirectory = path.join( process.cwd(), "../../../.." )

const settingsFile =
    config.mode == "production"
        ? "production-settings.json"
        : "development-settings.json"

const settings = require( path.join( projectDirectory, "settings", settingsFile ) )

const definePlugin = new webpack.DefinePlugin( {
    SOCKET_HOSTNAME: `"${ settings.socketHostname }"`
} )

config.plugins.push( definePlugin )