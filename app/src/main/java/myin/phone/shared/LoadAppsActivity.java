package myin.phone.shared;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import myin.phone.SharedConst;
import myin.phone.apps.AppItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class LoadAppsActivity extends AppCompatActivity {

    private static final Logger log = Logger.getLogger("LoadAppsActivity");
    protected List<AppItem> appList = new ArrayList<>();
    protected SharedPreferences preferences;
    protected boolean dirty = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getApplicationContext().getSharedPreferences(SharedConst.PREFERENCE_NAME, 0);
        reloadApps();
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        if (appsHasChanged()) {
            reloadApps();
        }
*/
    }

    protected boolean appsHasChanged() {
        return dirty;
    }

    private void reloadApps() {
        Set<String> appPackages = preferences.getStringSet(SharedConst.PREF_APPS, Collections.emptySet());

        if (appPackages == null) {
            return;
        }

        appList.clear();
        PackageManager pm = getPackageManager();
        for (String appPackage : appPackages) {
            appList.add(new AppItem(pm, appPackage));
        }

        markAppsPristine();
        log.info("Loading Apps: " + appList);
    }

    protected void markAppsPristine() {
        dirty = false;
    }

    protected void markAppsDirty() {
        dirty = true;
    }

}
