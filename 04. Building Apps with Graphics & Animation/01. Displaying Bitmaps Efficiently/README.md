### Displaying Bitmaps Efficiently
Learn how to use common techniques to process and load Bitmap objects in a way that keeps your user interface (UI) components responsive and avoids exceeding your application memory limit.

> If you're not careful, bitmaps can quickly consume your available memory budget leading to an application crash due to the dreaded exception:
>
> **`java.lang.OutofMemoryError: bitmap size exceeds VM budget.`**

There are a number of reasons why loading bitmaps in your Android application is tricky:

1. Mobile devices typically have constrained system resources. Android devices can have as little as 16MB of memory available to a single application. The [Android Compatibility Definition Document (CDD)](https://source.android.com/compatibility/cts/downloads.html), Section 3.7. Virtual Machine Compatibility gives the required minimum application memory for various screen sizes and densities. Applications should be optimized to perform under this minimum memory limit. However, keep in mind many devices are configured with higher limits.
2. Bitmaps take up a lot of memory, especially for rich images like photographs. For example, the camera on the [Galaxy Nexus](https://www.android.com/phones/) takes photos up to 2592x1936 pixels (5 megapixels). If the bitmap configuration used is [ARGB_8888](https://developer.android.com/reference/android/graphics/Bitmap.Config.html) (the default from the Android 2.3 onward) then loading this image into memory takes about 19MB of memory (2592*1936*4 bytes), immediately exhausting the per-app limit on some devices.
3. Android app UI’s frequently require several bitmaps to be loaded at once. Components such as [ListView](https://developer.android.com/reference/android/widget/ListView.html), [GridView](https://developer.android.com/reference/android/widget/GridView.html) and [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html) commonly include multiple bitmaps on-screen at once with many more potentially off-screen ready to show at the flick of a finger.

Dependencies and prerequisites
- Android 2.1 (API Level 7) or higher
- [Support Library](https://developer.android.com/topic/libraries/support-library/index.html)

Video
- [DevBytes: Bitmap Allocation](https://developer.android.com/training/displaying-bitmaps/index.html)
- [DevBytes: Making Apps Beautiful -> Part 4 - Performance Tuning](https://developer.android.com/training/displaying-bitmaps/index.html)

-----------------------------------------------------------

#### Loading Large Bitmaps Efficiently
- This lesson walks you through decoding large bitmaps without exceeding the per application memory limit.

This lesson walks you through decoding large bitmaps without exceeding the per application memory limit by loading a smaller subsampled version in memory.
Here are some factors to consider:
- Estimated memory usage of loading the full image in memory.
- Amount of memory you are willing to commit to loading this image given any other memory requirements of your application.
- Dimensions of the target [ImageView](https://developer.android.com/reference/android/widget/ImageView.html) or UI component that the image is to be loaded into.
- Screen size and density of the current device.

_(Given that you are working with limited memory, ideally you only want to load a lower resolution version in memory. The lower resolution version should match the size of the UI component that displays it. An image with a higher resolution does not provide any visible benefit, but still takes up precious memory and incurs additional performance overhead due to additional on the fly scaling.)_

#### Processing Bitmaps Off the UI Thread
- This lesson walks you through processing bitmaps in a background thread using [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask.html) and shows you how to handle concurrency issues.

Bitmap processing (resizing, downloading from a remote source, etc.) should never take place on the main UI thread. This lesson walks you through processing bitmaps in a background thread using [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask.html) and explains how to handle concurrency issues.

The [BitmapFactory.decode*](https://developer.android.com/reference/android/graphics/BitmapFactory.html#decodeByteArray(byte[], int, int, android.graphics.BitmapFactory.Options)) methods, discussed in the [Load Large Bitmaps](https://developer.android.com/training/displaying-bitmaps/load-bitmap.html) Efficiently lesson, should not be executed on the main UI thread if the source data is read from disk or a network location (or really any source other than memory). 
_The time this data takes to load is unpredictable and depends on a variety of factors (speed of reading from disk or network, size of image, power of CPU, etc.)._

If one of these tasks blocks the UI thread, the system flags your application as non-responsive and the user has the option of closing it (see **[Designing for Responsiveness](https://developer.android.com/training/articles/perf-anr.html)** for more information).

The blog post **[Multithreading for Performance](http://android-developers.blogspot.com/2010/07/multithreading-for-performance.html)** further discusses dealing with **concurrency**, and offers a solution where the ImageView stores a reference to the most recent AsyncTask which can later be checked when the task completes. Using a similar method, the AsyncTask from the previous section can be extended to follow a similar pattern.

#### Caching Bitmaps
- This lesson walks you through using a memory and disk bitmap cache to improve the responsiveness and fluidity of your UI when loading multiple bitmaps.

Loading a single bitmap into your user interface (UI) is straightforward, however things get more complicated if you need to load a larger set of images at once. 
In many cases (such as with components like [ListView](https://developer.android.com/reference/android/widget/ListView.html), [GridView](https://developer.android.com/reference/android/widget/GridView.html) or [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html)), the total number of images on-screen combined with images that might soon scroll onto the screen are essentially unlimited.

_Memory usage is kept down with components like this by recycling the child views as they move off-screen. The garbage collector also frees up your loaded bitmaps, assuming you don't keep any long lived references. This is all good and well, but in order to keep a fluid and fast-loading UI you want to avoid continually processing these images each time they come back on-screen. A memory and disk cache can often help here, allowing components to quickly reload processed images._
