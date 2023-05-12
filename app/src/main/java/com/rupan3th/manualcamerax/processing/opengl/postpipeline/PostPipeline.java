package com.rupan3th.manualcamerax.processing.opengl.postpipeline;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.processing.ImageFrame;
import com.rupan3th.manualcamerax.processing.opengl.GLBasePipeline;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLDrawParams;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLInterface;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.parameters.IsoExpoSelector;
import com.rupan3th.manualcamerax.processing.parameters.ResolutionSolution;
import com.rupan3th.manualcamerax.processing.render.Parameters;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PostPipeline extends GLBasePipeline {
    public ByteBuffer stackFrame;
    public ByteBuffer lowFrame;
    public ByteBuffer highFrame;
    public GLTexture FusionMap;
    public GLTexture GainMap;
    public ArrayList<Bitmap> debugData = new ArrayList<>();
    public ArrayList<ImageFrame> SAGAIN;
    public float[] analyzedBL = new float[]{0.f,0.f,0.f};;
    float regenerationSense = 1.f;
    float AecCorr = 1.f;
    public int getRotation() {
        int rotation = mParameters.cameraRotation;
        String TAG = "ParseExif";
        Log.d(TAG, "Gravity rotation:" + ManualCameraX.getGravity().getRotation());
        Log.d(TAG, "Sensor rotation:" + ManualCameraX.getCaptureController().mSensorOrientation);
        return rotation;
    }
    @SuppressWarnings("SuspiciousNameCombination")
    private Point getRotatedCoords(Point in){
        switch (getRotation()){
            case 0:
            case 180:
                return in;
            case 90:
            case 270:
                return new Point(in.y,in.x);
        }
        return in;
    }
    public Bitmap Run(ByteBuffer inBuffer, Parameters parameters){
        mParameters = parameters;
        mSettings = ManualCameraX.getSettings();
        Point rotated = getRotatedCoords(parameters.rawSize);
        if(ManualCameraX.getSettings().energySaving || mParameters.rawSize.x*mParameters.rawSize.y < ResolutionSolution.smallRes){
            GLDrawParams.TileSize = 8;
        } else {
            GLDrawParams.TileSize = 256;
        }
        /*if (PhotonCamera.getSettings().selectedMode == CameraMode.NIGHT) {
            rotated.x/=2;
            rotated.y/=2;


        }*/
        Bitmap output = Bitmap.createBitmap(rotated.x,rotated.y, Bitmap.Config.ARGB_8888);

        GLCoreBlockProcessing glproc = new GLCoreBlockProcessing(rotated,output, new GLFormat(GLFormat.DataType.UNSIGNED_8,4));
        glint = new GLInterface(glproc);
        stackFrame = inBuffer;
        glint.parameters = parameters;
        add(new Bayer2Float());

        //add(new ExposureFusionBayer2());
        //if(!IsoExpoSelez = mix(mix(z, z*z, BR),z,z);ctor.HDR) {
            if (ManualCameraX.getSettings().cfaPattern != 4) {
                //if (PhotonCamera.getSettings().selectedMode != CameraMode.NIGHT) {
                    add(new Demosaic2());
                //} else {
                //    add(new BinnedDemosaic());
                //}
            } else {
                add(new MonoDemosaic());
            }
        //} else {
        //    add(new LFHDR());
        //}
        /*
         * * * All filters after demosaicing * * *
         */
        if(ManualCameraX.getSettings().hdrxNR) {
            add(new SmartNR());
        }

        //add(new DynamicBL());
        //add(new GlobalToneMapping(0,"GlobalTonemap"));


        add(new Initial());

        add(new Equalization());


        //add(new AWB());

        


        //add(new Median(new Point(1,1),4,"PostMedian",R.raw.medianfilter));
        add(new SharpenDual());
        //add(new Sharpen(R.raw.sharpeningbilateral));
        add(new RotateWatermark(getRotation()));
        return runAll();
    }
}
