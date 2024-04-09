# LitKeepAndroid

LitKeep(a bill record app) android app source code.

I like 👍`js` and 👍`reactjs` very much. I am very glad that the emergence of ✨`Jetpack Compose` allows android
development to no longer be subject to poor 👿xml. And 🍦`kotlin` is so nice!. This is a learning project of mine, and I hope it
can really be used for keeping accounts.

## How to

My another repo contains the backend source 🌏[https://github.com/Cufoon/LitKeepService.git](https://github.com/Cufoon/LitKeepService.git)

After you start a backend server, you could create a kotlin file in

```text
./app/src/main/kotlin/cufoon/litkeep/android/service/
```

It should contain content like following:

```kotlin
package cufoon.litkeep.android.service

// please have your address end with /
const val LITKEEP_BACKEND_URL = "https://your-service-domain-address/"
```

Then open your android studio, just have a try! 😄
