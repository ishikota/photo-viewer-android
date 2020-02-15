# photo-viewer-android
Simple photo viewer app supporting these features

- List items with infinite scroll
- Show details, Like/Unlike an item
- Search items
- Login(Oauth2)

<img src="screenshots/app.gif" width="300">

| List | Photo Detail | Collection Detail | UserDetail | Search |
|:--:|:--:|:--:|:--:|:--:|
| <img src="screenshots/top.gif" width="150" /> | <img src="screenshots/detail.gif" width="150" /> |  <img src="screenshots/detail2.gif" width="150" /> | <img src="screenshots/user.gif" width="150" /> | <img src="screenshots/search.gif" width="150" /> |

# Characteristics
- Architecture
    - MVVM architecture using [Android Jetpack](https://developer.android.com/jetpack)
        - LiveData, ViewModel, DataBinding, Paging
    - single-activity using [Navigation Coomponent](https://developer.android.com/guide/navigation)
- Tech-stack
    - [Dagger2](https://github.com/google/dagger) - dependency injection
    - [Retrofit](https://github.com/square/retrofit) - networking
    - [RxJava2](https://github.com/ReactiveX/RxJava) - background operation
    - [Timber](https://github.com/JakeWharton/timber) - logging
    - [Flipper](https://fbflipper.com/) - debugging tool
    - [Glide](https://github.com/bumptech/glide) -  downloading image
    - [Paging with network](https://github.com/android/architecture-components-samples/tree/master/PagingWithNetworkSample) - to use [Paging](https://developer.android.com/topic/libraries/architecture/paging) library with a backend API 
- Tests
    - [Truth](https://github.com/google/truth) - make unit test more readable
    - [mockito](https://github.com/mockito/mockito), [mockito-kotlin](https://github.com/nhaarman/mockito-kotlin) - mock or stub test depndencies

# How to build this app?
This app using [3rd party's free api](https://unsplash.com/developers) for backend.
So you need to register this api to get accesskey and  secretkey :(

If you get your  accesskey and  secretkey, create `secret.properties` file in project root like following.

```
accesskey={your access key  comes here}
secretkey={your secret key comes here}
```

Then these values are read in compiltime  and used when this  app calling api.

If you want to try login-needed feature (ex. like/unlike photo, edit user info),
Go your app page and edit "Redirect URI & Permissions" section

1. add `ishikota://mysite.com/callback` to "Redirect URI" section 
2. check all items (ex. "Public access", "Write likes access")
