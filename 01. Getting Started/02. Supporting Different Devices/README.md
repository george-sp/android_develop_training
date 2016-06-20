### Supporting Different Devices
This class teaches you how to use basic platform features that leverage alternative resources and other features so your app can provide an optimized user experience on a variety of Android-compatible devices, using a single application package (APK).

-----------------------------------------------------------

#### Supporting Different Languages
- Learn how to support multiple languages with alternative string resources.

It’s always a good practice to extract UI strings from your app code and keep them in an external file.
Android makes this easy with a resources directory in each Android project.

#### Supporting Different Screens
- Learn how to optimize the user experience for different screen sizes and densities.

Android categorizes device screens using two general properties: size and density.
As such, you should include some alternative resources that optimize your app’s appearance for different screen sizes and densities.

- There are four **generalized sizes:** small, normal, large, xlarge
- And four **generalized densities:** low (ldpi), medium (mdpi), high (hdpi), extra high (xhdpi)

To declare different layouts and bitmaps you'd like to use for different screens, you must place these alternative resources in separate directories, similar to how you do for different language strings.

#### Supporting Different Platform Versions
- Learn how to use APIs available in new versions of Android while continuing to support older versions of Android.

This lesson shows you how to take advantage of the latest APIs while continuing to support older versions as well.

The dashboard for [Platform Versions](https://developer.android.com/about/dashboards/index.html) is updated regularly to show the distribution of active devices running each version of Android, based on the number of devices that visit the Google Play Store.

> **Tip:** In order to provide the best features and functionality across several Android versions, you should use the [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html) in your app, which allows you to use several recent platform APIs on older versions.


