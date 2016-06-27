### Sharing Simple Data
One of the great things about Android applications is their ability to communicate and integrate with each other. 

This class covers some common ways you can send and receive simple data between applications using [Intent](https://developer.android.com/reference/android/content/Intent.html) APIs and the [ActionProvider](https://developer.android.com/reference/android/view/ActionProvider.html) object.

-----------------------------------------------------------

#### Sending Simple Data to Other Apps
- Learn how to set up your application to be able to send text and binary data to other applications with intents.

Sending and receiving data between applications with intents is most commonly used for social sharing of content.
Intents allow users to share information quickly and easily, using their favorite applications.

> **Note:** The best way to add a share action item to an ActionBar is to use ShareActionProvider, which became available in API level 14. ShareActionProvider is discussed in the lesson about Adding an Easy Share Action.

#### Receiving Simple Data from Other Apps
- Learn how to set up your application to receive text and binary data from intents.

Think about how users interact with your application, and what data types you want to receive from other applications. 
_For example, a social networking application would likely be interested in receiving text content, like an interesting web URL, from another app._

#### Adding an Easy Share Action
- Learn how to add a "share" action item to your action bar.

An [ActionProvider](https://developer.android.com/reference/android/view/ActionProvider.html), once attached to a menu item in the action bar, handles both the appearance and behavior of that item. In the case of [ShareActionProvider](https://developer.android.com/reference/android/widget/ShareActionProvider.html), you provide a share intent and it does the rest.
