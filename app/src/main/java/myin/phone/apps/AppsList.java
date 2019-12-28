package myin.phone.apps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import myin.phone.R;
import myin.phone.list.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppsList extends AppCompatActivity {

    public static final String SELECTED_APP = "appList_selectedApp";
    public static final int SELECTED_APP_CODE = 0;
    public static final int SELECT_APP_REQUEST_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);

        RecyclerView recyclerAppsList = findViewById(R.id.apps_list);
        recyclerAppsList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAppsList.setLayoutManager(layoutManager);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        List<AppItem> appItems = new ArrayList<>();
        for (ResolveInfo app : apps) {
            appItems.add(new AppItem(app.loadLabel(pm), app.activityInfo.packageName));
        }

        ListItemAdapter<AppItem> appsListAdapter = new ListItemAdapter<>(appItems);
        appsListAdapter.onItemClickListener(appItem -> {
            Intent data = new Intent();
            data.putExtra(SELECTED_APP, appItem.getFullName());
            System.out.println("Clicked on App: " + appItem.getName());

            setResult(SELECTED_APP_CODE, data);
            finish();
        });

        recyclerAppsList.setAdapter(appsListAdapter);
    }

}
