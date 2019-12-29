package myin.phone.home;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import myin.phone.R;
import myin.phone.list.ListItemAdapter;
import myin.phone.shared.LoadAppsActivity;

public class HomeActivity extends LoadAppsActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        RecyclerView appsView = findViewById(R.id.apps_list);
        appsView.setHasFixedSize(true);
        appsView.setLayoutManager(new LinearLayoutManager(this));
        appsView.setAdapter(new ListItemAdapter<>(appList));
    }

}
