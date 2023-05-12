package com.rupan3th.manualcamerax.processing.opengl.postpipeline;

import android.hardware.camera2.CaptureResult;
import android.util.Log;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.capture.CaptureController;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.opengl.nodes.Node;
import com.rupan3th.manualcamerax.processing.parameters.IsoExpoSelector;
import com.rupan3th.manualcamerax.settings.PreferenceKeys;

public class SharpenDual extends Node {
    public SharpenDual() {
        super(0, "Sharpening");
    }

    @Override
    public void Compile() {
    }
    float blurSize = 0.30f;
    float sharpSize = 0.9f;
    @Override
    public void Run() {
        blurSize = getTuning("BlurSize", blurSize);
        sharpSize = getTuning("SharpSize", sharpSize);
        float sharpnessLevel = (float) Math.sqrt((CaptureController.mCaptureResult.get(CaptureResult.SENSOR_SENSITIVITY)) * IsoExpoSelector.getMPY() - 50.) / 14.2f;
        sharpnessLevel = Math.max(0.5f, sharpnessLevel);
        sharpnessLevel = Math.min(1.5f, sharpnessLevel);
        glProg.setVar("size", blurSize);
        glProg.setVar("strength", PreferenceKeys.getSharpnessValue());
        glProg.useProgram(R.raw.blur);
        glProg.setTexture("InputBuffer",previousNode.WorkingTexture);
        glProg.drawBlocks(basePipeline.main3);
        glProg.useProgram(R.raw.sharpen33d);
        Log.d("PostNode:" + Name, "sharpnessLevel:" + sharpnessLevel + " iso:" + CaptureController.mCaptureResult.get(CaptureResult.SENSOR_SENSITIVITY));
        glProg.setVar("size", sharpSize);
        glProg.setVar("strength", PreferenceKeys.getSharpnessValue());
        glProg.setTexture("InputBuffer", previousNode.WorkingTexture);
        glProg.setTexture("BlurBuffer",basePipeline.main3);
        WorkingTexture = basePipeline.getMain();
        glProg.drawBlocks(WorkingTexture);
        glProg.closed = true;
    }
}
