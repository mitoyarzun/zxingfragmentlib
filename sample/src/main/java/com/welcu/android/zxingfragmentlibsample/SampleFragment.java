package com.welcu.android.zxingfragmentlibsample;

import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

/**
 * Created by mito on 9/17/13.
 */
public class SampleFragment extends BarCodeScannerFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setmCallBack(new IResultCallback() {
            @Override
            public void result(Result lastResult) {
                Toast.makeText(getActivity(), "Scan: " + lastResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public SampleFragment() {

    }

    @Override
    public int getRequestedCameraId() {
        return -1; // set to 1 to use the front camera (won't work if the device doesn't have one, it is up to you to handle this method ;)
    }
}