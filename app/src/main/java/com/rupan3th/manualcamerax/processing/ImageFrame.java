package com.rupan3th.manualcamerax.processing;

import android.media.Image;

import com.rupan3th.manualcamerax.control.GyroBurst;
import com.rupan3th.manualcamerax.processing.parameters.IsoExpoSelector;

import java.nio.ByteBuffer;

public class ImageFrame {
    public ByteBuffer buffer;
    public Image image;
    public GyroBurst frameGyro;
    public float[][][] BlurKernels;
    public double posx,posy;
    public double rX,rY,rZ;
    public double[] HomographyMatrix;
    public double rotation;
    public int number;
    public IsoExpoSelector.ExpoPair pair;

    public ImageFrame(ByteBuffer in) {
        buffer = in;
    }
}
