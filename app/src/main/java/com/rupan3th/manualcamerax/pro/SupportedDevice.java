package com.rupan3th.manualcamerax.pro;

import android.os.Build;
import android.util.Log;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.settings.PreferenceKeys;
import com.rupan3th.manualcamerax.settings.SettingsManager;
import com.rupan3th.manualcamerax.util.HttpLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.rupan3th.manualcamerax.settings.PreferenceKeys.Key.ALL_DEVICES_NAMES_KEY;

public class SupportedDevice {
    public static final String THIS_DEVICE = Build.BRAND.toLowerCase() + ":" + Build.DEVICE.toLowerCase();
    private static final String TAG = "SupportedDevice";
    private final SettingsManager mSettingsManager;
    private Set<String> mSupportedDevicesSet = new LinkedHashSet<>();
    public Specific specific;
    private boolean loaded = false;
    private int checkedCount = 0;

    public SupportedDevice(SettingsManager manager) {
        mSettingsManager = manager;
    }

    public void loadCheck() {
        specific = new Specific(mSettingsManager);
        new Thread(() -> {
            try {
                if (checkedCount < 1) {
                    loadSupportedDevicesList();
                    specific.loadSpecific();
                    isSupported();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!loaded && mSettingsManager.isSet(PreferenceKeys.Key.DEVICES_PREFERENCE_FILE_NAME.mValue, ALL_DEVICES_NAMES_KEY))
                mSupportedDevicesSet = mSettingsManager.getStringSet(PreferenceKeys.Key.DEVICES_PREFERENCE_FILE_NAME.mValue, ALL_DEVICES_NAMES_KEY, null);
        }).start();
    }

    private void isSupported() {
        Log.d(TAG, "isSupported(): checkedCount = " + checkedCount);
        checkedCount++;
        if (mSupportedDevicesSet == null) {
            return;
        }
        if (mSupportedDevicesSet.contains(THIS_DEVICE)) {
            ManualCameraX.showToastFast(R.string.device_support);
        } else {
            ManualCameraX.showToastFast(R.string.device_unsupport);
        }
    }

    public boolean isSupportedDevice() {
        if (mSupportedDevicesSet == null) {
            return false;
        }
        return mSupportedDevicesSet.contains(THIS_DEVICE);
    }

    private void loadSupportedDevicesList() throws IOException {
        BufferedReader in = HttpLoader.readURL("https://raw.githubusercontent.com/eszdman/PhotonCamera/dev/app/SupportedList.txt");
        String str;
        while ((str = in.readLine()) != null) {
            mSupportedDevicesSet.add(str);
            Log.d(TAG, "Loaded:" + str);
        }

        loaded = true;
        in.close();
        mSettingsManager.set(PreferenceKeys.Key.DEVICES_PREFERENCE_FILE_NAME.mValue, ALL_DEVICES_NAMES_KEY, mSupportedDevicesSet);
    }
}
