### Saving Data
This class introduces you to the principal data storage options in Android, including:
- Saving key-value pairs of simple data types in a shared preferences file
- Saving arbitrary files in Android's file system
- Using databases managed by SQLite

-----------------------------------------------------------

#### Saving Kay Value Sets
This class shows you how to use the SharedPreferences APIs to store and retrieve simple values.

> **Note:** The `SharedPreferences` APIs are only for reading and writing key-value pairs and you should not confuse them with the `Preference` APIs, which help you build a user interface for your app settings (although they use `SharedPreferences` as their implementation to save the app settings). For information about using the `Preference` APIs, see the [Settings](https://developer.android.com/guide/topics/ui/settings.html) guide.

#### Saving Files
This lesson describes how to work with the Android file system to read and write files with the [`File`](https://developer.android.com/reference/java/io/File.html) APIs.

A `File` object is suited to reading or writing large amounts of data in start-to-finish order without skipping around. For example, it's good for image files or anything exchanged over a network.

This lesson shows how to perform basic file-related tasks in your app. The lesson assumes that you are familiar with the basics of the Linux file system and the standard file input/output APIs in [`java.io`](https://developer.android.com/reference/java/io/package-summary.html).
