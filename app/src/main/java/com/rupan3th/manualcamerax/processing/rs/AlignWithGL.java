package com.rupan3th.manualcamerax.processing.rs;

import android.graphics.Point;
import android.renderscript.Allocation;
import android.renderscript.Int2;
import android.renderscript.RenderScript;
import android.util.Log;

import com.particlesdevs.photoncamera.ScriptC_align;
import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.processing.opengl.GLCoreBlockProcessing;
import com.rupan3th.manualcamerax.processing.opengl.GLFormat;
import com.rupan3th.manualcamerax.processing.opengl.GLInterface;
import com.rupan3th.manualcamerax.processing.opengl.GLTexture;
import com.rupan3th.manualcamerax.processing.opengl.rawpipeline.AlignAndMergeHybrid;
import com.rupan3th.manualcamerax.processing.opengl.scripts.BoxDown;
import com.rupan3th.manualcamerax.processing.opengl.scripts.GaussianResize;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import static com.rupan3th.manualcamerax.util.Utilities.addP;
import static com.rupan3th.manualcamerax.util.Utilities.div;

public class AlignWithGL {
    private RUtils rUtils;
    private ScriptC_align align;

    public GLTexture refDown2;
    public GLTexture refDown8;
    public GLTexture refDown32;
    public GLTexture refDown128;


    public GLTexture inputDown2;
    public GLTexture inputDown8;
    public GLTexture inputDown32;
    public GLTexture inputDown128;

    public GlAllocation DiffHVIn;
    public GlAllocation DiffHVRef;

    public GlAllocation CornersRef;
    public GlAllocation CornersIn;

    private GLInterface glInterface;

    public int[] alignments;
    private void PrepareDiffs(GLTexture in, GLTexture ref, int i){
        glInterface.glUtils.ConvDiff(in, DiffHVIn.glTexture,0.f);
        glInterface.glUtils.ConvDiff(ref, DiffHVRef.glTexture,0.f);
        glInterface.glUtils.Corners(DiffHVRef.glTexture,CornersRef.glTexture);
        CornersRef.pushToAllocation();
        glInterface.glUtils.Corners(DiffHVIn.glTexture,CornersIn.glTexture);
        CornersIn.pushToAllocation();
    }

    public GlAllocation align128,align32,align8,align2;
    public static RenderScript rs;

    private static final String TAG = "AlignRS";
    public AlignWithGL(){
        rs = ManualCameraX.getRenderScript();
        rUtils = new RUtils(rs);
        align = new ScriptC_align(rs);
    }
    public void AlignFrame(GLInterface glInterface,int i){
        this.glInterface = glInterface;
        Log.d("AlignRs","RunningAlign128");
        Log.d("AlignRs","inputDown128"+inputDown128.mSize);
        Log.d("AlignRs","align128"+align128.glTexture.mSize);

        align128.createAllocation();
        align32.createAllocation();
        align8.createAllocation();
        align2.createAllocation();


        align.set_TILESIZE(AlignAndMergeHybrid.tileSize);

        PrepareDiffs(inputDown128,refDown128,i);
        align.set_prevScale(0);
        align.set_inputBuffer(CornersIn.allocation);
        align.set_referenceBuffer(CornersRef.allocation);
        align.set_inputSize(new Int2(inputDown128.mSize.x,inputDown128.mSize.y));
        align.set_alignOutput(align128.allocation);
        align.forEach_align(rUtils.Range(align128.allocation));


        Log.d("AlignRs","RunningAlign32");

        PrepareDiffs(inputDown128,refDown128,i);
        align.set_prevScale(0);
        align.set_alignVectors(align128.allocation);
        align.set_inputBuffer(CornersIn.allocation);
        align.set_referenceBuffer(CornersRef.allocation);
        align.set_inputSize(new Int2(inputDown32.mSize.x,inputDown32.mSize.y));
        align.set_prevSize(new Int2(align128.allocation.getType().getX(),align128.allocation.getType().getY()));
        align.set_alignOutput(align32.allocation);
        align.forEach_align(rUtils.Range(align32.allocation));

        Log.d("AlignRs","RunningAlign8");
        PrepareDiffs(inputDown128,refDown128,i);
        align.set_prevScale(4);
        align.set_alignVectors(align32.allocation);
        align.set_inputBuffer(CornersIn.allocation);
        align.set_referenceBuffer(CornersRef.allocation);
        align.set_inputSize(new Int2(inputDown8.mSize.x,inputDown8.mSize.y));
        align.set_prevSize(new Int2(align32.allocation.getType().getX(),align32.allocation.getType().getY()));
        align.set_alignOutput(align8.allocation);
        align.forEach_align(rUtils.Range(align8.allocation));

        Log.d("AlignRs","RunningAlign2");
        PrepareDiffs(inputDown128,refDown128,i);
        align.set_prevScale(4);
        align.set_alignVectors(align8.allocation);
        align.set_inputBuffer(CornersIn.allocation);
        align.set_referenceBuffer(CornersRef.allocation);
        align.set_inputSize(new Int2(inputDown2.mSize.x,inputDown2.mSize.y));
        align.set_prevSize(new Int2(align8.allocation.getType().getX(),align8.allocation.getType().getY()));
        align.set_alignOutput(align2.allocation);
        align.forEach_align(rUtils.Range(align2.allocation));

        ByteBuffer buffer = align2.allocationBuffer();
        buffer.asReadOnlyBuffer().asIntBuffer().get(alignments);
    }
}
