package com.welcu.android.zxingfragmentlib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.zxing.Result;

/**
 * Created by mito on 9/17/13.
 */
public class SampleFragment extends BarCodeScannerFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setmCallBack(new IResultCallback() {
            @Override
            public void result(Result lastResult) {
                Toast.makeText(getActivity(), "Scan: " + lastResult.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}