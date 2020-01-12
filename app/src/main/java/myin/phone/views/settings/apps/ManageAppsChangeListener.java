package myin.phone.views.settings.apps;

import myin.phone.data.app.HomeApp;

public interface ManageAppsChangeListener {
    void onItemAdded(HomeApp app);
    void onItemDeleted(HomeApp app);
    void syncApps();
}
