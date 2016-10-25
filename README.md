#IcyNote

IcyNote is a clean coded, highly decoupled note taking app for android.

##Test account

- You can login using this account : 
- email : `test@icynote.ch`
- password : `icynote`

##Code: How to access the core ?

- use `icynote.core.impl.CoreSingleton.getCore()` to obtain the current core instance
- this instance is created when the user logs in (already implemented)
- see the class `icynote.core.impl.CoreSingleton` for more details.

##Code: Authentication

- User authentication either with *Google Sign In* or with email/password combination
- Google's token get translated to internal *Uuid* through [Firebase Auth](https://firebase.google.com)
- The internal Uuid will be used in the core to retrieve user's notes and media

###Warnings

DO NOT PUBLICLY SHARE SECRET KEYS

- `/ debug.keystore` 
 - do not share
 - should be copied into `~/.android/debug.keystore`
- `app/ google-services.json`
 - DO NOT SHARE
