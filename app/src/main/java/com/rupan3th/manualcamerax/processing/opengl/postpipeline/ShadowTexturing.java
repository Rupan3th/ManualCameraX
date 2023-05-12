package com.rupan3th.manualcamerax.processing.opengl.postpipeline;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLInterface;
import com.rupan3th.manualcamerax.processing.opengl.GLProg;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.opengl.nodes.Node;

import java.nio.ByteBuffer;

public class ShadowTexturing extends Node {
    public ShadowTexturing(int rid, String name) {
        super(rid, name);
    }

    @Override
    public void Run() {
        BitmapDrawable dr = (BitmapDrawable) ManualCameraX.getDrawableStatic(R.drawable.shadowtex);
        ByteBuffer buff = ByteBuffer.allocate(dr.getBitmap().getByteCount());
        dr.getBitmap().copyPixelsToBuffer(buff);
        GLInterface glint = basePipeline.glint;
        Node Previous = super.previousNode;
        GLProg glProg = glint.glProgram;
        glProg.setTexture("InputBuffer", Previous.WorkingTexture);
        Log.v("ShadowTexturing", "Buffer size:" + buff.capacity());
        buff.position(0);
        glProg.setTexture("InputTex", new GLTexture(255, 255, new GLFormat(GLFormat.DataType.UNSIGNED_16, 2), buff));
        WorkingTexture = new GLTexture(Previous.WorkingTexture.mSize, Previous.WorkingTexture.mFormat, null);
    }
}
