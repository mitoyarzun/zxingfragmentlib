package com.welcu.android.zxingfragmentlibsample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;
import com.welcu.android.zxingfragmentsample.R;

/**
 * Created by mito on 9/17/13.
 */
public class SampleActivity extends FragmentActivity {

    boolean torchState = false;

    Button mToggleButton;
    BarCodeScannerFragment mScannerFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        FragmentManager fm = getSupportFragmentManager();
        mScannerFragment = (BarCodeScannerFragment) fm.findFragmentById(R.id.scanner_fragment);

        mToggleButton = (Button) findViewById(R.id.button_flash);
        mToggleButton.setOnClickListener(createToggleFlashListener());
    }

    private View.OnClickListener createToggleFlashListener() {
        return new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            torchState = !torchState;
            mScannerFragment.setTorch(torchState);
          }
        };
    }
}