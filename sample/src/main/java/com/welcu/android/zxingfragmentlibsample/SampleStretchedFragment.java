package com.welcu.android.zxingfragmentlibsample;

import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

/**
 * Created by joyarzun on 4/8/14.
 */
public class SampleStretchedFragment extends BarCodeScannerFragment {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setmCallBack(new IResultCallback() {
      @Override
      public void result(Result lastResult) {
        Toast.makeText(getActivity(), "Scan: " + lastResult.toString(), Toast.LENGTH_SHORT).show();
      }
    });
    //cameraManager.setManualFramingRect(450, 349, 100, 50);
  }

  public SampleStretchedFragment() {

  }
}