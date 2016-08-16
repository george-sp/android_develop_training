
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
- Learn the main features and components of the transitions framework.

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
- Learn how to create a scene to store the state of a view hierarchy.

This lesson shows you how to create scenes in your app and how to define scene actions. The next lesson shows you how to transition between two scenes.

Scenes store the state of a view hierarchy, including all its views and their property values. The transitions framework can run animations between a starting and an ending scene. The starting scene is often determined automatically from the current state of the user interface. For the ending scene, the framework enables you to create a scene from a layout resource file or from a group of views in your code.

> **Note:** The framework can animate changes in a single view hierarchy without using scenes, as described in [Apply a Transition Without Scenes](https://developer.android.com/training/transitions/transitions.html#NoScenes). However, understanding this lesson is essential to work with transitions.

--------------------------------------------------------------------------

> - **Create a Scene From a Layout Resource**
>
> You can create a [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) instance directly from a layout resource file. Use this technique when the view hierarchy in the file is mostly static. The resulting scene represents the state of the view hierarchy at the time you created the [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) instance. If you change the view hierarchy, you have to recreate the scene. The framework creates the scene from the entire view hierarchy in the file; you can not create a scene from part of a layout file.
> 
> To create a [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) instance from a layout resource file, retrieve the scene root from your layout as a [`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html) instance and then call the [`Scene.getSceneForLayout()`](https://developer.android.com/reference/android/transition/Scene.html#getSceneForLayout(android.view.ViewGroup, int, android.content.Context)) method with the scene root and the resource ID of the layout file that contains the view hierarchy for the scene.
>> - Define Layouts for Scenes
>>
>> The code snippets in the rest of this section show you how to create two different scenes with the same scene root element. The snippets also demonstrate that you can load multiple unrelated Scene objects without implying that they are related to each other.
>>
>> The example consists of the following layout definitions:
>>  1. The main layout of an activity with a text label and a child layout.
>>  2. A relative layout for the first scene with two text fields.
>>  3. A relative layout for the second scene with the same two text fields in different order.
>>
>> The example is designed so that all of the animation occurs within the child layout of the main layout for the activity. The text label in the main layout remains static.
>>
>> - Generate Scenes from Layouts
>>
>> After you create definitions for the two relative layouts, you can obtain an scene for each of them. This enables you to later transition between the two UI configurations. To obtain a scene, you need a reference to the scene root and the layout resource ID.
>
> - **Create a Scene in Your Code**
>
> You can also create a [`Scene`](https://developer.android.com/reference/android/transition/Scene.html) instance in your code from a [`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html) object. Use this technique when you modify the view hierarchies directly in your code or when you generate them dynamically.
>
> To create a scene from a view hierarchy in your code, use the [`Scene(sceneRoot, viewHierarchy)`](https://developer.android.com/reference/android/transition/Scene.html#Scene(android.view.ViewGroup, android.view.View)) constructor. Calling this constructor is equivalent to calling the [`Scene.getSceneForLayout()`](https://developer.android.com/reference/android/transition/Scene.html#getSceneForLayout(android.view.ViewGroup, int, android.content.Context)) method when you have already inflated a layout file.
>
> - **Create Scene Actions**
>
> The framework enables you to define custom scene actions that the system runs when entering or exiting a scene. In many cases, defining custom scene actions is not necessary, since the framework animates the change between scenes automatically.
>
> Scene actions are useful for handling these cases:
>
> 1. Animate views that are not in the same hierarchy. You can animate views for both the starting and ending scenes using exit and entry scene actions.
> 2. Animate views that the transitions framework cannot animate automatically, such as [`ListView`](https://developer.android.com/reference/android/widget/ListView.html) objects. For more information, see [`Limitations`](https://developer.android.com/training/transitions/overview.html#Limitations).
>
>To provide custom scene actions, define your actions as [Runnable](https://developer.android.com/reference/java/lang/Runnable.html) objects and pass them to the [`Scene.setExitAction()`](https://developer.android.com/reference/android/transition/Scene.html#setExitAction(java.lang.Runnable)) or [`Scene.setEnterAction()`](https://developer.android.com/reference/android/transition/Scene.html#setEnterAction(java.lang.Runnable)) methods. The framework calls the [`setExitAction()`](https://developer.android.com/reference/android/transition/Scene.html#setExitAction(java.lang.Runnable)) method on the starting scene before running the transition animation and the [`setEnterAction()`](https://developer.android.com/reference/android/transition/Scene.html#setEnterAction(java.lang.Runnable)) method on the ending scene after running the transition animation.
>
>> **Note:** Do not use scene actions to pass data between views in the starting and ending scenes. For more information, see [Defining Transition Lifecycle Callbacks](https://developer.android.com/training/transitions/transitions.html#Callbacks).

#### Applying a Transition
- Learn how to apply a transition between two scenes of a view hierarchy.

This lesson teaches you run an animation between two scenes using built-in transitions to move, resize, and fade views. The next lesson shows you how to define custom transitions.

In the transitions framework, animations create a series of frames that depict a change between the view hierarchies in the starting and ending scenes. The framework represents these animations as transition objects, which contain information about an animation. To run an animation, you provide the transition to use and the ending scene to a transition manager.

> - **Create a Transition**
>
> In the previous lesson, you learned how to create scenes that represent the state of different view hierarchies. Once you have defined the starting scene and the ending scene you want to change between, you need to create a Transition object that defines an animation. The framework enables you to specify a built-in transition in a resource file and inflate it in your code or to create an instance of a built-in transition directly in your code.
>
> **Build-in transition types:**
>
| Class        | Tag          | Attributes  | Effect |
| ------------- |:-------------:|:-------------:| :------------- |
| [AutoTransition](https://developer.android.com/reference/android/transition/AutoTransition.html) | `<autoTransition/>` | - | Default transition. Fade out, move and resize, and fade in views, in that order. |
| [Fade](https://developer.android.com/reference/android/transition/Fade.html)      | `<fade/>` |  ```android:fadingMode="[fade_in | fade out | fade_in_out]"``` | `fade_in` fades in views<br>`fade_out` fades out views<br>`fade_in_out` (default) does a fade_out followed by a fade_in. |
| [ChangeBounds](https://developer.android.com/reference/android/transition/ChangeBounds.html) | `<changeBounds/>`	 | - | Moves and resizes views.
>> - Create a transition instance from a resource file
>>
>> This technique enables you to modify your transition definition without having to change the code of your activity. This technique is also useful to separate complex transition definitions from your application code, as shown in [Specify Multiple Transitions](https://developer.android.com/training/transitions/transitions.html#Multiple).
>>
>> To specify a built-in transition in a resource file, follow these steps:
>>
>>  1. Add the `res/transition/` directory to your project.
>>  2. Create a new XML resource file inside this directory.
>>  3. Add an XML node for one of the built-in transitions.
>>
>> - Create a transition instance in your code
>>
>> This technique is useful for creating transition objects dynamically if you modify the user interface in your code, and to create simple built-in transition instances with few or no parameters.
>>
>> To create an instance of a built-in transition, invoke one of the public constructors in the subclasses of the [`Transition`](https://developer.android.com/reference/android/transition/Transition.html) class. For example, the following code snippet creates an instance of the [`Fade`](https://developer.android.com/reference/android/transition/Fade.html) transition:
>>
>> ```Transition mFadeTransition = new Fade();```


> - **Apply a Transition**
>
> You typically apply a transition to change between different view hierarchies in response to an event, such as a user action. For example, consider a search app: when the user enters a search term and clicks the search button, the app changes to the scene that represents the results layout while applying a transition that fades out the search button and fades in the search results.
>
> To make a scene change while applying a transition in response to some event in your activity, call the [`TransitionManager.go()`](https://developer.android.com/reference/android/transition/TransitionManager.html#go(android.transition.Scene)) static method with the ending scene and the transition instance to use for the animation, as shown in the following snippet:
>
> ```TransitionManager.go(mEndingScene, mFadeTransition);```
>
> The framework changes the view hierarchy inside the scene root with the view hierarchy from the ending scene while running the animation specified by the transition instance. The starting scene is the ending scene from the last transition. If there was no previous transition, the starting scene is determined automatically from the current state of the user interface.
>
> If you do not specify a transition instance, the transition manager can apply an automatic transition that does something reasonable for most situations. For more information, see the API reference for the [`TransitionManager`](https://developer.android.com/reference/android/transition/TransitionManager.html) class.
>
> - **Choose Specific Target Views**
>
> The framework applies transitions to all views in the starting and ending scenes by default. In some cases, you may only want to apply an animation to a subset of views in a scene. For example, the framework does not support animating changes to [`ListView`](https://developer.android.com/reference/android/widget/ListView.html) objects, so you should not try to animate them during a transition. The framework enables you to select specific views you want to animate.
>
> Each view that the transition animates is called a _target_. You can only select targets that are part of the view hierarchy associated with a scene.
>
> To remove one or more views from the list of targets, call the [`removeTarget()`](https://developer.android.com/reference/android/transition/Transition.html#removeTarget(android.view.View)) method before starting the transition. To add only the views you specify to the list of targets, call the [`addTarget()`](https://developer.android.com/reference/android/transition/Transition.html#addTarget(android.view.View)) method. For more information, see the API reference for the [`Transition`](https://developer.android.com/reference/android/transition/Transition.html) class.
>
> - **Specify Multiple Transitions**
>
> To get the most impact from an animation, you should match it to the type of changes that occur between the scenes. For example, if you are removing some views and adding others between scenes, a fade out/fade in animation provides a noticeable indication that some views are no longer available. If you are moving views to different points on the screen, a better choice would be to animate the movement so that users notice the new location of the views.
>
> You do not have to choose only one animation, since the transitions framework enables you to combine animation effects in a transition set that contains a group of individual built-in or custom transitions.
>
> To define a transition set from a collection of transitions in XML, create a resource file in the `res/transitions/` directory and list the transitions under the `transitionSet` element.
>
> To inflate the transition set into a [`TransitionSet`](https://developer.android.com/reference/android/transition/TransitionSet.html) object in your code, call the [`TransitionInflater.from()`](https://developer.android.com/reference/android/transition/TransitionInflater.html#from(android.content.Context)) method in your activity. The [`TransitionSet`](https://developer.android.com/reference/android/transition/TransitionSet.html) class extends from the [`Transition`](https://developer.android.com/reference/android/transition/Transition.html) class, so you can use it with a transition manager just like any other [`Transition`](https://developer.android.com/reference/android/transition/Transition.html) instance.
>
> - **Apply a Transition Without Scenes**
>
> Changing view hierarchies is not the only way to modify your user interface. You can also make changes by adding, modifying, and removing child views within the current hierarchy. For example, you can implement a search interaction with just a single layout. Start with the layout showing a search entry field and a search icon. To change the user interface to show the results, remove the search button when the user clicks it by calling the [`ViewGroup.removeView()`](https://developer.android.com/reference/android/view/ViewGroup.html#removeView(android.view.View)) method, and add the search results by calling [`ViewGroup.addView()`](https://developer.android.com/reference/android/view/ViewGroup.html#addView(android.view.View)) method.
>
> If you make changes within the current view hierarchy in this fashion, you do not need to create a scene. Instead, you can create and apply a transition between two states of a view hierarchy using a _delayed transition_. This feature of the transitions framework starts with the current view hierarchy state, records changes you make to its views, and applies a transition that animates the changes when the system redraws the user interface.
>
> To create a delayed transition within a single view hierarchy, follow these steps:
> 
> 1. When the event that triggers the transition occurs, call the [`TransitionManager.beginDelayedTransition()`](https://developer.android.com/reference/android/transition/TransitionManager.html#beginDelayedTransition(android.view.ViewGroup)) method providing the parent view of all the views you want to change and the transition to use. The framework stores the current state of the child views and their property values.
> 2. Make changes to the child views as required by your use case. The framework records the changes you make to the child views and their properties.
> 3. When the system redraws the user interface according to your changes, the framework animates the changes between the original state and the new state.
>
> - **Define Transition Lifecycle Callbacks**
>
> The transition lifecycle is similar to the activity lifecycle. It represents the transition states that the framework monitors during the time between a call to the [`TransitionManager.go()`](https://developer.android.com/reference/android/transition/TransitionManager.html#go(android.transition.Scene)) method and the completion of the animation. At important lifecycle states, the framework invokes callbacks defined by the [`TransitionListener`](https://developer.android.com/reference/android/transition/Transition.TransitionListener.html) interface.
>
> Transition lifecycle callbacks are useful, for example, for copying a view property value from the starting view hierarchy to the ending view hierarchy during a scene change. You cannot simply copy the value from its starting view to the view in the ending view hierarchy, because the ending view hierarchy is not inflated until the transition is completed. Instead, you need to store the value in a variable and then copy it into the ending view hierarchy when the framework has finished the transition. To get notified when the transition is completed, you can implement the [`TransitionListener.onTransitionEnd()`](https://developer.android.com/reference/android/transition/Transition.TransitionListener.html#onTransitionEnd(android.transition.Transition)) method in your activity.
>
> For more information, see the API reference for the [`TransitionListener`](https://developer.android.com/reference/android/transition/Transition.TransitionListener.html) class.

#### Creating Custom Transitions
- Learn how to create other animation effects not included in the transitions framework.

This lesson teaches you to capture property values and generate animations to create custom transitions.

_A custom transition enables you to create an animation that is not available from any of the built-in transition classes. For example, you can define a custom transition that turns the foreground color of text and input fields to gray to indicate that the fields are disabled in the new screen. This type of change helps users see the fields you disabled._
> - **Extend the Transition Class**
>
> To create a custom transition, add a class to your project that extends the [`Transition`](https://developer.android.com/reference/android/transition/Transition.html) class and override the methods shown:
>
> 1. [`captureStartValues(TransitionValues values)`](https://developer.android.com/reference/android/transition/Transition.html#captureStartValues(android.transition.TransitionValues))
> 2. [`captureEndValues(TransitionValues values)`](https://developer.android.com/reference/android/transition/Transition.html#captureEndValues(android.transition.TransitionValues))
> 3. [`createAnimator(ViewGroup sceneRoot,
                                   TransitionValues startValues,
                                   TransitionValues endValues)`](https://developer.android.com/reference/android/transition/Transition.html#createAnimator(android.view.ViewGroup, android.transition.TransitionValues, android.transition.TransitionValues))
>
> - **Capture View Property Values**
>
> Transition animations use the property animation system described in [Property Animation](https://developer.android.com/guide/topics/graphics/prop-animation.html). Property animations change a view property between a starting and ending value over a specified period of time, so the framework needs to have both the starting and ending value of the property to construct the animation.
>
> However, a property animation usually needs only a small subset of all the view's property values. For example, a color animation needs color property values, while a movement animation needs position property values. Since the property values needed for an animation are specific to a transition, the transitions framework does not provide every property value to a transition. Instead, the framework invokes callback methods that allow a transition to capture only the property values it needs and store them in the framework.
>> - Capture Starting Values
>>
>> To pass the starting view values to the framework, implement the [`captureStartValues(transitionValues)`](https://developer.android.com/reference/android/transition/Transition.html#captureStartValues(android.transition.TransitionValues)) method. The framework calls this method for every view in the starting scene. The method argument is a [`TransitionValues`](https://developer.android.com/reference/android/transition/TransitionValues.html) object that contains a reference to the view and a [`Map`](https://developer.android.com/reference/java/util/Map.html) instance in which you can store the view values you want. In your implementation, retrieve these property values and pass them back to the framework by storing them in the map.
>>
>> To ensure that the key for a property value does not conflict with other [`TransitionValues`](https://developer.android.com/reference/android/transition/TransitionValues.html) keys, use the following naming scheme:
>>
>> `package_name:transition_name:property_name`
>>
>> - Capture Ending Values
>>
>> The framework calls the [`captureEndValues(TransitionValues)`](https://developer.android.com/reference/android/transition/Transition.html#captureEndValues(android.transition.TransitionValues)) method once for every target view in the ending scene. In all other respects, [`captureEndValues()`](https://developer.android.com/reference/android/transition/Transition.html#captureEndValues(android.transition.TransitionValues)) works the same as [`captureStartValues()`](https://developer.android.com/reference/android/transition/Transition.html#captureStartValues(android.transition.TransitionValues)).
>>
>> In this example, both the [`captureStartValues()`](https://developer.android.com/reference/android/transition/Transition.html#captureStartValues(android.transition.TransitionValues)) and [`captureEndValues()`](https://developer.android.com/reference/android/transition/Transition.html#captureEndValues(android.transition.TransitionValues)) methods invoke `captureValues()` to retrieve and store values. The view property that `captureValues()` retrieves is the same, but it has different values in the starting and ending scenes. The framework maintains separate maps for the starting and ending states of a view.
>
> - **Create a Custom Animator**
>
> To animate the changes to a view between its state in the starting scene and its state in the ending scene, you provide an animator by overriding the [`createAnimator()`](https://developer.android.com/reference/android/transition/Transition.html#createAnimator(android.view.ViewGroup, android.transition.TransitionValues, android.transition.TransitionValues)) method. When the framework calls this method, it passes in the scene root view and the TransitionValues objects that contain the starting and ending values you captured.
>
> The number of times the framework calls the [`createAnimator()`](https://developer.android.com/reference/android/transition/Transition.html#createAnimator(android.view.ViewGroup, android.transition.TransitionValues, android.transition.TransitionValues)) method depends on the changes that occur between the starting and ending scenes. For example, consider a fade out/fade in animation implemented as a custom transition. If the starting scene has five targets of which two are removed from the ending scene, and the ending scene has the three targets from the starting scene plus a new target, then the framework calls [`createAnimator()`](https://developer.android.com/reference/android/transition/Transition.html#createAnimator(android.view.ViewGroup, android.transition.TransitionValues, android.transition.TransitionValues)) six times: three of the calls animate the fading out and fading in of the targets that stay in both scene objects; two more calls animate the fading out of the targets removed from the ending scene; and one call animates the fading in of the new target in the ending scene.
>
> For target views that exist on both the starting and ending scenes, the framework provides a [`TransitionValues`](https://developer.android.com/reference/android/transition/TransitionValues.html) object for both the `startValues` and `endValues` arguments. For target views that only exist in the starting or the ending scene, the framework provides a [`TransitionValues`](https://developer.android.com/reference/android/transition/TransitionValues.html) object for the corresponding argument and null for the other.
>
> To implement the [`createAnimator(ViewGroup, TransitionValues, TransitionValues)`](https://developer.android.com/reference/android/transition/Transition.html#createAnimator(android.view.ViewGroup, android.transition.TransitionValues, android.transition.TransitionValues)) method when you create a custom transition, use the view property values you captured to create an [`Animator`](https://developer.android.com/reference/android/animation/Animator.html) object and return it to the framework. For an example implementation, see the [`ChangeColor`](https://developer.android.com/samples/CustomTransition/src/com.example.android.customtransition/ChangeColor.html) class in the [CustomTransition](https://developer.android.com/samples/CustomTransition/index.html) sample. For more information about property animators, see [Property Animation](https://developer.android.com/guide/topics/graphics/prop-animation.html).
>
> - **Apply a Custom Transition**
>
> Custom transitions work the same as built-in transitions. You can apply a custom transition using a transition manager, as described in [`Applying a Transition`](https://developer.android.com/training/transitions/transitions.html#Apply).
