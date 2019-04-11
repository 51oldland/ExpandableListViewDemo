package com.example.administrator.expandablelistviewdemo1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        TextView con = (TextView) findViewById(R.id.content);
        con.setText(getIntent().getStringExtra("content"));
    }
}
