package com.rupan3th.manualcamerax.processing.parameters;

import com.rupan3th.manualcamerax.api.CameraMode;
import com.rupan3th.manualcamerax.app.ManualCameraX;


public class FrameNumberSelector {
    public static int frameCount;
    public static int throwCount;
    public static int getFrames() {
        double lightcycle = (Math.exp(1.3595 + 0.0020 * ExposureIndex.index()*6400.0/IsoExpoSelector.getISOAnalog())) / 9;
        double target = (Math.exp(1.3595 + 0.0020 * ExposureIndex.index()*6400.0/IsoExpoSelector.getISOAnalog())) / 14;
        lightcycle *= ManualCameraX.getSettings().frameCount;
        target *= ManualCameraX.getSettings().frameCount;
        frameCount = Math.min(Math.max((int) lightcycle, 4), ManualCameraX.getSettings().frameCount);
        throwCount = Math.min(Math.max((int) target, 4), ManualCameraX.getSettings().frameCount);
        if (ManualCameraX.getSettings().selectedMode == CameraMode.UNLIMITED) frameCount = -1;
        if(ManualCameraX.getSettings().DebugData) frameCount = ManualCameraX.getSettings().frameCount;
        throwCount = (frameCount-throwCount);
        return frameCount;
    }
}
