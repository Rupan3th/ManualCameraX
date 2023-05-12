package com.rupan3th.manualcamerax.ui.camera.views.settingsbar;

import com.rupan3th.manualcamerax.ui.camera.model.SettingsBarButtonModel;
import com.rupan3th.manualcamerax.ui.camera.model.SettingsBarEntryModel;

public interface SettingsBarListener {
    void onEntryUpdated(SettingsBarEntryModel settingsBarEntryModel, SettingsBarButtonModel buttonModel);
}
