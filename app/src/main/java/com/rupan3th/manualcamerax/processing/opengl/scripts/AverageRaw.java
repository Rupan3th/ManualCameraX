package com.rupan3th.manualcamerax.processing.opengl.scripts;

import android.graphics.Point;
import android.util.Log;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.processing.processor.UnlimitedProcessor;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLOneScript;
import com.rupan3th.manualcamerax.processing.opengl.GLProg;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;

import static com.rupan3th.manualcamerax.processing.processor.UnlimitedProcessor.unlimitedCounter;

public class AverageRaw extends GLOneScript {
    GLTexture in1,in2, first,second,stack,finalTex;
    private GLProg glProg;
    int used = 1;
    public AverageRaw(Point size, String name) {
        super(size, new GLCoreBlockProcessing(size,new GLFormat(GLFormat.DataType.UNSIGNED_16)), R.raw.average, name);
    }
    float[] wpoints;
    public void Init(){
        first = new GLTexture(size,new GLFormat(GLFormat.DataType.FLOAT_16));
        second = new GLTexture(size,new GLFormat(GLFormat.DataType.FLOAT_16));
        stack = new GLTexture(size,new GLFormat(GLFormat.DataType.FLOAT_16));
        finalTex = new GLTexture(size,new GLFormat(GLFormat.DataType.UNSIGNED_16));
        float []oldp = ManualCameraX.getParameters().whitePoint;
        wpoints = new float[oldp.length];
        float min = 1000.f;
        for(float p : oldp){
            if(p<min){
                min = p;
            }
        }
        for(int i =0;i<oldp.length;i++){
            wpoints[i] = oldp[i];
        }
    }
    GLTexture GetAlterIn(){
        if(used == 1) {
            return first;
        } else {
            return second;
        }
    }
    GLTexture GetAlterOut(){
        if(used == 1){
            used = 2;
            return second;
        } else {
            used = 1;
            return first;
        }
    }
    private int cnt2 = 1;
    @Override
    public void Run() {

        //Stage 1 average alternate texture
            Compile();
            AverageParams scriptParams = (AverageParams) additionalParams;
            glProg = glOne.glProgram;
            in1 = GetAlterIn();
            in2 = new GLTexture(size, new GLFormat(GLFormat.DataType.UNSIGNED_16), scriptParams.inp2);
            if (in1 == null) {
                glProg.setVar("first", 1);
                if(in1 == null) Init();
            } else {
                glProg.setVar("first", 0);
                glProg.setTexture("InputBuffer", in1);
            }
            glProg.setTexture("InputBuffer2", in2);
            glProg.setVar("CfaPattern",ManualCameraX.getParameters().cfaPattern);
            glProg.setVar("blacklevel", ManualCameraX.getParameters().blackLevel);
            glProg.setVar("WhitePoint", wpoints);
            glProg.setVar("whitelevel", (int) (ManualCameraX.getParameters().whiteLevel));
            if(unlimitedCounter < 3) {
                glProg.setVar("unlimitedcount", 1);
            } else {
                glProg.setVar("unlimitedcount", unlimitedCounter);
            }

            //WorkingTexture.BufferLoad();
            glProg.drawBlocks(GetAlterOut());
            //glOne.glProcessing.drawBlocksToOutput();
            AfterRun();
        //Stage 2 average stack
        if(unlimitedCounter > 80) {
            AverageStack();
        }
    }
    private void AverageStack(){
        glProg.useProgram(R.raw.averageff);
        glProg.setTexture("InputBuffer",GetAlterIn());
        glProg.setTexture("InputBuffer2", stack);
        glProg.setVar("unlimitedcount",Math.min(cnt2,250));
        glProg.drawBlocks(GetAlterOut());
        Log.d(Name,"AverageShift:"+Math.min(cnt2,250));
        GLTexture t = stack;
        if(used == 2){
            stack = second;
            second = t;
        } else {
            stack = first;
            first = t;
        }
        cnt2++;
        unlimitedCounter = 1;
    }
    public void FinalScript(){
        AverageStack();
        glProg = glOne.glProgram;
        glProg.useProgram(R.raw.medianfilterhotpixeltoraw);
        glProg.setVar("CfaPattern",ManualCameraX.getParameters().cfaPattern);
        Log.d(Name,"CFAPattern:"+ManualCameraX.getParameters().cfaPattern);
        glProg.setTexture("InputBuffer",stack);
        glProg.setVar("whitelevel",(int) UnlimitedProcessor.FAKE_WL);
        first.close();
        second.close();
        //in1 = WorkingTexture;
        finalTex.BufferLoad();
        glOne.glProcessing.drawBlocksToOutput();
        stack.close();
        finalTex.close();
        glProg.close();
        Output = glOne.glProcessing.mOutBuffer;
    }

    @Override
    public void AfterRun() {
        in2.close();
    }
}