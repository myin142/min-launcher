package myin.phone.settings;

import myin.phone.data.app.HomeApp;

public interface ManageAppsChangeListener {
    void onItemAdded(HomeApp app);
    void onItemDeleted(HomeApp app);
    void syncApps();
}
