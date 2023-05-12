package com.rupan3th.manualcamerax.ui.camera.views.manualmode.knobview;


public interface KnobViewChangedListener {
    void onRotationStateChanged(KnobView knobView, KnobView.RotationState rotationState);

    void onSelectedKnobItemChanged(KnobView knobView, KnobItemInfo knobItemInfo, KnobItemInfo knobItemInfo2);
}
