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

package com.welcu.android.zxingfragmentlib;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Map;

final class DecodeHandler extends Handler implements IConstants {

  private static final String TAG = DecodeHandler.class.getSimpleName();

  private final BarCodeScannerFragment fragment;
  private final MultiFormatReader multiFormatReader;
  private boolean running = true;

  DecodeHandler(BarCodeScannerFragment fragment, Map<DecodeHintType, Object> hints) {
    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(hints);
    this.fragment = fragment;
  }

  @Override
  public void handleMessage(Message message) {
    if (!running) {
      return;
    }
    switch (message.what) {
      case DECODE:
        decode((byte[]) message.obj, message.arg1, message.arg2);
        break;
      case QUIT:
        running = false;
        Looper.myLooper().quit();
        break;
    }
  }

  /**
   * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
   * reuse the same reader objects from one decode to the next.
   *
   * @param data   The YUV preview frame.
   * @param width  The width of the preview frame.
   * @param height The height of the preview frame.
   */
  private void decode(byte[] data, int width, int height) {
    long start = System.currentTimeMillis();
    Result rawResult = null;
    PlanarYUVLuminanceSource source = fragment.getCameraManager().buildLuminanceSource(data, width, height);
//    PlanarYUVLuminanceSource source = fragment.getCameraManager().buildLuminanceSource(data, fragment.getView().getWidth(), fragment.getView().getHeight());
    if (source != null) {
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      try {
        rawResult = multiFormatReader.decodeWithState(bitmap);
      } catch (ReaderException re) {
        // continue
      } finally {
        multiFormatReader.reset();
      }
    }

    Handler handler = fragment.getHandler();
    if (rawResult != null) {
      // Don't log the barcode contents for security.
      long end = System.currentTimeMillis();
      Log.d(TAG, "Found barcode in " + (end - start) + " ms");
      if (handler != null) {
        Message message = Message.obtain(handler, DECODE_SUCCEDED, rawResult);
        Bundle bundle = new Bundle();
//        bundleThumbnail(source, bundle);
        message.setData(bundle);
        message.sendToTarget();
      }
    } else {
      if (handler != null) {
        Message message = Message.obtain(handler, DECODE_FAILED);
        message.sendToTarget();
      }
    }
  }

//  private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
//    int[] pixels = source.renderThumbnail();
//    int width = source.getThumbnailWidth();
//    int height = source.getThumbnailHeight();
//    Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
//    bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
//    bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / source.getWidth());
//  }

}
