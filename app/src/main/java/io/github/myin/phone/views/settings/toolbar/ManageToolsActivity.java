package io.github.myin.phone.views.settings.toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.github.myin.phone.R;
import io.github.myin.phone.data.tool.HomeTool;
import io.github.myin.phone.data.tool.HomeToolRepository;
import io.github.myin.phone.list.OnListChangeListener;
import io.github.myin.phone.list.NoScrollLinearLayout;
import io.github.myin.phone.views.SelectAppActivity;
import io.github.myin.phone.views.apps.AppsList;

import javax.inject.Inject;
import java.util.List;

public class ManageToolsActivity extends SelectAppActivity implements OnListChangeListener<HomeTool> {

    private static final int REQ_NEW_APP = 1;
    private static final int REQ_EDIT_APP = 2;

    private TextView addText;
    private ManageToolsAdapter toolsAdapter;
    private HomeTool editTool;

    @Inject
    HomeToolRepository homeToolRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_tools_edit);

        addText = findViewById(R.id.add_tool);
        addText.setOnClickListener(v -> openNewAppsList());

        homeToolRepository.getHomeToolSorted().observe(this, list -> toolsAdapter.submitList(list));

        RecyclerView editAppsList = findViewById(R.id.edit_tools_list);
        editAppsList.setLayoutManager(new NoScrollLinearLayout(this, LinearLayoutManager.HORIZONTAL));

        toolsAdapter = new ManageToolsAdapter(getPackageManager());
        toolsAdapter.setOnListChange(this);
        toolsAdapter.setOnItemClick(this::openEditAppsList);

        editAppsList.setAdapter(toolsAdapter);

        ItemTouchHelper touchHelper = new ManageToolsTouchHelper(toolsAdapter);
        touchHelper.attachToRecyclerView(editAppsList);
    }

    private void openNewAppsList() {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_NEW_APP);
    }

    private void openEditAppsList(HomeTool tool) {
        Intent appsListIntent = new Intent(this, AppsList.class);
        startActivityForResult(appsListIntent, REQ_EDIT_APP);
        editTool = tool;
    }

    @Override
    protected void onAppSelected(int requestCode, ResolveInfo info) {
        HomeTool homeTool = resolveToHomeTool(info);
        switch (requestCode) {
            case REQ_NEW_APP:
                toolsAdapter.addItem(homeTool);
                break;
            case REQ_EDIT_APP:
                editTool.copyValuesFrom(homeTool);
                toolsAdapter.updateItem(editTool);
                break;
        }
    }

    private HomeTool resolveToHomeTool(ResolveInfo resolveInfo) {
        ActivityInfo info = resolveInfo.activityInfo;
        return new HomeTool(info.packageName, info.name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editTool = null;
    }

    @Override
    public void onItemAdded(HomeTool tool) {
        homeToolRepository.insert(tool);
    }

    @Override
    public void onItemDeleted(HomeTool tool) {
        homeToolRepository.delete(tool);
    }

    @Override
    public void syncItems(List<HomeTool> tools) {
        homeToolRepository.update(tools.toArray(new HomeTool[0]));
    }
}
