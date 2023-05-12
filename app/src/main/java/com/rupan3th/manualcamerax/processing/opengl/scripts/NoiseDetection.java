package com.rupan3th.manualcamerax.processing.opengl.scripts;

import android.graphics.Point;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLOneScript;
import com.rupan3th.manualcamerax.processing.opengl.GLProg;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;

public class NoiseDetection extends GLOneScript {
    public NoiseDetection(Point size) {
        super(size, null, null, R.raw.noisedetection44, "NoiseDetection444");
    }

    public NoiseDetection(Point size, GLCoreBlockProcessing glCoreBlockProcessing) {
        super(size, glCoreBlockProcessing, R.raw.noisedetection44, "NoiseDetection444");
    }

    @Override
    public void StartScript() {
        ScriptParams scriptParams = (ScriptParams) additionalParams;
        GLProg glProg = glOne.glProgram;
        glProg.setTexture("InputBuffer", scriptParams.textureInput);
        super.WorkingTexture = new GLTexture(scriptParams.textureInput.mSize, scriptParams.textureInput.mFormat, null);
    }
}
