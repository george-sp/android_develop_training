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
> Create the two views that you want to crossfade. The following example creates a progress indicator and a scrollable text view.
>
> **Set up the Animation**
>
> To set up the animation:
>
> 1. Create member variables for the views that you want to crossfade. You need these references later when modifying the views during the animation.
> 2. For the view that is being faded in, set its visibility to [`GONE`](https://developer.android.com/reference/android/view/View.html#GONE). This prevents the view from taking up layout space and omits it from layout calculations, speeding up processing.
> 3. Cache the [`config_shortAnimTime`](https://developer.android.com/reference/android/R.integer.html#config_shortAnimTime) system property in a member variable. This property defines a standard "short" duration for the animation. This duration is ideal for subtle animations or animations that occur very frequently. [`config_longAnimTime`](https://developer.android.com/reference/android/R.integer.html#config_longAnimTime) and [`config_mediumAnimTime`](https://developer.android.com/reference/android/R.integer.html#config_mediumAnimTime) are also available if you wish to use them.
>
> **Crossfade the Views**
>
> Now that the views are properly set up, crossfade them by doing the following:
>
> 1. For the view that is fading in, set the alpha value to `0` and the visibility to [`VISIBLE`](https://developer.android.com/reference/android/view/View.html#VISIBLE). (Remember that it was initially set to [`GONE`](https://developer.android.com/reference/android/view/View.html#GONE).) This makes the view visible but completely transparent.
> 2. For the view that is fading in, animate its alpha value from `0` to `1`. At the same time, for the view that is fading out, animate the alpha value from `1` to `0`.
> 3. Using [`onAnimationEnd()`](https://developer.android.com/reference/android/animation/Animator.AnimatorListener.html#onAnimationEnd(android.animation.Animator)) in an [`Animator.AnimatorListener`](https://developer.android.com/reference/android/animation/Animator.AnimatorListener.html), set the visibility of the view that was fading out to [`GONE`](https://developer.android.com/reference/android/view/View.html#GONE). Even though the alpha value is `0`, setting the view's visibility to [`GONE`](https://developer.android.com/reference/android/view/View.html#GONE) prevents the view from taking up layout space and omits it from layout calculations, speeding up processing.

#### Using ViewPager for Screen Slides
Learn how to animate between horizontally adjacent screens with a sliding transition.

This lesson shows you how to do screen slides with a [`ViewPager`](https://developer.android.com/reference/android/support/v4/view/ViewPager.html) provided by the [support library](https://developer.android.com/topic/libraries/support-library/index.html). [`ViewPagers`](https://developer.android.com/reference/android/support/v4/view/ViewPager.html) can animate screen slides automatically.

Screen slides are transitions between one entire screen to another and are common with UIs like setup wizards or slideshows.

> **Create the Views**
>
> Create a layout file that you'll later use for the content of a fragment. The following example contains a text view to display some text.
>
> **Create the Fragment**
>
> Create a [`Fragment`](https://developer.android.com/reference/android/support/v4/app/Fragment.html) class that returns the layout that you just created in the [`onCreateView()`](https://developer.android.com/reference/android/app/Fragment.html#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)) method. You can then create instances of this fragment in the parent activity whenever you need a new page to display to the user
>
> **Add a ViewPager**
>
> [`ViewPagers`](https://developer.android.com/reference/android/support/v4/view/ViewPager.html) have built-in swipe gestures to transition through pages, and they display screen slide animations by default, so you don't need to create any. [`ViewPagers`](https://developer.android.com/reference/android/support/v4/view/ViewPager.html) use [`PagerAdapters`](https://developer.android.com/reference/android/support/v4/view/PagerAdapter.html) as a supply for new pages to display, so the [`PagerAdapters`](https://developer.android.com/reference/android/support/v4/view/PagerAdapter.html) will use the fragment class that you created earlier.
>
> Create an activity that does the following things:
>
> - Sets the content view to be the layout with the [`ViewPager`](https://developer.android.com/reference/android/support/v4/view/ViewPager.html).
> - Creates a class that extends the [`FragmentStatePagerAdapter`](https://developer.android.com/reference/android/support/v13/app/FragmentStatePagerAdapter.html) abstract class and implements the [`getItem()`](https://developer.android.com/reference/android/support/v4/app/FragmentStatePagerAdapter.html#getItem(int)) method to supply instances of `ScreenSlidePageFragment` as new pages. The pager adapter also requires that you implement the [`getCount()`](https://developer.android.com/reference/android/support/v4/view/PagerAdapter.html#getCount()) method, which returns the amount of pages the adapter will create (five in the example).
> - Hooks up the [`PagerAdapter`](https://developer.android.com/reference/android/support/v4/view/PagerAdapter.html) to the [`ViewPager`](https://developer.android.com/reference/android/support/v4/view/ViewPager.html).
> - Handles the device's back button by moving backwards in the virtual stack of fragments. If the user is already on the first page, go back on the activity back stack.
>
> **Customize the Animation with PageTransformer**
>
> To display a different animation from the default screen slide animation, implement the [`ViewPager.PageTransformer`](https://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html) interface and supply it to the view pager. The interface exposes a single method, [`transformPage()`](https://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html#transformPage(android.view.View, float)). At each point in the screen's transition, this method is called once for each visible page (generally there's only one visible page) and for adjacent pages just off the screen. For example, if page three is visible and the user drags towards page four, [`transformPage()`](https://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html#transformPage(android.view.View, float)) is called for pages two, three, and four at each step of the gesture.
>
> In your implementation of [`transformPage()`](https://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html#transformPage(android.view.View, float)), you can then create custom slide animations by determining which pages need to be transformed based on the position of the page on the screen, which is obtained from the `position` parameter of the [`transformPage()`](https://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html#transformPage(android.view.View, float)) method.
>
> The `position` parameter indicates where a given page is located relative to the center of the screen. It is a dynamic property that changes as the user scrolls through the pages. When a page fills the screen, its position value is `0`. When a page is drawn just off the right side of the screen, its position value is `1`. If the user scrolls halfway between pages one and two, page one has a position of -0.5 and page two has a position of 0.5. Based on the position of the pages on the screen, you can create custom slide animations by setting page properties with methods such as [`setAlpha()`](https://developer.android.com/reference/android/view/View.html#setAlpha(float)), [`setTranslationX()`](https://developer.android.com/reference/android/view/View.html#setTranslationX(float)), or [`setScaleY()`](https://developer.android.com/reference/android/view/View.html#setScaleY(float)).
>
> When you have an implementation of a [`PageTransformer`](https://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html), call [`setPageTransformer()`](https://developer.android.com/reference/android/support/v4/view/ViewPager.html#setPageTransformer(boolean, android.support.v4.view.ViewPager.PageTransformer)) with your implementation to apply your custom animations. For example, if you have a [`PageTransformer`](https://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html) named `ZoomOutPageTransformer`, you can set your custom animations like this:
>
> ```
ViewPager mPager = (ViewPager) findViewById(R.id.pager);
...
mPager.setPageTransformer(true, new ZoomOutPageTransformer());
```
>
>> - Zoom-out page transformer
>>
>> This page transformer shrinks and fades pages when scrolling between adjacent pages. As a page gets closer to the center, it grows back to its normal size and fades in.
>>
>> - Depth page transformer
>>
>> This page transformer uses the default slide animation for sliding pages to the left, while using a "depth" animation for sliding pages to the right. This depth animation fades the page out, and scales it down linearly.
>>
>>> **Note:** During the depth animation, the default animation (a screen slide) still takes place, so you must counteract the screen slide with a negative X translation. For example:
>>
>> ```
view.setTranslationX(-1 * view.getWidth() * position);
```

#### Displaying Card Flip Animations
Learn how to animate between two views with a flipping motion.

This lesson shows you how to do a card flip animation with custom fragment animations. Card flips animate between views of content by showing an animation that emulates a card flipping over.

> **Create the Animators**
>
> Create the animations for the card flips. You'll need two animators for when the front of the card animates out and to the left and in and from the left. You'll also need two animators for when the back of the card animates in and from the right and out and to the right.
>
> **Create the Views**
>
> Each side of the "card" is a separate layout that can contain any content you want, such as two screens of text, two images, or any combination of views to flip between. You'll then use the two layouts in the fragments that you'll later animate. The following layouts create one side of a card that shows text, and the other side of the card that displays an [`ImageView`](https://developer.android.com/reference/android/widget/ImageView.html).
>
> **Create the Fragment**
>
> Create fragment classes for the front and back of the card. These classes return the layouts that you created previously in the [`onCreateView()`](https://developer.android.com/reference/android/app/Fragment.html#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)) method of each fragment. You can then create instances of this fragment in the parent activity where you want to show the card. The example shows nested fragment classes inside of the parent activity that uses them.
>
> **Animate the Card Flip**
>
> Now, you'll need to display the fragments inside of a parent activity. To do this, first create the layout for your activity. The example creates a [`FrameLayout`](https://developer.android.com/reference/android/widget/FrameLayout.html) that you can add fragments to at runtime.
> In the activity code, set the content view to be the layout that you just created. It's also good idea to show a default fragment when the activity is created, so the example activity shows you how to display the front of the card by default.
> Now that you have the front of the card showing, you can show the back of the card with the flip animation at an appropriate time. Create a method to show the other side of the card that does the following things:
>
> - Sets the custom animations that you created earlier for the fragment transitions.
> - Replaces the currently displayed fragment with a new fragment and animates this event with the custom animations that you created.
> - Adds the previously displayed fragment to the fragment back stack so when the user presses the _Back_ button, the card flips back over.

#### Zooming a View
Learn how to enlarge views with a touch-to-zoom animation.

This lesson demonstrates how to do a touch-to-zoom animation, which is useful for apps such as photo galleries to animate a view from a thumbnail to a full-size image that fills the screen.

> **Create the Views**
>
> Create a layout file that contains the small and large version of the content that you want to zoom. Thia example creates an [`ImageButton`](https://developer.android.com/reference/android/widget/ImageButton.html) for clickable image thumbnail and an [`ImageView`](https://developer.android.com/reference/android/widget/ImageView.html) that displays the enlarged view of the image.
>
> **Set up the Zoom Animation**
>
> Once you apply your layout, set up the event handlers that trigger the zoom animation. The following example adds a [`View.OnClickListener`](https://developer.android.com/reference/android/view/View.OnClickListener.html) to the [`ImageButton`](https://developer.android.com/reference/android/widget/ImageButton.html) to execute the zoom animation when the user clicks the image button
>
> **Zoom the View**
>
> You'll now need to animate from the normal sized view to the zoomed view when appropriate. In general, you need to animate from the bounds of the normal-sized view to the bounds of the larger-sized view. The following method shows you how to implement a zoom animation that zooms from an image thumbnail to an enlarged view by doing the following things:
>
> 1. Assign the high-res image to the hidden "zoomed-in" (enlarged) [`ImageView`](https://developer.android.com/reference/android/widget/ImageView.html). The following example loads a large image resource on the UI thread for simplicity. You will want to do this loading in a separate thread to prevent blocking on the UI thread and then set the bitmap on the UI thread. Ideally, the bitmap should not be larger than the screen size.
> 2. Calculate the starting and ending bounds for the [`ImageView`](https://developer.android.com/reference/android/widget/ImageView.html).
> 3. Animate each of the four positioning and sizing properties [X](https://developer.android.com/reference/android/view/View.html#X), [Y](https://developer.android.com/reference/android/view/View.html#Y), ([SCALE_X](https://developer.android.com/reference/android/view/View.html#SCALE_X), and [SCALE_Y](https://developer.android.com/reference/android/view/View.html#SCALE_Y)) simultaneously, from the starting bounds to the ending bounds. These four animations are added to an [AnimatorSet](https://developer.android.com/reference/android/animation/AnimatorSet.html) so that they can be started at the same time.
> 4. Zoom back out by running a similar animation but in reverse when the user touches the screen when the image is zoomed in. You can do this by adding a [View.OnClickListener](https://developer.android.com/reference/android/view/View.OnClickListener.html) to the [ImageView](https://developer.android.com/reference/android/widget/ImageView.html). When clicked, the [ImageView](https://developer.android.com/reference/android/widget/ImageView.html) minimizes back down to the size of the image thumbnail and sets its visibility to [GONE](https://developer.android.com/reference/android/view/View.html#GONE) to hide it.

#### Animating Layout Changes
Learn how to enable built-in animations when adding, removing, or updating child views in a layout.

A layout animation is a pre-loaded animation that the system runs each time you make a change to the layout configuration. All you need to do is set an attribute in the layout to tell the Android system to animate these layout changes, and system-default animations are carried out for you.

> **Tip:** If you want to supply custom layout animations, create a [`LayoutTransition`](https://developer.android.com/reference/android/animation/LayoutTransition.html) object and supply it to the layout with the [`setLayoutTransition()`](https://developer.android.com/reference/android/view/ViewGroup.html#setLayoutTransition(android.animation.LayoutTransition)) method.

> **Create the Layout**
>
> In your activity's layout XML file, set the `android:animateLayoutChanges` attribute to `true` for the layout that you want to enable animations for. For instance:

> ```
<LinearLayout android:id="@+id/container"
    android:animateLayoutChanges="true"
    ...
/>
```
