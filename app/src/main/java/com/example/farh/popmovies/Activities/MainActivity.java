package com.example.farh.popmovies.Activities;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.farh.popmovies.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    public static RelativeLayout cornStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Pop Movies");

        AppBarLayout bar = (AppBarLayout) findViewById(R.id.appBar);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainRel);
        bar.measure(WRAP_CONTENT, WRAP_CONTENT);
        int num = bar.getMeasuredHeight();
        layout.setPadding(0, num, 0, 0);

        cornStamp = (RelativeLayout) findViewById(R.id.cornStamp);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}
