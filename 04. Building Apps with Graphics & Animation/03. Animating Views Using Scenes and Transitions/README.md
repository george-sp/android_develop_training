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
