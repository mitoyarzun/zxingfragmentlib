package com.welcu.android.zxingfragmentlibsample;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;
import com.welcu.android.zxingfragmentsample.R;

/**
 * Created by joyarzun on 4/8/14.
 */
public class SampleStretchedActivity extends FragmentActivity {
  boolean torchState = false;

  LinearLayout layoutContent;
  Button mToggleButton;
  BarCodeScannerFragment mScannerFragment;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stretched_sample);

    FragmentManager fm = getSupportFragmentManager();
    mScannerFragment = (BarCodeScannerFragment) fm.findFragmentById(R.id.scanner_fragment);

    layoutContent = (LinearLayout) findViewById(R.id.layout_content);

    final ViewTreeObserver observer = layoutContent.getViewTreeObserver();

    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        // We're assuming that the other layout is under the scanner
        int activityWidth = layoutContent.getWidth();
        int activityHeight = findViewById(R.id.scanner_fragment).getHeight();

        int usableWidth = layoutContent.getWidth();
        int usableHeight = activityHeight - layoutContent.getHeight();

        int desiredHeight = (int) (usableHeight * 0.8);
        int desiredWidth = (int) (usableWidth * 0.75);

        Rect framingRect = new Rect(
            (usableWidth - desiredWidth) / 2, // left
            (usableHeight - desiredHeight) / 2, // top
            (usableWidth - desiredWidth) / 2 + desiredWidth, // right
            (usableHeight - desiredHeight) / 2 + desiredHeight// bottom
        );
        Log.v("RECT", "left: " + framingRect.left + " top: " + framingRect.top + " right: " + framingRect.right + " bottom: " + framingRect.bottom + " activityHeight: " + activityHeight + " activitiWidth: " + activityWidth);
//        mScannerFragment.setFramingRect(framingRect);
      }
    });

//    mToggleButton = (Button) findViewById(R.id.button_flash);
//    mToggleButton.setOnClickListener(createToggleFlashListener());

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
