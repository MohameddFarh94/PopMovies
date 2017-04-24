package com.example.farh.popmovies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farh.popmovies.Activities.DetailsActivity;
import com.example.farh.popmovies.Activities.MainActivity;
import com.example.farh.popmovies.Fragment.DetailsFragment;
import com.example.farh.popmovies.Fragment.MovieFragment;
import com.example.farh.popmovies.Models.Movie;
import com.example.farh.popmovies.R;
import com.example.farh.popmovies.Utils.DatabaseHandler;
import com.example.farh.popmovies.Utils.Urls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import static com.example.farh.popmovies.Fragment.MovieFragment.adapter;
import static com.example.farh.popmovies.Fragment.MovieFragment.mGridMovies;
import static com.example.farh.popmovies.Fragment.MovieFragment.up;


import java.util.List;

/**
 * Created by farh on 18/10/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesHolder>{
    public static ImageLoader imageLoader;
    Context context;
    public static  List<Movie> movieList;
    private Activity activity;
    private DatabaseHandler handler;


    public MovieAdapter(Context context, List<Movie> movieList , Activity activity) {
        this.context = context;
        this.movieList = movieList;
        this.activity = activity;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public MovieAdapter.MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        handler = new DatabaseHandler(context);
        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MoviesHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        holder.mPosterFav.setVisibility(View.GONE);
        holder.mPosterUnFav.setVisibility(View.VISIBLE);
        holder.mPosterFav.invalidate();
        holder.mPosterUnFav.invalidate();

        String posterUrl = Urls.POSTER_URL + movie.getPosterPath();
        DisplayImageOptions imageOptions = new  DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(false).displayer(new FadeInBitmapDisplayer(2000))
                .resetViewBeforeLoading(true).showImageOnFail(R.mipmap.no_poster).showImageForEmptyUri(R.mipmap.no_poster).build();
        imageLoader.displayImage(posterUrl , holder.mPoster , imageOptions , new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Palette palette = Palette.from(loadedImage).generate();
                holder.mPosterTitleBackground.setBackgroundColor(palette.getVibrantColor(0));
                if (palette.getVibrantColor(0) == Color.TRANSPARENT){
                    holder.mPosterTitleBackground.setBackgroundColor(palette.getMutedColor(0));
                }
                holder.mPosterTitleBackground.getBackground().setAlpha(150);
            }
        });
        String date = movie.getDate();
        String year;
        if (date.equals("")){
            year ="Unknown";
        }else{
             year = date.substring(0, 4);

        }
        holder.mPosterYear.setText(year);
        holder.mPosterTitle.setText(movie.getTitle());
        holder.mPosterTitle.setSelected(true);

        final ScaleAnimation animation = new ScaleAnimation(0f , 1f , 0f , 1f , ScaleAnimation.RELATIVE_TO_SELF , 0.5f
        , Animation.RELATIVE_TO_SELF , 0.5f);
        animation.setDuration(150);

        holder.mPosterFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mPosterFav.setVisibility(View.GONE);
                holder.mPosterUnFav.setVisibility(View.VISIBLE);
                holder.mPosterUnFav.setAnimation(animation);

                handler.deleteRow(movie.getId());

                if (MovieFragment.fav.isChecked()){
                    movieList.remove(position);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemRemoved(position -1);
                    mGridMovies.scrollToPosition(position -1);
                }
            }
        });
        holder.mPosterUnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mPosterUnFav.setVisibility(View.GONE);
                holder.mPosterFav.setVisibility(View.VISIBLE);
                holder.mPosterFav.setAnimation(animation);

                handler.insertRow(movie.getPosterPath() , movie.getOverview() , movie.getId() , movie.getTitle() ,
                        movie.getBackdrop() , movie.getVoteAverage() , movie.getDate());
            }
        });

        boolean ifExist = handler.ifExist(movie.getId());
        if (ifExist){
            holder.mPosterFav.setVisibility(View.VISIBLE);
            holder.mPosterUnFav.setVisibility(View.GONE);
        }else{
            holder.mPosterUnFav.setVisibility(View.VISIBLE);
            holder.mPosterFav.setVisibility(View.GONE);
        }

        holder.mGridCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration conf = v.getResources().getConfiguration();
                if (conf.smallestScreenWidthDp >= 600){
                    DetailsFragment detailsFragment = new DetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("movie" , movie);
                    bundle.putInt("pos" , position);
                    detailsFragment.setArguments(bundle);
                    activity.getFragmentManager().beginTransaction().replace(R.id.detailFrameContainer , detailsFragment).commit();
                    MainActivity.cornStamp.setVisibility(View.GONE);
                }else{
                    Intent intent = new Intent(context , DetailsActivity.class);
                    String year , date ;
                    date = movie.getDate();
                    String title = movie.getTitle();
                    String overView = movie.getOverview();
                    if (date.equals("")){
                        year = "Unknown";
                    }else{
                        year = date.substring(0 , 4);
                    }
                    if (title.equals("")){
                        title = "Not available";
                    }else{
                        title = movie.getTitle();
                    }
                    if (overView.equals("")){
                        overView = "No overview found";
                    }else{
                        overView = movie.getOverview();
                    }
                    intent.putExtra("poster" , movie.getPosterPath());
                    intent.putExtra("overview" , overView);
                    intent.putExtra("id" , movie.getId());
                    intent.putExtra("title" , title);
                    intent.putExtra("backdrop" , movie.getBackdrop());
                    intent.putExtra("voteAverage" , movie.getVoteAverage());
                    intent.putExtra("year" , year);
                    intent.putExtra("position" , position);
                    context.startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MoviesHolder extends RecyclerView.ViewHolder{

        private final ImageView mPoster , mPosterFav , mPosterUnFav;
        private final View mPosterTitleBackground;
        private final TextView mPosterTitle , mPosterYear;
        private final CardView mGridCard;

        public MoviesHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.gridPoster);
            mPosterTitleBackground = itemView.findViewById(R.id.posterTitleBackground);
            mPosterTitle = (TextView) itemView.findViewById(R.id.posterTitle);
            mPosterYear = (TextView) itemView.findViewById(R.id.posterYear);
            mGridCard = (CardView) itemView.findViewById(R.id.gridCard);
            mPosterFav = (ImageView) itemView.findViewById(R.id.posterFav);
            mPosterUnFav = (ImageView) itemView.findViewById(R.id.posterUnFav);

        }
    }
    public void clear() {
        if (movieList != null) {
            int size = movieList.size();
            movieList.clear();
            notifyItemRangeRemoved(0, size);
        }

    }
}
