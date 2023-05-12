package com.rupan3th.manualcamerax.ui.camera.model;

import android.graphics.Bitmap;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Class that holds the ui state, for now the orientation
 */
public class CameraFragmentModel extends BaseObservable {
    private int orientation;
    private int duration;
    private Bitmap bitmap;
    private boolean settingsBarVisibility;

    @Bindable
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        notifyChange();
    }

    @Bindable
    public int getOrientation() {
        return orientation;
    }

    /**
     * set the orientation and note the binded views about the change
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
        notifyChange();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    @Bindable
    public boolean isSettingsBarVisibility() {
        return settingsBarVisibility;
    }

    public void setSettingsBarVisibility(boolean settingsBarVisibility) {
        this.settingsBarVisibility = settingsBarVisibility;
        notifyChange();
    }
}
