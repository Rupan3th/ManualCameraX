package com.rupan3th.manualcamerax.processing.opengl.scripts;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLOneParams;
import com.rupan3th.manualcamerax.processing.opengl.GLOneScript;
import com.rupan3th.manualcamerax.processing.opengl.GLProg;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;

import java.nio.ByteBuffer;

public class BoxDown extends GLOneScript {
    public ByteBuffer inputB;
    public Point sizeIn;
    public BoxDown(GLCoreBlockProcessing glCoreBlockProcessing) {
        super(new Point(1,1), glCoreBlockProcessing, R.raw.boxdown221, "BoxDown");
    }

    @Override
    public void StartScript() {
        GLProg glProg = glOne.glProgram;
        GLTexture input = new GLTexture(sizeIn,new GLFormat(GLFormat.DataType.UNSIGNED_16),inputB);
        glProg.setTexture("InputBuffer", input);
        WorkingTexture = new GLTexture(new Point(sizeIn.x / 2, sizeIn.y / 2), new GLFormat(GLFormat.DataType.FLOAT_16));
    }

    @Override
    public void Run() {
        Compile();
        startT();
        StartScript();
        WorkingTexture.BufferLoad();
        glOne.glProcessing.drawBlocksToOutput(WorkingTexture.mSize,WorkingTexture.mFormat,Output);
        AfterRun();
        endT();
        WorkingTexture.close();
    }
}
