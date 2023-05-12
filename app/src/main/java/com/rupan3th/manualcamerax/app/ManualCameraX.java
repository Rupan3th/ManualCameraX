package com.rupan3th.manualcamerax.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.RenderScript;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.os.HandlerCompat;

import com.rupan3th.manualcamerax.api.Settings;
import com.rupan3th.manualcamerax.capture.CaptureController;
import com.rupan3th.manualcamerax.control.Gravity;
import com.rupan3th.manualcamerax.control.Gyro;
import com.rupan3th.manualcamerax.control.Vibration;
import com.rupan3th.manualcamerax.pro.Specific;
import com.rupan3th.manualcamerax.pro.SupportedDevice;
import com.rupan3th.manualcamerax.processing.render.Parameters;
import com.rupan3th.manualcamerax.settings.MigrationManager;
import com.rupan3th.manualcamerax.settings.PreferenceKeys;
import com.rupan3th.manualcamerax.settings.SettingsManager;
import com.rupan3th.manualcamerax.ui.camera.CameraActivity;
import com.rupan3th.manualcamerax.util.AssetLoader;
import com.rupan3th.manualcamerax.util.log.ActivityLifecycleMonitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManualCameraX extends Application {
    public static final boolean DEBUG = false;
    private static ManualCameraX sPhotonCamera;
    //    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//    private final ExecutorService executorService = Executors.newWorkStealingPool();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setPriority(Thread.MIN_PRIORITY);
        return t;
    });
    private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private Settings mSettings;
    private Gravity mGravity;
    private Gyro mGyro;
    private Vibration mVibration;
    private Parameters mParameters;
    private CaptureController mCaptureController;
    private SupportedDevice mSupportedDevice;
    private SettingsManager mSettingsManager;
    private AssetLoader mAssetLoader;
    private RenderScript mRS;

    @Nullable
    public static ManualCameraX getInstance(Context context) {
        if (context instanceof Activity) {
            Application application = ((Activity) context).getApplication();
            if (application instanceof ManualCameraX) {
                return (ManualCameraX) application;
            }
        }
        return null;
    }

    public static Handler getMainHandler() {
        return sPhotonCamera.mainThreadHandler;
    }

    public static Settings getSettings() {
        return sPhotonCamera.mSettings;
    }

    public static Gravity getGravity() {
        return sPhotonCamera.mGravity;
    }

    public static Gyro getGyro() {
        return sPhotonCamera.mGyro;
    }

    public static Vibration getVibration() {
        return sPhotonCamera.mVibration;
    }

    public static Parameters getParameters() {
        return sPhotonCamera.mParameters;
    }

    public static Specific getSpecific(){
        return sPhotonCamera.mSupportedDevice.specific;
    }

    public static RenderScript getRenderScript() {
        return sPhotonCamera.mRS;
    }

    public static CaptureController getCaptureController() {
        return sPhotonCamera.mCaptureController;
    }

    public static void setCaptureController(CaptureController captureController) {
        sPhotonCamera.mCaptureController = captureController;
    }

    public static AssetLoader getAssetLoader() {
        return sPhotonCamera.mAssetLoader;
    }

    public static void restartWithDelay(Context context, long delayMs) {
        getMainHandler().postDelayed(() -> restartApp(context), delayMs);
    }

    public static void restartApp(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
        System.exit(0);
    }

    public static void showToast(String msg) {
        getMainHandler().post(() -> Toast.makeText(sPhotonCamera, msg, Toast.LENGTH_LONG).show());
    }

    public static void showToast(@StringRes int stringRes) {
        getMainHandler().post(() -> Toast.makeText(sPhotonCamera, stringRes, Toast.LENGTH_LONG).show());
    }

    public static void showToastFast(@StringRes int stringRes) {
        getMainHandler().post(() -> Toast.makeText(sPhotonCamera, stringRes, Toast.LENGTH_SHORT).show());
    }

    public static Resources getResourcesStatic() {
        return sPhotonCamera.getResources();
    }

    public static String getStringStatic(@StringRes int stringRes) {
        return sPhotonCamera.getResources().getString(stringRes);
    }

    public static Drawable getDrawableStatic(int resID) {
        return ContextCompat.getDrawable(sPhotonCamera, resID);
    }

    public static PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException {
        return sPhotonCamera.getPackageManager().getPackageInfo(sPhotonCamera.getPackageName(), 0);
    }

    public static String getVersion() {
        String version = "";
        try {
            PackageInfo pInfo = ManualCameraX.getPackageInfo();
            version = pInfo.versionName + '(' + pInfo.versionCode + ')';
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public SupportedDevice getSupportedDevice() {
        return mSupportedDevice;
    }

    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleMonitor());
        sPhotonCamera = this;
        initModules();
    }

    private void initModules() {

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGravity = new Gravity(sensorManager);

        mGyro = new Gyro(sensorManager);

        mVibration = new Vibration(this);

        mSettingsManager = new SettingsManager(this);

        MigrationManager.migrate(mSettingsManager);

        PreferenceKeys.initialise(mSettingsManager);

        mSettings = new Settings();

        mParameters = new Parameters();
        mSupportedDevice = new SupportedDevice(mSettingsManager);
        mAssetLoader = new AssetLoader(this);
        mRS = RenderScript.create(this);
    }
    //  a MemoryInfo object for the device's current memory status.
    /*public ActivityManager.MemoryInfo AvailableMemory() {
        ActivityManager activityManager = (ActivityManager) mCameraActivity.SystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }*/

    @Override
    public void onTerminate() {
        super.onTerminate();
        executorService.shutdownNow();
        mCaptureController = null;
        sPhotonCamera = null;
    }
}
