# ZXing Fragment Library

This library allows you to embed a Barcode scanner as a Fragment.

Most of the code was taken from the official [ZXing repository](https://code.google.com/p/zxing/), this is basically a re-implementation of CaptureActivity.java.

This was also inspired by another similar library, [barcodefragmentlib](https://code.google.com/p/barcodefragmentlib/).

## WARNING

I'm currently updating this library, I've updated the original ZXing sources to the latest version and there might be some incompatibilities with older versions of Android.

Please browse the ```before-rebuild``` branch to get the previous version.

There are some bugs that prevent the scanning in portrait mode (especially with 1D codes, QR and others should be ok).

I've tested the code and it's working (at least) in Android 2.1 (Eclair), 2.3 (Gingerbread) and 4.4 (KitKat). If yours doesn't work, please create an issue.

## Goals

Here are the basic guidelines for this project. As always, suggestions are appreciated :)

* Provide a simple library to scan codes using ZXing's core library.
* Be compatible with Android 2.1 and up.
* Work out of the box (or almost).
* Facilitate customization and/or configuration.

## Usage

1. Add the library as a module dependency to your app.
2. Add the ZXing core library (the jar is included in libs/zxing-core-2.3.jar)
3. Add the following permissions to your AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.CAMERA" />
 	<uses-feature android:name="android.hardware.camera" />
 	<uses-feature android:name="android.hardware.camera.autofocus" />
 	<uses-feature android:name="android.hardware.camera.flash" />
```

4. Extend the BarCodeScannerFragment class and add a callback somewhere:

    ```java
    this.setmCallBack(new BarCodeScannerFragment.IResultCallback() {
        @Override
        public void result(Result lastResult) {
            Log.v("zxingfragmentlib", lastResult.toString());
        }
    });
    ```
5. Enjoy!

A sample activity is included, look at SampleActivity.java, SampleFragment.java and sample_activity.xml to get an idea.


## Contributing

Any code improvements and bug reports are appreciated, just submit a pull request or open an issue.

## TODO

* Remove unused XMLs
* Remove PreferencesActivity
* Add front-camera selector
* Test in more devices

## Author

Jaime Oyarzun <jaime@welcu.com>

The original ZXing project is at https://github.com/zxing/zxing
