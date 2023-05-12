package com.rupan3th.manualcamerax.processing.opengl.postpipeline;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.processing.opengl.GLDrawParams;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.opengl.nodes.Node;

public class MonoDemosaic extends Node {
    public MonoDemosaic() {
        super(R.raw.monochrome, "Monochrome");
    }

    @Override
    public void Run() {
        PostPipeline postPipeline = (PostPipeline) (basePipeline);
        GLTexture glTexture;
        //glTexture = new GLTexture(params.rawSize, new GLFormat(GLFormat.DataType.UNSIGNED_16), postPipeline.stackFrame);
        glTexture = previousNode.WorkingTexture;
        glProg.setTexture("RawBuffer", glTexture);
        for(int i =0; i<4;i++){
            basePipeline.mParameters.blackLevel[i]/=basePipeline.mParameters.whiteLevel;
        }
        glProg.setVar("blackLevel",basePipeline.mParameters.blackLevel);
        WorkingTexture = new GLTexture(basePipeline.mParameters.rawSize, new GLFormat(GLFormat.DataType.FLOAT_16, GLDrawParams.WorkDim));
        glProg.drawBlocks(WorkingTexture);
        glProg.closed = true;
    }
}
