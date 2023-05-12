package com.rupan3th.manualcamerax.processing.opengl.scripts;

import android.graphics.Point;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLOneScript;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.render.Parameters;

import java.nio.ByteBuffer;

public class BinnedRaw extends GLOneScript {
    public ByteBuffer inp;
    public ByteBuffer prevmap;
    public Parameters parameters;
    public BinnedRaw(Point size) {
        super(size, new GLCoreBlockProcessing(size,new GLFormat(GLFormat.DataType.UNSIGNED_16), GLCoreBlockProcessing.Allocate.None), R.raw.binnedraw, "NonIdealRaw");
    }

    @Override
    public void Run() {
        Compile();
        float maxmap = 0.f;
        for(int i =0; i<parameters.gainMap.length;i++){
            if(maxmap < parameters.gainMap[i]) maxmap = parameters.gainMap[i];
        }
        GLTexture input = new GLTexture(parameters.rawSize,new GLFormat(GLFormat.DataType.FLOAT_32),prevmap);
        GLTexture inpb = new GLTexture(parameters.rawSize,new GLFormat(GLFormat.DataType.UNSIGNED_16),inp);
        glOne.glProgram.setTexture("GainMap",input);
        glOne.glProgram.setTexture("InputBuffer",inpb);
        glOne.glProgram.setVar("MaxMap",maxmap);
        glOne.glProcessing.mOutBuffer = inp;
        inpb.BufferLoad();
        glOne.glProcessing.drawBlocksToOutput();
        inpb.close();
        input.close();
        Output = glOne.glProcessing.mOutBuffer;
    }
}
