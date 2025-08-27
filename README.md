# Vicinity KMP
Compose Multiplatform MultiModule Project to Help User Find Events (KMP - Android, IOS, Desktop)

Sample Project to re-use checkpoint for future KMP Projects


## Features
* Display Paged Events Based On Location
* Search Events Based On Location And Keyword
* Like Any Event, Syncing Likes in Real time across devices
* Share Events Between Users - Deeplink on Android And IOS
* Login, Sign Up and Delete Account
* Filter and Sort Events Based On Classification and Dates
* Display Real time charts of trend events based on user likes
* Display Events Happening iN Location Based On Visible Map Bounds
* Update User Profile Data (Picture, bio, name, password) syncing in Real time across devices

## Architecture - MVVM/MVI
## Tools
* Jetpack Compose - Compose Multiplatform [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
* Supabase for auth and real time database [Supabase](https://github.com/supabase-community/supabase-kt)
* Firebase storage and Crashlytics [Firebase](https://github.com/GitLiveApp/firebase-kotlin-sdk)
* Compass [Compass](https://github.com/jordond/compass)
* Connectivity [Connectivity](https://github.com/jordond/connectivity)
* Github Actions [Github Actions](https://github.com/features/actions)
* SQLDelight [SQLDelight](https://github.com/sqldelight/sqldelight)
* Multiplatform Settings [Settings](https://github.com/russhwolf/multiplatform-settings)
* Map Compose [Map](https://github.com/p-lr/MapComposeMP)
* Koin [Koin](https://github.com/InsertKoinIO/koin)
* Paging3 [SQLDelight and Google Paging](https://sqldelight.github.io/sqldelight/2.1.0/2.x/extensions/androidx-paging3/app.cash.sqldelight.paging3/index.html)
* FlagKit [Flagkit](https://github.com/acarlsen/kmp-flagkit)
* Coil [Coil](https://github.com/coil-kt/coil)
* Filekit [FileKit](https://github.com/vinceglb/FileKit)
* Koala Charts [Koala](https://github.com/KoalaPlot/koalaplot-core)
* Calendar [Calendar](https://github.com/kizitonwose/Calendar)
* Android GeoHash [Android GeoHash](https://github.com/drfonfon/android-kotlin-geohash)
* JVM GeoHash [JVM Geohash](https://github.com/kungfoo/geohash-java)
* Shimmer [Shimmer](https://github.com/valentinilk/compose-shimmer)

<img width="1589" height="948" alt="Screenshot 2025-08-27 at 17 21 45" src="https://github.com/user-attachments/assets/ff81b22d-4857-4165-8307-46d46fb35305" />

### Event Detail
https://github.com/user-attachments/assets/bb962a28-b65b-424e-8ef5-7ebc5d5ca509

### Realtime Likes
https://github.com/user-attachments/assets/84d2b361-3a05-4335-b5a4-d6b2740a44ba


### Realtime Profile Update
https://github.com/user-attachments/assets/fb6239cf-7a8f-4173-b838-492db2b202d7


### Realtime Trending
https://github.com/user-attachments/assets/c8c2cdd5-f4f7-4dac-a701-a90385996282

### Share and Deeplink(IOS and Android)
https://github.com/user-attachments/assets/29d59c3c-bdc5-44fd-b53b-6013fadee622

### Search and Filter Events
https://github.com/user-attachments/assets/3b21656f-75bb-43e1-ba59-31b08235acc9


### Global Map
https://github.com/user-attachments/assets/b563fda1-7203-456e-97ad-a1c397c6f248

### Log Out and Input Validation
https://github.com/user-attachments/assets/852638e2-09bf-48de-8ea9-6642e24f9208





## Important Notice
This project requires a specific Supabase database schema to function properly. Simply adding API keys will not be sufficient to run the project locally, as you would need to recreate the exact database tables and structure.
However these are the breakdown of the constants in local.properties

# Ticketmaster API Configuration
base.url=https://app.ticketmaster.com/
api.key=YOUR_TICKETMASTER_API_KEY
tmapi.key=YOUR_TICKETMASTER_API_KEY

# LocationIQ API Configuration  
liq.base.url=https://api.locationiq.com/
liq.api.key=YOUR_LOCATIONIQ_API_KEY

# IP Geolocation API
ip.base.url=http://ip-api.com/json/

# Google Configuration
MAP_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
api.secret=YOUR_GOOGLE_MAPS_SECRET
client.id=YOUR_GOOGLE_NATIVE_CLIENT_ID

# Firebase Configuration
project.id=YOUR_FIREBASE_PROJECT_ID
storage.url=YOUR_FIREBASE_STORAGE_BUCKET_URL

# Supabase Configuration (Auth, Realtime, PostgREST)
supabase.url=YOUR_SUPABASE_URL
supabase.api.key=YOUR_SUPABASE_API_KEY
















