package com.rupan3th.manualcamerax.control;

import androidx.annotation.NonNull;

public class GyroBurst {
    public float shakiness;
    public float[][] movementss;
    public float[] timestampss;
    public float[] integrated;
    private final int maxSamples;
    public GyroBurst(int maxSamples){
        this.maxSamples = maxSamples;
        movementss = new float[3][maxSamples];
        timestampss = new float[maxSamples];
        integrated = new float[3];
    }

    @NonNull
    @Override
    public GyroBurst clone() {
        GyroBurst out = new GyroBurst(maxSamples);
        out.movementss = movementss.clone();
        out.timestampss = timestampss.clone();
        out.integrated = integrated.clone();
        out.shakiness = shakiness;
        return out;
    }
}

