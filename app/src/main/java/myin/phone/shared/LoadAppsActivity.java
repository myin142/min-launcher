package myin.phone.shared;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.annimon.stream.Optional;
import myin.phone.SharedConst;
import myin.phone.apps.AppItem;

import java.util.ArrayList;
import java.util.List;

public class LoadAppsActivity extends AppCompatActivity {

    protected List<AppItem> appList = new ArrayList<>();
    protected SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getApplicationContext().getSharedPreferences(SharedConst.PREFERENCE_NAME, 0);
        reloadApps();
    }

    protected void reloadApps() {
        String[] appPackages = Optional.ofNullable(preferences.getString(SharedConst.PREF_APPS, null))
                .map(x -> x.split(SharedConst.PREF_APPS_DELIM))
                .orElse(new String[0]);

        appList.clear();
        PackageManager pm = getPackageManager();
        for (String appPackage : appPackages) {
            appList.add(new AppItem(pm, appPackage));
        }
    }

}
