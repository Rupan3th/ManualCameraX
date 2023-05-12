package com.rupan3th.manualcamerax.processing.parameters;

import com.rupan3th.manualcamerax.app.ManualCameraX;

import java.util.Locale;

public class ExposureIndex {
    public static final long sec = 1000000000;

    public static double index() {
        long exposureTime = ManualCameraX.getCaptureController().mPreviewExposureTime;
        int iso = ManualCameraX.getCaptureController().mPreviewIso;
        double time = (double) (exposureTime) / sec;
        return iso * time;
    }

    public static double time2sec(long in) {
        return ((double) in) / sec;
    }

    public static String sec2string(double in) {
        if (in > 1.0) return String.format(Locale.US, "%.2f", in);
        else {
            in = 1.0 / in;
            return "1/" + String.format(Locale.US, "%.0f", in);
        }
    }

    public static long sec2time(double in) {
        return (long) (in * sec);
    }
}
