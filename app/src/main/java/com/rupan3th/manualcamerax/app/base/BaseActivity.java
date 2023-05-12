package com.rupan3th.manualcamerax.app.base;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.rupan3th.manualcamerax.settings.PreferenceKeys;

import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        PreferenceKeys.setActivityTheme(BaseActivity.this);
        super.onCreate(savedInstanceState);
    }
    public interface BackPressedListener{
        boolean onBackPressed();
    }
}
