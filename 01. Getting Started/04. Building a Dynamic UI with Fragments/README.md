### Building a Dynamic UI with Fragments
This class shows you how to create a dynamic user experience with fragments and optimize your app's user experience for devices with different screen sizes, all while continuing to support devices running versions as old as Android 1.6.

-----------------------------------------------------------

#### Creating a Fragment
- Learn how to build a fragment and implement basic behaviors within its callback methods.

This lesson shows how to extend the [Fragment](https://developer.android.com/reference/android/support/v4/app/Fragment.html) class using the [Support Library](https://developer.android.com/topic/libraries/support-library/index.html) so your app remains compatible with devices running system versions as low as Android 1.6.

#### Building a Flexible UI
- Learn how to build your app with layouts that provide different fragment configurations for different screens.

When designing your application to support a wide range of screen sizes, you can reuse your fragments in different layout configurations to optimize the user experience based on the available screen space.

#### Communicating with Other Fragments
- Learn how to set up communication paths from a fragment to the activity and other fragments.

In order to reuse the Fragment UI components, you should build each as a completely self-contained, modular component that defines its own layout and behavior. Once you have defined these reusable Fragments, you can associate them with an Activity and connect them with the application logic to realize the overall composite UI.
