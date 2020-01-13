package myin.phone.views.settings.toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.annimon.stream.function.Consumer;
import lombok.Setter;
import myin.phone.R;
import myin.phone.data.BaseAppDiffCallback;
import myin.phone.data.BaseAppListAdapter;
import myin.phone.data.app.HomeApp;
import myin.phone.data.tool.HomeTool;
import myin.phone.list.TextViewHolder;

@Setter
public class ManageToolsAdapter extends BaseAppListAdapter<HomeTool, ManageToolsAdapter.ManageToolView> {

    public ManageToolsAdapter() {
        super(new BaseAppDiffCallback<>());
    }

    @NonNull
    @Override
    public ManageToolView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_tool_item, parent, false);

        return new ManageToolView(view, view.findViewById(R.id.icon));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageToolView holder, int position) {
        HomeTool app = getItem(position);
        holder.setOnImageClick(v -> {
            if (onItemClick != null) {
                onItemClick.accept(app);
            }
        });
    }

    public static class ManageToolView extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ManageToolView(View view, ImageView imageView) {
            super(view);
            this.imageView = imageView;
        }

        public void setOnImageClick(View.OnClickListener clickListener) {
            this.imageView.setOnClickListener(clickListener);
        }
    }
}
