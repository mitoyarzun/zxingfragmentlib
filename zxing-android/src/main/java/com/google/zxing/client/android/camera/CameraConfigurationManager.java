/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.camera;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.google.zxing.client.android.PreferencesActivity;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
final class CameraConfigurationManager {

  private static final String TAG = "CameraConfiguration";

  private final Context context;
  private Point screenResolution;
  private Point cameraResolution;

  CameraConfigurationManager(Context context) {
    this.context = context;
  }

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */
  void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    Point theScreenResolution = new Point();

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
      theScreenResolution = new Point(display.getWidth(), display.getHeight());
    } else {
      display.getSize(theScreenResolution);
    }

    int orientation = 0;

    if (getScreenOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
      orientation = 90;
    } else if (getScreenOrientation()==ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
      orientation = 180;
    }

    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD) {
      camera.setDisplayOrientation(orientation);
    } else {
      if (getScreenOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        parameters.set("orientation", "portrait");
      } else {
        parameters.set("orientation", "landscape");
      }
      camera.setParameters(parameters);
    }

    screenResolution = theScreenResolution;
    Log.i(TAG, "Screen resolution: " + screenResolution);
    cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
    Log.i(TAG, "Camera resolution: " + cameraResolution);
  }

  void setDesiredCameraParameters(Camera camera, boolean safeMode) {
    Camera.Parameters parameters = camera.getParameters();

    if (parameters == null) {
      Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
      return;
    }

    Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

    if (safeMode) {
      Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
    }

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    initializeTorch(parameters, prefs, safeMode);

    CameraConfigurationUtils.setFocus(
        parameters,
        prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true),
        prefs.getBoolean(PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS, true),
        safeMode);

    if (!safeMode) {
      if (prefs.getBoolean(PreferencesActivity.KEY_INVERT_SCAN, false)) {
        CameraConfigurationUtils.setInvertColor(parameters);
      }

      if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_BARCODE_SCENE_MODE, true)) {
        CameraConfigurationUtils.setBarcodeSceneMode(parameters);
      }

      if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_METERING, true)) {
        CameraConfigurationUtils.setVideoStabilization(parameters);
        CameraConfigurationUtils.setFocusArea(parameters);
        CameraConfigurationUtils.setMetering(parameters);
      }

    }

    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);

    Log.i(TAG, "Final camera parameters: " + parameters.flatten());

    camera.setParameters(parameters);

    Camera.Parameters afterParameters = camera.getParameters();
    Camera.Size afterSize = afterParameters.getPreviewSize();
    if (afterSize!= null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
      Log.w(TAG, "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y +
                 ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
      cameraResolution.x = afterSize.width;
      cameraResolution.y = afterSize.height;
    }
  }

  Point getCameraResolution() {
    return cameraResolution;
  }

  Point getScreenResolution() {
    return screenResolution;
  }

  boolean getTorchState(Camera camera) {
    if (camera != null) {
      Camera.Parameters parameters = camera.getParameters();
      if (parameters != null) {
        String flashMode = parameters.getFlashMode();
        return flashMode != null &&
            (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
             Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
      }
    }
    return false;
  }

  void setTorch(Camera camera, boolean newSetting) {
    Camera.Parameters parameters = camera.getParameters();
    doSetTorch(parameters, newSetting, false);
    camera.setParameters(parameters);
  }

  private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode) {
    boolean currentSetting = FrontLightMode.readPref(prefs) == FrontLightMode.ON;
    doSetTorch(parameters, currentSetting, safeMode);
  }

  private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
    CameraConfigurationUtils.setTorch(parameters, newSetting);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    if (!safeMode && !prefs.getBoolean(PreferencesActivity.KEY_DISABLE_EXPOSURE, true)) {
      CameraConfigurationUtils.setBestExposure(parameters, newSetting);
    }
  }


  // Taken from http://stackoverflow.com/a/10383164/902599
  private int getScreenOrientation() {
    int rotation;
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD) {
        rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
    } else {
        rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getOrientation();
    }
    DisplayMetrics dm = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    int orientation;
    // if the device's natural orientation is portrait:
    if ((rotation == Surface.ROTATION_0
            || rotation == Surface.ROTATION_180) && height > width ||
            (rotation == Surface.ROTATION_90
                    || rotation == Surface.ROTATION_270) && width > height) {
      switch(rotation) {
        case Surface.ROTATION_0:
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
        case Surface.ROTATION_90:
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
        case Surface.ROTATION_180:
          orientation =
                  ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
          break;
        case Surface.ROTATION_270:
          orientation =
                  ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
          break;
        default:
          Log.e(TAG, "Unknown screen orientation. Defaulting to " +
                  "portrait.");
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
      }
    }
    // if the device's natural orientation is landscape or if the device
    // is square:
    else {
      switch(rotation) {
        case Surface.ROTATION_0:
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
        case Surface.ROTATION_90:
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
        case Surface.ROTATION_180:
          orientation =
                  ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
          break;
        case Surface.ROTATION_270:
          orientation =
                  ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
          break;
        default:
          Log.e(TAG, "Unknown screen orientation. Defaulting to " +
                  "landscape.");
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
      }
    }
    Log.v(TAG, "Orientation: " + orientation);
    return orientation;
  }

}
