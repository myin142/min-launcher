package io.github.myin.phone.views.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import io.github.myin.phone.R;

/**
 * Simple activity to create a new ToDo item.
 * Returns the entered text in the result intent under key RESULT_TODO_TEXT.
 */
public class TodoActivity extends AppCompatActivity {

    public static final String RESULT_TODO_TEXT = "todo_text";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_activity);

        EditText input = findViewById(R.id.input);
        if (input.requestFocus()) {
            new WindowInsetsControllerCompat(getWindow(), input).show(WindowInsetsCompat.Type.ime());
        }

        // Handle IME action (Done) to finish and return the text
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    submitAndFinish();
                    return true;
                }
                return false;
            }

            private void submitAndFinish() {
                String text = input.getText() != null ? input.getText().toString().trim() : "";
                Intent result = new Intent();
                result.putExtra(RESULT_TODO_TEXT, text);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }
}

