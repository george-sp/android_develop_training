### Interacting with Other Apps
This class shows you how to use an Intent to perform some basic interactions with other apps, such as:

- start another app.
- receive a result from that app.
- make your app able to respond to intents from other apps.

> An Intent can be explicit in order to start a specific component (a specific Activity instance) or implicit in order to start any component that can handle the intended action (such as "capture a photo").

-----------------------------------------------------------

#### Sending the User to Another App
- Shows how you can create implicit intents to launch other apps that can perform an action.

This lesson shows you how to create an implicit intent for a particular action, and how to use it to start an activity that performs the action in another app.

#### Getting a Result from an Activity
- Shows how you can create implicit intents to launch other apps that can perform an action.

This lesson shows you how to start another activity and receive a result from the activity.

Starting another activity doesn't have to be one-way. You can also start another activity and receive a result back. To receive a result, call [`startActivityForResult()`](https://developer.android.com/reference/android/app/Activity.html#startActivityForResult(android.content.Intent, int)) (instead of [`startActivity()`](https://developer.android.com/reference/android/app/Activity.html#startActivity(android.content.Intent))).

#### Allowing Other Apps to Start Your Activity
- Shows how to make activities in your app open for use by other apps by defining intent filters that declare the implicit intents your app accepts.

This lesson shows how to make activities in your app open for use by other apps by defining intent filters that declare the implicit intents your app accepts.

To allow other apps to start your activity, you need to add an <intent-filter> element in your manifest file for the corresponding <activity> element.
