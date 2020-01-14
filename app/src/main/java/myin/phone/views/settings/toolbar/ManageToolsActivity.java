package myin.phone.views.settings.toolbar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import myin.phone.R;
import myin.phone.data.tool.HomeTool;
import myin.phone.data.tool.HomeToolRepository;
import myin.phone.list.OnListChangeListener;
import myin.phone.list.NoScrollLinearLayout;

import javax.inject.Inject;
import java.util.List;

public class ManageToolsActivity extends AppCompatActivity implements OnListChangeListener<HomeTool> {

    private ManageToolsAdapter toolsAdapter;

    @Inject
    HomeToolRepository homeToolRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_tools_edit);

        homeToolRepository.getHomeToolSorted().observe(this, list -> {
            toolsAdapter.submitList(list);
        });

        findViewById(R.id.add_phone).setOnClickListener(v -> {

        });

        RecyclerView editAppsList = findViewById(R.id.edit_apps_list);
        editAppsList.setLayoutManager(new NoScrollLinearLayout(this));
        editAppsList.setHasFixedSize(true);

        toolsAdapter = new ManageToolsAdapter();
        toolsAdapter.setOnListChange(this);
//        toolsAdapter.setOnItemClick(this::openEditAppsList);

        editAppsList.setAdapter(toolsAdapter);

        ItemTouchHelper touchHelper = new ManageToolsTouchHelper(toolsAdapter);
        touchHelper.attachToRecyclerView(editAppsList);
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
