package myin.phone.apps;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.RequiredArgsConstructor;
import myin.phone.R;

import java.util.List;

@RequiredArgsConstructor
public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.AppListItemView> {

    private final List<AppItem> appList;
    private OnAppClickListener appClickListener;

    @NonNull
    @Override
    public AppListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_item, parent, false);
        return new AppListItemView(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListItemView holder, int position) {
        CharSequence appName = appList.get(position).getName();
        holder.textView.setText(appName);

        holder.textView.setOnClickListener(v -> {
            if (appClickListener != null) {
                appClickListener.onClick(appList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class AppListItemView extends RecyclerView.ViewHolder {

        private TextView textView;

        public AppListItemView(@NonNull TextView itemView) {
            super(itemView);
            textView = itemView;
        }
    }

    public void onAppClickListener(OnAppClickListener appClickListener) {
        this.appClickListener = appClickListener;
    }

}
