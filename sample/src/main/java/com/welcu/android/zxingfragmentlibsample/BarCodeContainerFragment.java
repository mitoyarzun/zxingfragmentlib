package com.welcu.android.zxingfragmentlibsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.welcu.android.zxingfragmentsample.R;

/**
 * Created by joyarzun on 2/5/14.
 */

public class BarCodeContainerFragment extends Fragment {
  public BarCodeContainerFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_nested_fragment, container, false);
  }
}