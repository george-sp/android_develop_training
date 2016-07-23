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

### Loading Large Bitmaps Efficiently
- This lesson walks you through decoding large bitmaps without exceeding the per application memory limit.

This lesson walks you through decoding large bitmaps without exceeding the per application memory limit by loading a smaller subsampled version in memory.

_(Given that you are working with limited memory, ideally you only want to load a lower resolution version in memory. The lower resolution version should match the size of the UI component that displays it. An image with a higher resolution does not provide any visible benefit, but still takes up precious memory and incurs additional performance overhead due to additional on the fly scaling.)_
