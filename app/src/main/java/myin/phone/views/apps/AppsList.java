package myin.phone.views.apps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import myin.phone.R;
import myin.phone.data.app.HomeApp;
import myin.phone.data.app.HomeAppDiffCallback;
import myin.phone.list.TextListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsList extends AppCompatActivity {

    public static final String SELECTED_PACKAGE_APP = "appList_selectedPackage";
    public static final String SELECTED_CLASS_APP  = "appList_selectedClass";
    public static final String SELECTED_LABEL_APP = "appList_selectedLabel";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);

        RecyclerView recyclerAppsList = findViewById(R.id.apps_list);
        recyclerAppsList.setHasFixedSize(true);
        recyclerAppsList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));

        List<HomeApp> appList = new ArrayList<>();
        for (ResolveInfo app : apps) {
            ActivityInfo info = app.activityInfo;
            HomeApp homeApp = new HomeApp(info.packageName, info.name, app.loadLabel(pm).toString());
            appList.add(homeApp);
        }

        TextListAdapter<HomeApp> appsListAdapter = new TextListAdapter<>(new HomeAppDiffCallback());
        appsListAdapter.setOnItemClickListener(appItem -> {
            Intent data = new Intent();
            data.putExtra(SELECTED_PACKAGE_APP, appItem.packageName);
            data.putExtra(SELECTED_CLASS_APP, appItem.className);
            data.putExtra(SELECTED_LABEL_APP, appItem.label);

            setResult(RESULT_OK, data);
            finish();
        });
        appsListAdapter.submitList(appList);

        recyclerAppsList.setAdapter(appsListAdapter);
    }

}
