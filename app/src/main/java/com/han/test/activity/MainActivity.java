package com.han.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.han.test.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private  static final String TAG = "MainActivity";

    private TextView tvHeart, tvCrazy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCrazy = (TextView) findViewById(R.id.tv_crazy);
        tvCrazy.setOnClickListener(this);

        tvHeart = (TextView) findViewById(R.id.tv_heart);
        tvHeart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvHeart) {
            startActivity(new Intent(MainActivity.this, HeartActivity.class));
        } else if (v == tvCrazy) {
            startActivity(new Intent(MainActivity.this, BezierActivity.class));
        }
    }
}
