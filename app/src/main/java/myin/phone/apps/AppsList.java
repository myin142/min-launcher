package myin.phone.apps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import myin.phone.R;
import myin.phone.list.ListItemAdapter;

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

        List<AppItem> appItems = new ArrayList<>();
        for (ResolveInfo app : apps) {
            appItems.add(new AppItem(pm, app));
        }

        ListItemAdapter<AppItem> appsListAdapter = new ListItemAdapter<>(appItems);
        appsListAdapter.onItemClickListener((appItem, p)-> {
            Intent data = new Intent();
            data.putExtra(SELECTED_PACKAGE_APP, appItem.getPackageName());
            data.putExtra(SELECTED_CLASS_APP, appItem.getClassName());
            data.putExtra(SELECTED_LABEL_APP, appItem.getLabel());

            setResult(RESULT_OK, data);
            finish();
        });

        recyclerAppsList.setAdapter(appsListAdapter);
    }

}
