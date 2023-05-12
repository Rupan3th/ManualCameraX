package com.rupan3th.manualcamerax.api;

import androidx.annotation.StringRes;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.ManualCameraX;

import java.util.stream.Stream;

public enum CameraMode {
    UNLIMITED(R.string.mode_unlimited),
    PHOTO(R.string.mode_photo),
    NIGHT(R.string.mode_night),
    VIDEO(R.string.mode_video);

    int stringId;

    CameraMode(@StringRes int stringId) {
        this.stringId = stringId;
    }

    public static CameraMode valueOf(int modeOrdinal) {
        for (CameraMode mode : values()) {
            if (modeOrdinal == mode.ordinal()) {
                return mode;
            }
        }
        return PHOTO;
    }

    public static Integer[] nameIds() {
        return Stream.of(values()).map(mode -> mode.stringId).toArray(Integer[]::new);
    }

}
