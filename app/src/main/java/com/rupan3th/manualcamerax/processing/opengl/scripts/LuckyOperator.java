package com.rupan3th.manualcamerax.processing.opengl.scripts;

import android.graphics.Point;
import android.util.Log;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLOneScript;
import com.rupan3th.manualcamerax.processing.opengl.GLProg;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.opengl.GLUtils;

public class LuckyOperator extends GLOneScript {
    Point insize;
    public long out = 0;
    public LuckyOperator(Point size) {
        super(new Point(size.x/(64),size.y/(64)), null, new GLFormat(GLFormat.DataType.FLOAT_16,4), R.raw.luckyoperator, "LuckyOperator");
        insize = size;
    }

    @Override
    public void StartScript() {
        ScriptParams scriptParams = (ScriptParams)additionalParams;
        GLProg glProg = glOne.glProgram;
        GLTexture input1 = new GLTexture(insize,new GLFormat(GLFormat.DataType.UNSIGNED_16),scriptParams.input);
        glProg.setTexture("InputBuffer",input1);
        glProg.setVar("CfaPattern",ManualCameraX.getParameters().cfaPattern);
        WorkingTexture = new GLTexture(input1.mSize.x/2,input1.mSize.y/2,new GLFormat(GLFormat.DataType.FLOAT_16),null);
        glProg.drawBlocks(input1);
        GLTexture luckyTex = new GLTexture(input1.mSize,WorkingTexture.mFormat,null);
        glProg.drawBlocks(luckyTex);
        GLUtils glUtils = new GLUtils(glOne.glProcessing);
        //WorkingTexture = glUtils.gaussdown( glUtils.gaussdown(luckyTex,8),8);
        WorkingTexture =  glUtils.gaussdown(luckyTex,64);
        glOne.glProgram.drawBlocks(WorkingTexture);
        glOne.glProcessing.drawBlocksToOutput();
        glOne.glProgram.close();
        glOne.glProcessing.close();
        Output = glOne.glProcessing.mOutBuffer;
        for(int i =0; i<Output.remaining();i++){
            out+=((long)Output.get(i)) + 128;
        }
        WorkingTexture.close();
        Log.d("LuckyOperator","Result:"+out);
    }

    @Override
    public void Run() {
        Compile();
        startT();
        StartScript();
        endT();
    }
}
