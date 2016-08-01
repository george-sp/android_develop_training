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

> #### Use a Memory Cache
> 
> A memory cache offers fast access to bitmaps at the cost of taking up valuable application memory. The [LruCache](https://developer.android.com/reference/android/util/LruCache.html) class (also available in the [Support Library](https://developer.android.com/reference/android/support/v4/util/LruCache.html) for use back to API Level 4) is particularly well suited to the task of caching bitmaps, keeping recently referenced objects in a strong referenced [LinkedHashMap](https://developer.android.com/reference/java/util/LinkedHashMap.html) and evicting the least recently used member before the cache exceeds its designated size.
> 

> > Note: In the past, a popular memory cache implementation was a [SoftReference](https://developer.android.com/reference/java/lang/ref/SoftReference.html) or [WeakReference](https://developer.android.com/reference/java/lang/ref/SoftReference.html) bitmap cache, however this is not recommended. Starting from Android 2.3 (API Level 9) the garbage collector is more aggressive with collecting soft/weak references which makes them fairly ineffective. In addition, prior to Android 3.0 (API Level 11), the backing data of a bitmap was stored in native memory which is not released in a predictable manner, potentially causing an application to briefly exceed its memory limits and crash.

> 
> In order to choose a suitable size for a LruCache, a number of factors should be taken into consideration, for example:
> 
> - How memory intensive is the rest of your activity and/or application?
> - How many images will be on-screen at once? How many need to be available ready to come on-screen?
> - What is the screen size and density of the device? An extra high density screen (xhdpi) device like Galaxy Nexus will need a larger cache to hold the same number of images in memory compared to a device like Nexus S (hdpi).
> - What dimensions and configuration are the bitmaps and therefore how much memory will each take up?
> - How frequently will the images be accessed? Will some be accessed more frequently than others? If so, perhaps you may want to keep certain items always in memory or even have multiple LruCache objects for different groups of bitmaps.
> - Can you balance quality against quantity? Sometimes it can be more useful to store a larger number of lower quality bitmaps, potentially loading a higher quality version in another background task.
> There is no specific size or formula that suits all applications, it's up to you to analyze your usage and come up with a suitable solution. A cache that is too small causes additional overhead with no benefit, a cache that is too large can once again cause java.lang.OutOfMemory exceptions and leave the rest of your app little memory to work with.

> #### Use a Disk Cache
> A memory cache is useful in speeding up access to recently viewed bitmaps, however you cannot rely on images being available in this cache. Components like [GridView](https://developer.android.com/reference/android/widget/GridView.html) with larger datasets can easily fill up a memory cache. Your application could be interrupted by another task like a phone call, and while in the background it might be killed and the memory cache destroyed. Once the user resumes, your application has to process each image again.
> 
> A disk cache can be used in these cases to persist processed bitmaps and help decrease loading times where images are no longer available in a memory cache. Of course, fetching images from disk is slower than loading from memory and should be done in a background thread, as disk read times can be unpredictable.
> 
> > Note: A [ContentProvider](https://developer.android.com/reference/android/content/ContentProvider.html) might be a more appropriate place to store cached images if they are accessed more frequently, for example in an image gallery application.
>
> The sample code of this class uses a **DiskLruCache** implementation that is pulled from the **[Android source](https://android.googlesource.com/platform/libcore/+/jb-mr2-release/luni/src/main/java/libcore/io/DiskLruCache.java)**.
> 
> While the memory cache is checked in the UI thread, the disk cache is checked in the background thread. Disk operations should never take place on the UI thread. When image processing is complete, the final bitmap is added to both the memory and disk cache for future use.

#### Managing Bitmap Memory
- This lesson explains how to manage bitmap memory to maximize ypur app's performance.

In addition to the steps described in [Caching Bitmaps](https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html), there are specific things you can do to facilitate garbage collection and bitmap reuse.
The recommended strategy depends on which version(s) of Android you are targeting.

To set the stage for this lesson, here is how Android's management of bitmap memory has evolved:

 1. _On Android Android 2.2 (API level 8) and lower_, when garbage collection occurs, your app's threads get stopped. This causes a lag that can degrade performance. **Android 2.3 adds concurrent garbage collection, which means that the memory is reclaimed soon after a bitmap is no longer referenced.**
 2. _On Android 2.3.3 (API level 10) and lower_, the backing pixel data for a bitmap is stored in native memory. It is separate from the bitmap itself, which is stored in the Dalvik heap. The pixel data in native memory is not released in a predictable manner, potentially causing an application to briefly exceed its memory limits and crash. **As of Android 3.0 (API level 11), the pixel data is stored on the Dalvik heap along with the associated 

> - On Android 2.3.3 (API level 10) and lower, using [recycle()](https://developer.android.com/reference/android/graphics/Bitmap.html#recycle()) is recommended. If you're displaying large amounts of bitmap data in your app, you're likely to run into [OutOfMemoryError](https://developer.android.com/reference/java/lang/OutOfMemoryError.html) errors. The [recycle()](https://developer.android.com/reference/android/graphics/Bitmap.html#recycle()) method allows an app to reclaim memory as soon as possible.
> 
> > Caution: You should use [recycle()](https://developer.android.com/reference/android/graphics/Bitmap.html#recycle()) only when you are sure that the bitmap is no longer being used. If you call [recycle()](https://developer.android.com/reference/android/graphics/Bitmap.html#recycle()) and later attempt to draw the bitmap, you will get the error: **"Canvas: trying to use a recycled bitmap"**.

> - Android 3.0 (API level 11) introduces the [BitmapFactory.Options.inBitmap](https://developer.android.com/reference/android/graphics/BitmapFactory.Options.html#inBitmap) field. If this option is set, decode methods that take the [Options](https://developer.android.com/reference/android/graphics/BitmapFactory.Options.html) object will attempt to reuse an existing bitmap when loading content. This means that the bitmap's memory is reused, resulting in improved performance, and removing both memory allocation and de-allocation. However, there are certain restrictions with how [inBitmap](https://developer.android.com/reference/android/graphics/BitmapFactory.Options.html#inBitmap) can be used. In particular, before Android 4.4 (API level 19), only equal sized bitmaps are supported. For details, please see the [inBitmap](https://developer.android.com/reference/android/graphics/BitmapFactory.Options.html#inBitmap) documentation.
