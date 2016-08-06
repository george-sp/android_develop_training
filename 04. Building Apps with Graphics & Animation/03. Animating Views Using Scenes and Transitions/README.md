### Animating Views Using Scenes and Transitions
Learn how to animate state changes in a view hierarchy using transitions.

This class teaches you to use the built-in animations in the transitions framework to animate changes between view hierarchies. This class also covers how to create custom animations.

Android includes the transitions framework, which enables you to easily animate changes between two view hierarchies. The framework animates the views at runtime by changing some of their property values over time. The framework includes built-in animations for common effects and lets you create custom animations and transition lifecycle callbacks.

> **Note:** For Android versions earlier than 4.4.2 (API level 19) but greater than or equal to Android 4.0 (API level 14), use the `animateLayoutChanges` attribute to animate layouts. To learn more, see [Property Animation](https://developer.android.com/guide/topics/graphics/prop-animation.html) and [Animating Layout Changes](https://developer.android.com/training/animation/layout.html).

Dependencies and prerequisites
- Android 4.4.2 (API level 19) or higher

You should also read
- [How Android Draws View](https://developer.android.com/guide/topics/ui/how-android-draws.html)

Video
- [DevBytes: Android 4.4 Transitions](https://developer.android.com/training/transitions/index.html)

-----------------------------------------------------------

#### The Transitions Framework
Learn the main features and components of the transitions framework.

Animating your app's user interface provides more than just visual appeal. Animations highlight changes and provide visual cues that help users learn how your app works.

To help you animate a change between one view hierarchy and another, Android provides the transitions framework. This framework applies one or more animations to all the views in the hierarchies as it changes between them.

The framework has the following features:
- _Group-level animations_
	Applies one or more animation effects to all of the views in a view hierarchy.
- _Transition-based animation_
	Runs animations based on the changes between starting and ending view property values.
- _Built-in animations_
	Includes predefined animations for common effects such as fade out or movement.
- _Resource file support_
	Loads view hierarchies and built-in animations from layout resource files.
- _Lifecycle callbacks_
	Defines callbacks that provide finer control over the animation and hierarchy change process.

> **Overview**
> The example in Figure 1 shows how an animation provides visual cues to help the user. As the app changes from its search entry screen to its search results screen, it fades out views that are no longer in use and fades in new views.
> 
> This animation is an example of using the transitions framework. The framework animates changes to all the views in two view hierarchies. A view hierarchy can be as simple as a single view or as complex as a ViewGroup containing an elaborate tree of views. The framework animates each view by changing one or more of its property values over time between the initial or starting view hierarchy and the final or ending view hierarchy.
> 
> The transitions framework works in parallel with view hierarchies and animations. The purpose of the framework is to store the state of view hierarchies, change between these hierarchies in order to modify the appearance of the device screen, and animate the change by storing and applying animation definitions.
> 
> The diagram in Figure 2 illustrates the relationship between view hierarchies, framework objects, and animations:
> 
> 
> Figure 2. Relationships in the transitions framework.
> 
> The transitions framework provides abstractions for scenes, transitions, and transition managers. These are described in detail in the following sections. To use the framework, you create scenes for the view hierarchies in your app that you plan to change between. Next, you create a transition for each animation you want to use. To start the animation between two view hierarchies, you use a transition manager specifying the transition to use and the ending scene. This procedure is described in detail in the remaining lessons in this class.
>
> - **Scenes**
>
> A [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) stores the state of a view hierarchy, including all its views and their property values. A view hierarchy can be a simple view or a complex tree of views and child layouts. Storing the view hierarchy state in a scene enables you to transition into that state from another scene. The framework provides the Scene class to represent a scene.
> 
> The transitions framework lets you create scenes from layout resource files or from [`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html) objects in your code. Creating a scene in your code is useful if you generated a view hierarchy dynamically or if you are modifying it at runtime.
> 
> In most cases, you do not create a starting scene explicitly. If you have applied a transition, the framework uses the previous ending scene as the starting scene for any subsequent transitions. If you have not applied a transition, the framework collects information about the views from the current state of the screen.
> 
> A scene can also define its own actions that run when you make a scene change. For example, this feature is useful for cleaning up view settings after you transition to a scene.
> 
> In addition to the view hierarchy and its property values, a scene also stores a reference to the parent of the view hierarchy. This root view is called a **scene root**. Changes to the scene and animations that affect the scene occur within the scene root.
> 
> To learn how to create scenes, see [Creating a Scene](https://developer.android.com/training/transitions/scenes.html).
> 
> - **Transitions**
>
> In the transitions framework, animations create a series of frames that depict a change between the view hierarchies in the starting and ending scenes. Information about the animation is stored in a [`Transition`](https://developer.android.com/reference/android/transition/Transition.html) object. To run the animation, you apply the transition using a [`TransitionManager`](https://developer.android.com/reference/android/transition/TransitionManager.html) instance. The framework can transition between two different scenes or transition to a different state for the current scene.
> 
> The framework includes a set of built-in transitions for commonly-used animation effects, such as fading and resizing views. You can also define your own custom transitions to create an animation effect using the APIs in the animations framework. The transitions framework also enables you to combine different animation effects in a transition set that contains a group of individual built-in or custom transitions.
> 
> The transition lifecycle is similar to the activity lifecycle, and it represents the transition states that the framework monitors between the start and the completion of an animation. At important lifecycle states, the framework invokes callback methods that you can implement to make adjustments to your user interface at different phases of the transition.
> 
> To learn more about transitions, see [Applying a Transition](https://developer.android.com/training/transitions/transitions.html) and [Creating Custom Transitions](https://developer.android.com/training/transitions/custom-transitions.html).
> 
> - **Limitations**
> 
>This section lists some known limitations of the transitions framework:
> 
> 1. Animations applied to a [`SurfaceView`](https://developer.android.com/reference/android/view/SurfaceView.html) may not appear correctly. [`SurfaceView`](https://developer.android.com/reference/android/view/SurfaceView.html) instances are updated from a non-UI thread, so the updates may be out of sync with the animations of other views.
> 2. Some specific transition types may not produce the desired animation effect when applied to a [`TextureView`](https://developer.android.com/reference/android/view/TextureView.html).
> 3. Classes that extend [`AdapterView`](https://developer.android.com/reference/android/widget/AdapterView.html), such as [`ListView`](https://developer.android.com/reference/android/widget/ListView.html), manage their child views in ways that are incompatible with the transitions framework. If you try to animate a view based on [`AdapterView`](https://developer.android.com/reference/android/widget/AdapterView.html), the device display may hang.
> 4. If you try to resize a [`TextView`](https://developer.android.com/reference/android/widget/TextView.html) with an animation, the text will pop to a new location before the object has completely resized. To avoid this problem, do not animate the resizing of views that contain text.

#### Creating a Scene
Learn how to create a scene to store the state of a view hierarchy.

This lesson shows you how to create scenes in your app and how to define scene actions. The next lesson shows you how to transition between two scenes.

Scenes store the state of a view hierarchy, including all its views and their property values. The transitions framework can run animations between a starting and an ending scene. The starting scene is often determined automatically from the current state of the user interface. For the ending scene, the framework enables you to create a scene from a layout resource file or from a group of views in your code.

> **Note:** The framework can animate changes in a single view hierarchy without using scenes, as described in [Apply a Transition Without Scenes](https://developer.android.com/training/transitions/transitions.html#NoScenes). However, understanding this lesson is essential to work with transitions.

--------------------------------------------------------------------------

> - **Create a Scene From a Layout Resource**
>
> You can create a [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) instance directly from a layout resource file. Use this technique when the view hierarchy in the file is mostly static. The resulting scene represents the state of the view hierarchy at the time you created the [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) instance. If you change the view hierarchy, you have to recreate the scene. The framework creates the scene from the entire view hierarchy in the file; you can not create a scene from part of a layout file.
> 
> To create a [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) instance from a layout resource file, retrieve the scene root from your layout as a [`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html) instance and then call the [`Scene.getSceneForLayout()`](https://developer.android.com/reference/android/transition/Scene.html#getSceneForLayout(android.view.ViewGroup, int, android.content.Context)) method with the scene root and the resource ID of the layout file that contains the view hierarchy for the scene.

