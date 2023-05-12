package com.rupan3th.manualcamerax.processing.opengl.scripts;

import android.graphics.Point;

import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLOneScript;
import com.rupan3th.manualcamerax.processing.opengl.GLProg;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;


public class HotPixelRemoval extends GLOneScript {
    public HotPixelRemoval(Point size, int rid, String name) {
        super(size, new GLCoreBlockProcessing(size,new GLFormat(GLFormat.DataType.UNSIGNED_16)), rid, name);
    }
    @Override
    public void StartScript() {
        ScriptParams scriptParams = (ScriptParams)additionalParams;
        GLProg glProg = glOne.glProgram;
        GLTexture input1 = new GLTexture(size,new GLFormat(GLFormat.DataType.UNSIGNED_16),scriptParams.input);
        glProg.setTexture("InputBuffer",input1);
        glProg.setVar("CfaPattern",ManualCameraX.getParameters().cfaPattern);
        WorkingTexture = new GLTexture(input1);
    }
}