package com.rupan3th.manualcamerax.processing.opengl.nodes;

import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLInterface;
import com.rupan3th.manualcamerax.processing.opengl.GLProg;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.opengl.postpipeline.PostPipeline;
import com.rupan3th.manualcamerax.processing.render.Parameters;

public class Debug2 extends Node {

    public Debug2(int rid, String name) {
        super(rid, name);
    }

    @Override
    public void Run() {
        PostPipeline rawPipeline = (PostPipeline) basePipeline;
        GLInterface glint = rawPipeline.glint;
        GLProg glProg = glint.glProgram;
        Parameters params = glint.parameters;
        GLTexture glTexture = new GLTexture(params.rawSize, new GLFormat(GLFormat.DataType.UNSIGNED_16), rawPipeline.stackFrame);
        glProg.setTexture("InputBuffer", glTexture);
    }
}
