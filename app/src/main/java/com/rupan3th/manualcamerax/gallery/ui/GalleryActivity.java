package com.rupan3th.manualcamerax.gallery.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.base.BaseActivity;
import com.rupan3th.manualcamerax.databinding.ActivityGalleryBinding;

public class GalleryActivity extends BaseActivity {
    private ActivityGalleryBinding activityGalleryBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGalleryBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
//        DataBindingUtil.setContentView(this, R.layout.activity_gallery);
    }

    /*public void onBackArrowClicked(View view) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.gallery_navigation_host);
        NavController navController = navHostFragment.getNavController();
        navController.navigateUp();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityGalleryBinding = null;
    }
}
