package myin.phone.views.settings.apps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import lombok.Setter;
import myin.phone.R;
import myin.phone.data.BaseAppListAdapter;
import myin.phone.data.app.HomeApp;
import myin.phone.data.BaseAppDiffCallback;
import myin.phone.list.TextViewHolder;

@Setter
public class ManageAppsAdapter extends BaseAppListAdapter<HomeApp, ManageAppsAdapter.ManageAppView> {

    public ManageAppsAdapter() {
        super(new BaseAppDiffCallback<>());
    }

    @NonNull
    @Override
    public ManageAppView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_app_item, parent, false);

        return new ManageAppView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageAppView holder, int position) {
        HomeApp app = getItem(position);
        holder.setText(app.label);
        holder.setOnTextClick(v -> {
            if (onItemClick != null) {
                onItemClick.accept(app);
            }
        });
    }

    public static class ManageAppView extends TextViewHolder {
        public ManageAppView(View view) {
            super(view, view.findViewById(R.id.text));
        }
    }
}
