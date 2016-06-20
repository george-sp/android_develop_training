### Working with System Permissions
This class shows you how to declare and request permissions for your app.

To protect the system's integrity and the user's privacy, **_Android runs each app in a limited access sandbox_**.

If the app wants to use resources or information outside of its sandbox, the app has to explicitly request permission.
Depending on the type of permission the app requests, the system may grant the permission automatically, or the system may ask the user to grant the permission.

> [DESIGN PATTERNS: Permissions](https://material.google.com/patterns/permissions.html#)

-----------------------------------------------------------

#### Declaring Permissions
- Learn how to declare the permissions you need in your app manifest.

Every Android app runs in a limited-access sandbox. If an app needs to use resources or information outside of its own sandbox, the app has to request the appropriate permission. You declare that your app needs a permission by listing the permission in the [App Manifest](https://developer.android.com/guide/topics/manifest/manifest-intro.html).

#### Requesting Permissions at Run Time
- Learn how to request permissions from the user while the app is running. This lesson only applies to apps on devices running Android 6.0 (API level 23) and higher.

This lesson describes how to use the Android [Support Library](This lesson describes how to use the Android Support Library to check for, and request, permissions. The Android framework provides similar methods as of Android 6.0 (API level 23). However, using the support library is simpler, since your app doesn't need to check which version of Android it's running on before calling the methods.) to check for, and request, permissions. The Android framework provides similar methods as of Android 6.0 (API level 23). However, using the support library is simpler, since your app doesn't need to check which version of Android it's running on before calling the methods.

System permissions are divided into two categories, normal and dangerous:

- Normal permissions do not directly risk the user's privacy. If your app lists a normal permission in its manifest, the system grants the permission automatically.
- Dangerous permissions can give the app access to the user's confidential data. If your app lists a normal permission in its manifest, the system grants the permission automatically. If you list a dangerous permission, the user has to explicitly give approval to your app.

> **Note:** Beginning with Android 6.0 (API level 23), users can revoke permissions from any app at any time, even if the app targets a lower API level. You should test your app to verify that it behaves properly when it's missing a needed permission, regardless of what API level your app targets.

#### Permissions Best Practices
- Guidelines for creating the best user experience in requesting and using permissions.

It's easy for an app to overwhelm a user with permission requests. If a user finds the app frustrating to use, or the user is worried about what the app might be doing with the user's information, they may avoid using the app or uninstall it entirely. The following best practices can help you avoid such bad user experiences.
