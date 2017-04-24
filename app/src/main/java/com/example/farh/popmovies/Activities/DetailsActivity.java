package com.example.farh.popmovies.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farh.popmovies.Adapters.MovieAdapter;
import com.example.farh.popmovies.Fragment.MovieFragment;
import com.example.farh.popmovies.R;
import com.example.farh.popmovies.Utils.Urls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailsActivity extends AppCompatActivity {


    private Toolbar mToolBar;
    private CollapsingToolbarLayout mCollToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Regular.ttf")
        .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_details);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();
    }

    private void initCollapsingToolbar() {
        mCollToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        mCollToolBar.setTitle(getIntent().getExtras().getString("title"));
        ImageView mBackdrop = (ImageView) findViewById(R.id.backdrop);
        String backdropURL = Urls.POSTER_URL + getIntent().getExtras().getString("backdrop");
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(false)
                .displayer(new FadeInBitmapDisplayer(2000)).resetViewBeforeLoading(true).showImageOnFail(R.mipmap.no_poster)
                .showImageForEmptyUri(R.mipmap.no_poster).build();

        MovieAdapter.imageLoader.displayImage(backdropURL , mBackdrop , imageOptions , new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Palette palette = Palette.from(loadedImage).generate();
                mCollToolBar.setBackgroundColor(palette.getVibrantColor(0));
                mCollToolBar.setContentScrimColor(palette.getVibrantColor(0));
                if (palette.getVibrantColor(0) == Color.TRANSPARENT){
                    mCollToolBar.setBackgroundColor(palette.getMutedColor(0));
                    mCollToolBar.setContentScrimColor(palette.getMutedColor(0));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        MovieFragment.adapter.notifyDataSetChanged();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
