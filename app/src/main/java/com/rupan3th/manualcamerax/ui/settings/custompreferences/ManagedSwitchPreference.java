package com.rupan3th.manualcamerax.ui.settings.custompreferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.preference.SwitchPreferenceCompat;

import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.settings.SettingsManager;

/**
 * This class allows Settings UIs to display and set boolean values controlled
 * via the {@link SettingsManager}. The Default {@link SwitchPreferenceCompat} uses
 * {@link android.content.SharedPreferences} as a backing store; since the
 * {@link SettingsManager} stores all settings as Strings we need to ensure we
 * get and set boolean settings through the manager.
 */
public class ManagedSwitchPreference extends SwitchPreferenceCompat {
    private boolean fallback_value;

    public ManagedSwitchPreference(Context context) {
        super(context);
    }

    public ManagedSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ManagedSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean getPersistedBoolean(boolean defaultReturnValue) {
        ManualCameraX photonCamera = ManualCameraX.getInstance(getContext());
        if (photonCamera == null)
            return defaultReturnValue;
        else {
            SettingsManager settingsManager = photonCamera.getSettingsManager();
            if (settingsManager != null)
                return settingsManager.getBoolean(SettingsManager.SCOPE_GLOBAL, getKey(), defaultReturnValue);
            else
                return defaultReturnValue;
        }
    }

    @Override
    public boolean persistBoolean(boolean value) {
        ManualCameraX photonCamera = ManualCameraX.getInstance(getContext());
        if (photonCamera == null)
            return false;
        else {
            SettingsManager settingsManager = photonCamera.getSettingsManager();
            if (settingsManager != null) {
                settingsManager.set(SettingsManager.SCOPE_GLOBAL, getKey(), value);
                return true;
            } else
                return false;
        }
    }

    private void set(boolean value) {
        setChecked(value);
        persistBoolean(value);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        if (defaultValue == null) {
            defaultValue = fallback_value;
        }
        set(getPersistedBoolean((Boolean) defaultValue));
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        fallback_value = a.getBoolean(index, false);
        return a.getBoolean(index, false);
    }
}