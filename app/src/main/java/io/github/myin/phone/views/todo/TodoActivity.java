package io.github.myin.phone.views.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;
import io.github.myin.phone.R;
import io.github.myin.phone.data.todo.TodoItem;
import io.github.myin.phone.data.todo.TodoItemDiffCallback;
import io.github.myin.phone.data.todo.TodoItemRepository;
import io.github.myin.phone.list.TextListAdapter;
import io.github.myin.phone.utils.FeaturePreference;
import io.github.myin.phone.views.home.TodoTouchHelper;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class TodoActivity extends AppCompatActivity {

    @Inject
    TodoItemRepository todoItemRepository;

    private EditText input;
    private TextListAdapter<TodoItem> listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        setTheme(FeaturePreference.getThemeResourceId());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_activity);

        if (FeaturePreference.getLayoutDirection() == FeaturePreference.LayoutDirection.LEFT) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.anim_right_in, R.anim.anim_right_out);
        } else {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.anim_left_in, R.anim.anim_left_out);
        }

        findViewById(R.id.root).setLayoutDirection(FeaturePreference.getLayoutDirection().getValue());

        listAdapter = new TextListAdapter<>(new TodoItemDiffCallback(), getResources().getDimensionPixelSize(R.dimen.text_size));
        listAdapter.setDisplayFunction(item -> item.title);
        RecyclerView todoView = findViewById(R.id.todo_list);
        todoView.setLayoutManager(new LinearLayoutManager(this));
        todoView.setAdapter(listAdapter);
        todoView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard();
                }
            }
        });

        TodoTouchHelper touchHelper = new TodoTouchHelper(listAdapter, todoItemRepository);
        touchHelper.attachToRecyclerView(todoView);

        todoItemRepository.getAll().observe(this, list -> listAdapter.submitList(list));

        input = findViewById(R.id.input);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    createItem();
                    return true;
                }
                return false;
            }

            private void createItem() {
                String text = input.getText() != null ? input.getText().toString().trim() : "";
                if (!text.isBlank()) {
                    TodoItem item = new TodoItem();
                    item.title = text;
                    todoItemRepository.insert(item);
                    input.setText("");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        focusSearchInput();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    private void focusSearchInput() {
        if (input.requestFocus()) {
            new WindowInsetsControllerCompat(getWindow(), input).show(WindowInsetsCompat.Type.ime());
        }
    }
}

