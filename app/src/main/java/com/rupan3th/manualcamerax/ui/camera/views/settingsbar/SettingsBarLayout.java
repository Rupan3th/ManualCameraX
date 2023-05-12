package com.rupan3th.manualcamerax.ui.camera.views.settingsbar;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rupan3th.manualcamerax.R;
import com.rupan3th.manualcamerax.app.ManualCameraX;
import com.rupan3th.manualcamerax.control.Vibration;
import com.rupan3th.manualcamerax.ui.camera.model.SettingsBarButtonModel;
import com.rupan3th.manualcamerax.ui.camera.model.SettingsBarEntryModel;
import com.rupan3th.manualcamerax.ui.settings.SettingsActivity;

public class SettingsBarLayout extends RelativeLayout implements SettingsBarListener {
    private final LinearLayout optionsContainer;
    private final Vibration vibration;

    public SettingsBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        vibration = ManualCameraX.getVibration();
        setBackgroundResource(R.drawable.exif_background);

        ScrollView scrollView = new ScrollView(context);
        scrollView.setId(R.id.settings_bar_scroll_view);
        scrollView.setPadding(dp(10), dp(10), dp(10), dp(5));

        optionsContainer = new LinearLayout(context);
        optionsContainer.setOrientation(LinearLayout.VERTICAL);
        LayoutParams optionsContainerParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        scrollView.addView(optionsContainer, optionsContainerParam);

        LinearLayout settingsButtonContainer = new LinearLayout(context);
        settingsButtonContainer.setId(R.id.settings_bar_settings_button_container);
        settingsButtonContainer.setOrientation(LinearLayout.HORIZONTAL);
        settingsButtonContainer.setGravity(Gravity.END);
        LayoutParams settingsButtonContainerParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(40));
        settingsButtonContainerParam.setMargins(dp(5), dp(0), dp(5), dp(5));
        settingsButtonContainerParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ImageButton settingsButton = new ImageButton(context);
        settingsButton.setImageResource(R.drawable.ic_settings);
        settingsButton.setBackgroundResource(getResolvedAttr(context, android.R.attr.selectableItemBackgroundBorderless));
        settingsButton.setPadding(dp(10), dp(5), dp(10), dp(5));
        settingsButton.setOnClickListener(v -> context.startActivity(new Intent(context, SettingsActivity.class)));
        LayoutParams buttonParam = new LayoutParams(dp(35), dp(35));
        buttonParam.setMargins(dp(10), dp(2.5f), dp(20), dp(2.5f));
        settingsButtonContainer.addView(settingsButton, buttonParam);

        LayoutParams scrollViewParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollViewParam.addRule(ABOVE, R.id.settings_bar_settings_button_container);

        addView(scrollView, scrollViewParam);
        addView(settingsButtonContainer, settingsButtonContainerParam);
    }

    public void addEntry(SettingsBarEntryModel entryModel) {
        entryModel.setSettingsBarListener(this);
        SettingsBarEntryView entryView = new SettingsBarEntryView(getContext());
        entryView.setId(entryModel.getId());
        entryView.setSettingsBarEntryModel(entryModel);
        optionsContainer.addView(entryView);
    }

    private int dp(float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, getContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onEntryUpdated(SettingsBarEntryModel entryModel, SettingsBarButtonModel buttonModel) {
        vibration.Click();
        for (SettingsBarButtonModel model : entryModel.getSettingsBarButtonModels()) {
            findViewById(entryModel.getId()).findViewById(model.getId()).setSelected(model.isSelected());
        }
        ((TextView) findViewById(entryModel.getId()).findViewById(android.R.id.summary)).setText(entryModel.getStateTextStringId());
    }

    public void removeEntries() {
        if (optionsContainer != null) {
            optionsContainer.removeAllViews();
        }
    }

    private int getResolvedAttr(Context context, int attrId) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, outValue, true);
        return outValue.resourceId;
    }

    public void setChildVisibility(@IdRes int id, int visibility) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }
}
