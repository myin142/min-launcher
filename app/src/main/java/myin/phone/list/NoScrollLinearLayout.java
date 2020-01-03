package myin.phone.list;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NoScrollLinearLayout extends LinearLayoutManager {
    public NoScrollLinearLayout(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
