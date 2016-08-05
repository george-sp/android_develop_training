### Displaying Graphics with OpenGL ES
Learn how to create OpenGL graphics within the Android app framework and respond to touch input.

The Android framework provides plenty of standard tools for creating attractive, functional graphical user interfaces. However, if you want more control of what your application draws on screen, or are venturing into three dimensional graphics, you need to use a different tool. 
The OpenGL ES APIs provided by the Android framework offers a set of tools for displaying high-end, animated graphics that are limited only by your imagination and can also benefit from the acceleration of graphics processing units (GPUs) provided on many Android devices.

This class walks you through the basics of developing applications that use OpenGL, including setup, drawing objects, moving drawn elements and responding to touch input.

_The example code in this class uses the OpenGL ES 2.0 APIs, which is the recommended API version to use with current Android devices. 
For more information about versions of OpenGL ES, see the [OpenGL](https://developer.android.com/guide/topics/graphics/opengl.html) developer guide._

> **Note:** Be careful not to mix OpenGL ES 1.x API calls with OpenGL ES 2.0 methods! 
The two APIs are not interchangeable and trying to use them together only results in frustration and sadness.

Dependencies and prerequisites
- Android 2.2 (API Level 8) or higher
- Experience building an [Android app](https://developer.android.com/training/basics/firstapp/index.html)

You should also read
- [OpenGL](https://developer.android.com/guide/topics/graphics/opengl.html)
