# ZXing Fragment Library

This library allows you to embed a Barcode scanner as a Fragment.

Most of the code was taken from the official [ZXing repository](https://code.google.com/p/zxing/), this is basically a re-implementation of CaptureActivity.java.

This was also inspired by another similar library, [barcodefragmentlib](https://code.google.com/p/barcodefragmentlib/).

## Usage

1. Add the library as a module dependency to your app.
2. Add the following permissions to your AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.CAMERA" />
 	<uses-feature android:name="android.hardware.camera" />
 	<uses-feature android:name="android.hardware.camera.autofocus" />
 	<uses-feature android:name="android.hardware.camera.flash" />
```

3. Extend the BarCodeScannerFragment class and add a callback somewhere:

    ```java
    this.setmCallBack(new BarCodeScannerFragment.IResultCallback() {
        @Override
        public void result(Result lastResult) {
            Log.v("zxingfragmentlib", lastResult.toString());
        }
    });
    ```
4. Enjoy!

A sample activity is included, look at SampleActivity.java, SampleFragment.java and sample_activity.xml to get an idea.

If you have any questions contact me (Jaime) at android@welcu.com.

## Contributing

Any code improvements and bug reports are appreciated, just submit a pull request or open an issue.

## TODO

* Add a LICENSE file (Apache 2.0 ?)
* Remove unused XMLs
* Remove PreferencesActivity
* Add a gradle build file

## Author

Jaime Oyarzun <jaime@welcu.com>

