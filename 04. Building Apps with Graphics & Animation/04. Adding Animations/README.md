### Add Animations
Learn how to add transitional animations to your user interface.

This training class shows you how to implement some common types of animations that can increase usability and add flair without annoying your users.

Animations can add subtle visual cues that notify users about what's going on in your app and improve their mental model of your app's interface. 
Animations are especially useful when the screen changes state, such as when content loads or new actions become available.
Animations can also add a polished look to your app, which gives your app a higher quality feel.

_Keep in mind though, that overusing animations or using them at the wrong time can be detrimental, such as when they cause delays._

Dependencies and prerequisites
- Android 4.0 or later
- Experience building an Android [User Interface](https://developer.android.com/guide/topics/ui/index.html)

You should also read
- [Property Animation](https://developer.android.com/guide/topics/graphics/prop-animation.html)

-----------------------------------------------------------

#### Crossfading Two Views
Learn how to crossfade between two overlapping views. This lesson shows you how to crossfade a progress indicator to a view that contains text content.

Crossfade animations (also know as dissolve) gradually fade out one UI component while simultaneously fading in another. This animation is useful for situations where you want to switch content or views in your app. Crossfades are very subtle and short but offer a fluid transition from one screen to the next. When you don't use them, however, transitions often feel abrupt or hurried.

> **Create the Views**
>
> Create the two views that you want to crossfade. The following example creates a progress indicator and a scrollable text view
>
> **Set up the Animation**
>
> To set up the animation:
>
> 1. Create member variables for the views that you want to crossfade. You need these references later when modifying the views during the animation.
> 2. For the view that is being faded in, set its visibility to [GONE](https://developer.android.com/reference/android/view/View.html#GONE). This prevents the view from taking up layout space and omits it from layout calculations, speeding up processing.
> 3. Cache the [config_shortAnimTime](https://developer.android.com/reference/android/R.integer.html#config_shortAnimTime) system property in a member variable. This property defines a standard "short" duration for the animation. This duration is ideal for subtle animations or animations that occur very frequently. [config_longAnimTime](https://developer.android.com/reference/android/R.integer.html#config_longAnimTime) and [config_mediumAnimTime](https://developer.android.com/reference/android/R.integer.html#config_mediumAnimTime) are also available if you wish to use them.
