package com.rupan3th.manualcamerax.processing.opengl.rawpipeline;

import android.media.Image;
import android.util.Log;

import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.processing.ImageFrame;
import com.rupan3th.manualcamerax.processing.opengl.GLBasePipeline;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLInterface;
import com.rupan3th.manualcamerax.processing.render.Parameters;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RawPipeline extends GLBasePipeline {
    public float sensitivity = 1.f;
    public ArrayList<ImageFrame> images;
    public ArrayList<ByteBuffer> alignments;
    public ArrayList<Image> imageObj;
    public int alignAlgorithm;

    public ByteBuffer Run() {
        mParameters = ManualCameraX.getParameters();
        GLCoreBlockProcessing glproc = new GLCoreBlockProcessing(mParameters.rawSize, new GLFormat(GLFormat.DataType.UNSIGNED_16));
        //GLContext glContext = new GLContext(parameters.rawSize.x,parameters.rawSize.y);
        glint = new GLInterface(glproc);
        glint.parameters = mParameters;
        //add(new Debug(R.raw.debugraw,"DebugRaw"));
        if(alignAlgorithm == 1)
        add(new AlignAndMerge());
        if(alignAlgorithm == 2) {
            Log.d("RawPipeline","Entering hybrid alignment");
            add(new AlignAndMergeCV());
        }
        return runAllRaw();
    }
}
