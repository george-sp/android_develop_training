### Managing Audio Playback
If your app plays audio, it’s important that your users can control the audio in a predictable manner. 
To ensure a great user experience, it’s also important that your app manages the audio focus to ensure multiple apps aren’t playing audio at the same time. 

Dependencies and prerequisites
- Android 2.0 (API level 5) or higher
- Experience with [Media Playback](https://developer.android.com/guide/topics/media/mediaplayer.html)

-----------------------------------------------------------

#### Controlling Your App’s Volume and Playback
- Learn how to ensure your users can control the volume of your app using the hardware or software volume controls and where available the play, stop, pause, skip, and previous media playback keys.

A good user experience is a predictable one. If your app plays media it’s important that your users can control the volume of your app using the hardware or software volume controls of their device, bluetooth headset, or headphones.

#### Managing Audio Focus
- Learn how to request the audio focus, listen for a loss of audio focus, and how to respond when that happens.

With multiple apps potentially playing audio it's important to think about how they should interact. To avoid every music app playing at the same time, Android uses audio focus to moderate audio playback—only apps that hold the audio focus should play audio.

_Before your app starts playing audio it should request—and receive—the audio focus. Likewise, it should know how to listen for a loss of audio focus and respond appropriately when that happens._

#### Dealing with Audio Output Hardware
- Learn how to find out where the audio is being played and how to handle a headset being disconnected during playback.

Audio can be played from a number of sources.

Users have a number of alternatives when it comes to enjoying the audio from their Android devices. 
Most devices have a _built-in speaker_, _headphone jacks_ for wired headsets, and many also feature _Bluetooth connectivity_ and support for A2DP audio.
