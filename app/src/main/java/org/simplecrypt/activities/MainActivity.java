package org.simplecrypt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.simplecrypt.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.files:
                startActivityForResult(new Intent(getApplicationContext(), Files.class), 1);
                break;

            case R.id.text:
                startActivityForResult(new Intent(getApplicationContext(), Text.class), 2);
                break;
        }
    }
}
