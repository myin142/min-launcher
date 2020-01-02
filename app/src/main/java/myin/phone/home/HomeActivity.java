package myin.phone.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import myin.phone.R;
import myin.phone.data.app.HomeApp;
import myin.phone.data.app.HomeAppRepository;
import myin.phone.list.ListItemAdapter;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity {

    public static final int REQ_APPS_CHANGED = 1;

    private ListItemAdapter<HomeApp> appAdapter;

    @Inject
    HomeAppRepository homeAppRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        appAdapter = new ListItemAdapter<>();
        appAdapter.onItemClickListener((homeApp, p) -> {
            Intent appIntent = homeApp.getActivityIntent(getPackageManager());
            startActivity(appIntent);
        });

        homeAppRepository.getHomeApps().observe(this, appList -> {
            appAdapter.submitList(appList);
        });

        RecyclerView appsView = findViewById(R.id.apps_list);
        appsView.setHasFixedSize(true);
        appsView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        appsView.setAdapter(appAdapter);
    }

}
