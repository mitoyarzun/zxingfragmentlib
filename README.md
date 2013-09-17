# ZXing Fragment Library

This library allows you to embed a Barcode scanner as a Fragment.

Most of the code was taken from the official [ZXing repository](https://code.google.com/p/zxing/), this is basically a re-implementation of CaptureActivity.java.

This was also inspired by another similar library, [barcodefragmentlib](https://code.google.com/p/barcodefragmentlib/).

## Usage

1. Add the library as a module dependency to your app.
2. Extend the BarCodeScannerFragment class and add a callback somewhere:

    ```java
    this.setmCallBack(new BarCodeScannerFragment.IResultCallback() {
        @Override
        public void result(Result lastResult) {
            Log.v("zxingfragmentlib", lastResult.toString());
        }
    });
    ```
3. Look a the included sample for better reference.

## Collaboration

Any code improvements are appreciated, just submit a pull request.

## TODO

* Add a LICENSE file (Apache 2.0 ?)
* Remove unused XMLs
* Remove PreferencesActivity
