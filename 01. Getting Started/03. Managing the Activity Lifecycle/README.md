### Managing the Activity Lifecycle
This class explains important lifecycle callback methods that each Activity instance receives and how you can use them so your activity does what the user expects and does not consume system resources when your activity doesn't need them.

-----------------------------------------------------------

#### Starting an Activity
- Learn the basics about the activity lifecycle, how the user can launch your app, and how to perform basic activity creation.

This lesson provides an overview of the most important lifecycle methods and shows you how to handle the first lifecycle callback that creates a new instance of your activity.

#### Pausing and Resuming an Activity
- Learn what happens when your activity is paused (partially obscured) and resumed and what you should do during these state changes.

During normal app use, the foreground activity is sometimes obstructed by other visual components that cause the activity to pause. For example, when a semi-transparent activity opens (such as one in the style of a dialog), the previous activity pauses. As long as the activity is still partially visible but currently not the activity in focus, it remains paused.

> **Note:** When your activity receives a call to [**`onPause()`**](https://developer.android.com/reference/android/app/Activity.html#onPause()), it may be an indication that the activity will be paused for a moment and the user may return focus to your activity. However, it's usually the first indication that the user is leaving your activity.

#### Stopping and Restarting an Activity
- Learn what happens when the user completely leaves your activity and returns to it.

Properly stopping and restarting your activity is an important process in the activity lifecycle that ensures your users perceive that your app is always alive and doesn't lose their progress. 

The Activity class provides two lifecycle methods, [**`onStop()`**](https://developer.android.com/reference/android/app/Activity.html#onStop()) and [**`onRestart()`**](https://developer.android.com/reference/android/app/Activity.html#onStop()), which allow you to specifically handle how your activity handles being stopped and restarted.

> **Note:** Because the system retains your Activity instance in system memory when it is stopped, it's possible that you don't need to implement the [**`onStop()`**](https://developer.android.com/reference/android/app/Activity.html#onStop()) and [**`onRestart()`**](https://developer.android.com/reference/android/app/Activity.html#onStop()) (or even [**`onStart()`**](https://developer.android.com/reference/android/app/Activity.html#onStart()) methods at all. For most activities that are relatively simple, the activity will stop and restart just fine and you might only need to use [**`onPause()`**](https://developer.android.com/reference/android/app/Activity.html#onPause()) to pause ongoing actions and disconnect from system resources.

#### Recreating an Activity
- Learn what happens when the user completely leaves your activity and returns to it.

There are a few scenarios in which your activity is destroyed due to normal app behavior, such as when the user presses the Back button or your activity signals its own destruction by calling [**`finish()`**](https://developer.android.com/training/basics/activity-lifecycle/recreating.html).

> **Caution:** Your activity will be destroyed and recreated each time the user rotates the screen. When the screen changes orientation, the system destroys and recreates the foreground activity because the screen configuration has changed and your activity might need to load alternative resources (such as the layout).

> **Note:** In order for the Android system to restore the state of the views in your activity, **each view must have a unique ID**, supplied by the [__`android:id`__](https://developer.android.com/reference/android/view/View.html#attr_android:id) attribute.
