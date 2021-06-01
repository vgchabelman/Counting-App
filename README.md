# Cornershop Android Development Test

## How to run
App built using Android Studio 4.2.1
No special configurations necessary, runs on Android Emulators running version 5+.
Just install the server and run beforehand
```
$ npm install
$ npm start
```

## The test
App for counting things in general. Setup to work with localhost on Android emulators.

Features:
- Creating and deleting new counters.
- Updating the counters.
- Seeing your counters offline.
- Sharing a counter list.
- Delete multiple counters at once.

## The code

### Project Architecture
Built using Clean Architecture in mind, app uses model States to control the UI, as per the MVI pattern. 
Explanation of each layer:
- App: Knows all other layers, applies DataInjection through Koin.
- Presentation: Holds the UI of the app, also handling state changes through ViewModel classes. Activities observe LiveData objects of the possible States and react accordingly.
- Domain: Pure Kotlin module, is known by every other layer. Holds the Counter model and the other Counter States. Use cases handle bussiness logic.
- Data: Layer handling repository pattern. Implements interfaces found on Domain and handle getting data from api or cache.
- Remote: Holds DTOs for the API calls and transform them back to Counters. Remote access is done through Retrofit.
- Local: Uses Room to hold data for offline access. 
All asynchronous calls use Coroutines for simplicity. 

### Libraries used
- Requests: [Retrofit](https://square.github.io/retrofit/)
- Persistance Storage: [Room](https://developer.android.com/jetpack/androidx/releases/room)
- Dependecy Injection: [Koin](https://insert-koin.io/)
- Unit Tests: [JUnit 4](https://junit.org/junit4/javadoc/latest/overview-summary.html)
- Mocking Tests: [Mockito Kotlin 2](https://mvnrepository.com/artifact/com.nhaarman.mockitokotlin2/mockito-kotlin)
- Custom SearchView: [Floating Search View: ](https://github.com/tom5079/FloatingSearchView)

## Automated Tests
For convenience, fastlane is added to the project. Just go to android project root folder and run
```
$ bundle install
$ bundle exec fastlane test
```
Or try
```
$ ./gradlew test
```

## What comes next
Check out the [Projects](https://github.com/vgchabelman/Counting-App/projects) tab in GitHub to see the project next steps