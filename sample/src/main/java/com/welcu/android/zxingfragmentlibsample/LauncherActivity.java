package com.welcu.android.zxingfragmentlibsample;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.welcu.android.zxingfragmentsample.R;

public class LauncherActivity extends ActionBarActivity {

    private Button buttonSimpleScanner, buttonNestedScanner, buttonStretchedScanner;

    private final static int SIMPLE = 0;
    private final static int NESTED = 1;
    private final static int STRETCHED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        buttonSimpleScanner = (Button) findViewById(R.id.button_simple_scanner);
        buttonNestedScanner = (Button) findViewById(R.id.button_nested_scanner);
        buttonStretchedScanner = (Button) findViewById(R.id.button_stretched_fragment);

        buttonSimpleScanner.setOnClickListener(createScannerLauncherClickListener(SIMPLE));
        buttonNestedScanner.setOnClickListener(createScannerLauncherClickListener(NESTED));
        buttonStretchedScanner.setOnClickListener(createScannerLauncherClickListener(STRETCHED));
    }

    private View.OnClickListener createScannerLauncherClickListener(final int scannerType) {
      return new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Class activityClass = null;
          switch(scannerType) {
            case SIMPLE:
              activityClass = SampleActivity.class;
              break;
            case NESTED:
              activityClass = SampleNestedFragmentActivity.class;
              break;
            case STRETCHED:
              activityClass = SampleStretchedActivity.class;
              break;
          }
          Intent i = new Intent(view.getContext(), activityClass);
          startActivity(i);
        }
      };
    }
}
