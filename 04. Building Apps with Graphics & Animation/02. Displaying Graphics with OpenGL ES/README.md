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

-----------------------------------------------------------

#### Building an OpenGL ES Environment
- Learn how to set up an Android application to be able to draw OpenGL graphics.

This lesson explains how to complete a minimal implementation of [GLSurfaceView](https://developer.android.com/reference/android/opengl/GLSurfaceView.html) and [GLSurfaceView.Renderer](https://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html) in a simple application activity.

In order to draw graphics with OpenGL ES in your Android application, you must create a view container for them. 
One of the more straight-forward ways to do this is to implement both a [GLSurfaceView](https://developer.android.com/reference/android/opengl/GLSurfaceView.html) and a [GLSurfaceView.Renderer](https://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html). 
A [GLSurfaceView](https://developer.android.com/reference/android/opengl/GLSurfaceView.html) is a view container for graphics drawn with OpenGL and [GLSurfaceView.Renderer](https://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html) controls what is drawn within that view. 
For more information about these classes, see the [OpenGL ES](https://developer.android.com/guide/topics/graphics/opengl.html) developer guide.

_GLSurfaceView is just one way to incorporate OpenGL ES graphics into your application. For a full-screen or near-full screen graphics view, it is a reasonable choice. Developers who want to incorporate OpenGL ES graphics in a small portion of their layouts should take a look at TextureView. For real, do-it-yourself developers, it is also possible to build up an OpenGL ES view using SurfaceView, but this requires writing quite a bit of additional code._

#### Defining Shapes
Learn how to define shapes and why you need to know about faces and winding.

This lesson explains the OpenGL ES coordinate system relative to an Android device screen, the basics of defining a shape, shape faces, as well as defining a triangle and a square.

Being able to define shapes to be drawn in the context of an OpenGL ES view is the first step in creating your high-end graphics masterpiece. Drawing with OpenGL ES can be a little tricky without knowing a few basic things about how OpenGL ES expects you to define graphic objects.

#### Drawing Shapes
Learn how to draw OpenGL shapes in your application.

This lesson explains how to draw the shapes you defined in the previous lesson using the OpenGL ES 2.0 API.

After you define shapes to be drawn with OpenGL, you probably want to draw them. Drawing shapes with the OpenGL ES 2.0 takes a bit more code than you might imagine, because the API provides a great deal of control over the graphics rendering pipeline.

#### Applying Projection and Camera Views
Learn how to use projection and camera views to get a new perspective on your drawn objects.

This lesson describes how to create a projection and camera view and apply it to shapes drawn in your GLSurfaceView.

In the OpenGL ES environment, projection and camera views allow you to display drawn objects in a way that more closely resembles how you see physical objects with your eyes. This simulation of physical viewing is done with mathematical transformations of drawn object coordinates:
- _**Projection** - This transformation adjusts the coordinates of drawn objects based on the width and height of the [GLSurfaceView](https://developer.android.com/reference/android/opengl/GLSurfaceView.html) where they are displayed. Without this calculation, objects drawn by OpenGL ES are skewed by the unequal proportions of the view window. A projection transformation typically only has to be calculated when the proportions of the OpenGL view are established or changed in the [onSurfaceChanged()](https://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)) method of your renderer. For more information about OpenGL ES projections and coordinate mapping, see [Mapping Coordinates for Drawn Objects](https://developer.android.com/guide/topics/graphics/opengl.html#coordinate-mapping)._
- _**Camera View** - This transformation adjusts the coordinates of drawn objects based on a virtual camera position. It’s important to note that OpenGL ES does not define an actual camera object, but instead provides utility methods that simulate a camera by transforming the display of drawn objects. A camera view transformation might be calculated only once when you establish your [GLSurfaceView](https://developer.android.com/reference/android/opengl/GLSurfaceView.html), or might change dynamically based on user actions or your application’s function._
