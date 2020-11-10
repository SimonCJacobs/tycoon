# Project Tycoon

A multiplayer online Monopoly game

## Running in development

1. Start the server locally ```./gradlew :server:run```
2. Open game by opening HTML file ```build/distributions/index.html``` in browser

## Running in production

1. If necessary, deploy updated client-side build ```./gradlew :client:deploy```
2. Start the server ```./gradlew :server:deploy```
3. Navigate to ```monopolisation.grayv.co.uk``` in a browser