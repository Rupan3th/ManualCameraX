package com.rupan3th.manualcamerax.processing.opengl.postpipeline;

import android.hardware.camera2.CaptureResult;
import android.util.Log;

import com.rupan3th.manualcamerax.processing.opengl.nodes.Node;
import com.rupan3th.manualcamerax.processing.parameters.IsoExpoSelector;
import com.rupan3th.manualcamerax.settings.PreferenceKeys;
import com.rupan3th.manualcamerax.capture.CaptureController;

public class Sharpen extends Node {
    public Sharpen(int rid) {
        super(rid, "Sharpening");
    }
    @Override
    public void Run() {
        float sharpnessLevel = (float) Math.sqrt((CaptureController.mCaptureResult.get(CaptureResult.SENSOR_SENSITIVITY)) * IsoExpoSelector.getMPY() - 50.) / 14.2f;
        sharpnessLevel = Math.max(0.5f, sharpnessLevel);
        sharpnessLevel = Math.min(1.5f, sharpnessLevel);
        Log.d("PostNode:" + Name, "sharpnessLevel:" + sharpnessLevel + " iso:" + CaptureController.mCaptureResult.get(CaptureResult.SENSOR_SENSITIVITY));
        glProg.setVar("size", 1.75f);
        glProg.setVar("strength", PreferenceKeys.getSharpnessValue());
        glProg.setTexture("InputBuffer", previousNode.WorkingTexture);
        WorkingTexture = basePipeline.getMain();
    }
}
