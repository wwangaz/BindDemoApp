package com.wangweimin.binddemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wangweimin.binddemoapp.tasks.TasksActivity;

public class MainActivity extends BasePermissionActivity {
    private static String m = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text = (TextView) findViewById(R.id.text_view);
        Button button = (Button) findViewById(R.id.button);

        if (button != null && text != null)
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    text.setText(Sample.hello() + AddClass.hello());
                    Intent intent = new Intent(getApplicationContext(), TasksActivity.class);
                    startActivity(intent);
                }
            });
    }
}
