package com.rupan3th.manualcamerax.api;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import com.rupan3th.manualcamerax.capture.CaptureEventsListener;
import com.rupan3th.manualcamerax.processing.ProcessingEventsListener;

import java.io.File;

/**
 * Class primarily responsible for interfacing between camera/capture/processing related events and the UI
 */
public abstract class CameraEventsListener implements CaptureEventsListener, ProcessingEventsListener {
    protected String TAG = "CameraEventsListener";

    protected void logD(String msg) {
        Log.d(TAG, msg);
    }

    protected void logE(String msg) {
        Log.e(TAG, msg);
    }

    public abstract void onOpenCamera(CameraManager cameraManager);

    public abstract void onCameraRestarted();

    public abstract void onCharacteristicsUpdated(CameraCharacteristics characteristics);

    public abstract void onError(Object o);

    public abstract void onFatalError(String errorMsg);

    public abstract void onRequestTriggerMediaScanner(File f);
}
