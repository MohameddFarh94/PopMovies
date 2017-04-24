package com.example.farh.popmovies.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.farh.popmovies.Adapters.MovieAdapter;
import com.example.farh.popmovies.Adapters.ReviewAdapter;
import com.example.farh.popmovies.Adapters.TrailerAdapter;
import com.example.farh.popmovies.Models.Movie;
import com.example.farh.popmovies.Models.Review;
import com.example.farh.popmovies.Models.Trailer;
import com.example.farh.popmovies.R;
import com.example.farh.popmovies.Utils.DatabaseHandler;
import com.example.farh.popmovies.Utils.Urls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.farh.popmovies.R.id.backdrop;

/**
 * Created by farh on 29/10/2016.
 */

public class DetailsFragment extends Fragment{
    private CardView mDetailCard;
    private ImageView mDetailPoster , mDetailUnFav , mDetailFav , tabBackdrop;
    private TextView mDetailDate , mDetailDur , mDetailVoteAvr , mDetailOverview , mNoTrails , mNoRevs , detailTitle;
    public static ListView mDetail_trailers;
    private LinearLayout mRevsList;
    private String poster , backdrop , voteAverage ,overview , id , title , year;
    private List<Trailer> trailers ;
    private TrailerAdapter trailerAdapter;
    private List<Review> reviews;
    private ReviewAdapter reviewAdapter;
    private DatabaseHandler handler;
    private Movie movie;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        setHasOptionsMenu(true);
        mDetailCard = (CardView) view.findViewById(R.id.detailCard);
        mDetailPoster = (ImageView) view.findViewById(R.id.detailPoster);
        mDetailDate = (TextView) view.findViewById(R.id.detailDate);
        mDetailDur = (TextView) view.findViewById(R.id.detailDur);
        mDetailVoteAvr = (TextView) view.findViewById(R.id.detailVoteAvr);
        mDetailUnFav = (ImageView) view.findViewById(R.id.detailUnFav);
        mDetailFav = (ImageView) view.findViewById(R.id.detailFav);
        mDetailOverview = (TextView) view.findViewById(R.id.detailOverview);
        mDetail_trailers = (ListView) view.findViewById(R.id.detail_trailers);
        detailTitle = (TextView) view.findViewById(R.id.detailTitle);
        mNoTrails = (TextView) view.findViewById(R.id.noTrails);
        mNoRevs = (TextView) view.findViewById(R.id.noRevs);
        mRevsList = (LinearLayout) view.findViewById(R.id.revsList);
        tabBackdrop = (ImageView) view.findViewById(R.id.tabBackdrop);
        trailers = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        handler = new DatabaseHandler(getActivity());
        Configuration conf = getActivity().getResources().getConfiguration();
        if (conf.smallestScreenWidthDp >= 600){
            if (bundle != null){
                movie = bundle.getParcelable("movie");
                position = bundle.getInt("pos");
                Log.d("posooo" , position + "");
                if (movie != null){
                    poster = movie.getPosterPath();
                    overview = movie.getOverview();
                    id = movie.getId();
                    title = movie.getTitle();
                    backdrop = movie.getBackdrop();
                    voteAverage = movie.getVoteAverage();
                    String date = movie.getDate();
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
                    if (overview.equals("")){
                        overview = "No overview found";
                    }else{
                        overview = movie.getOverview();
                    }

                }
                Log.d("Details" , poster +"   " + overview  + "    " + title + "    " + backdrop + "    " + voteAverage);

            }
        }else{
            poster = getActivity().getIntent().getExtras().getString("poster");
            overview = getActivity().getIntent().getExtras().getString("overview");
            id = getActivity().getIntent().getExtras().getString("id");
            title = getActivity().getIntent().getExtras().getString("title");
            backdrop = getActivity().getIntent().getExtras().getString("backdrop");
            voteAverage = getActivity().getIntent().getExtras().getString("voteAverage");
            year = getActivity().getIntent().getExtras().getString("year");
            position = getActivity().getIntent().getExtras().getInt("position");
        }



        String posterDetailURL = Urls.POSTER_URL + poster;
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false)
                .showImageForEmptyUri(R.mipmap.no_poster).showImageOnFail(R.mipmap.no_poster)
                .displayer(new FadeInBitmapDisplayer(2000)).build();
        MovieAdapter.imageLoader.displayImage(posterDetailURL , mDetailPoster , imageOptions , new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Palette palette = Palette.from(loadedImage).generate();
                mDetailCard.setCardBackgroundColor(palette.getVibrantColor(0));
                if (palette.getVibrantColor(0) == Color.TRANSPARENT){
                    mDetailCard.setCardBackgroundColor(palette.getMutedColor(0));
                }
            }
        });
        String backdropUrl = "http://image.tmdb.org/t/p/w780" +  backdrop;
        if (conf.smallestScreenWidthDp >= 600){
            MovieAdapter.imageLoader.displayImage(backdropUrl , tabBackdrop , imageOptions , new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Palette palette = Palette.from(loadedImage).generate();
                    tabBackdrop.setBackgroundColor(palette.getVibrantColor(0));
                    if (palette.getVibrantColor(0) == Color.TRANSPARENT){
                        tabBackdrop.setBackgroundColor(palette.getMutedColor(0));
                    }
                }
            });
            detailTitle.setText(title);
        }
        mDetailDate.setText(year);
        mDetailOverview.setText(overview);
        mDetailVoteAvr.setText(voteAverage);

        final ScaleAnimation animation = new ScaleAnimation(0f , 1f , 0f , 1f , Animation.RELATIVE_TO_SELF , 0.5f
         , Animation.RELATIVE_TO_SELF , 0.5f);
        animation.setDuration(200);

        mDetailFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieFragment.adapter.notifyDataSetChanged();
                mDetailFav.setVisibility(View.GONE);
                mDetailUnFav.setAnimation(animation);
                mDetailUnFav.setVisibility(View.VISIBLE);

                handler.deleteRow(id);
                if (MovieFragment.fav.isChecked()){
                    MovieAdapter.movieList.remove(position);
                    MovieFragment.adapter.notifyItemRemoved(position);
                    MovieFragment.adapter.notifyDataSetChanged();
                }
            }
        });

        mDetailUnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieFragment.adapter.notifyDataSetChanged();
                mDetailUnFav.setVisibility(View.GONE);
                mDetailFav.setAnimation(animation);
                mDetailFav.setVisibility(View.VISIBLE);

                handler.insertRow(poster , overview , id , title ,
                        backdrop , voteAverage , year);
            }
        });

        boolean ifExist = handler.ifExist(id);
        if (ifExist){
            mDetailFav.setVisibility(View.VISIBLE);
            mDetailUnFav.setVisibility(View.GONE);
        }
        else{
            mDetailFav.setVisibility(View.GONE);
            mDetailUnFav.setVisibility(View.VISIBLE);
        }
        LoadDuration();
        trailerAdapter = new TrailerAdapter(getActivity(), trailers);
        loadTrailer();
        reviewAdapter = new ReviewAdapter(getActivity() , reviews);
        loadReview();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_fragment , menu);
        MenuItem share = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        if (mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share){
            createShareForecastIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT , "Check this movie: " + title +
                " from #Popular_Movies_APP");
        return shareIntent;
    }

    private void LoadDuration() {
        String durationURL = Urls.BASE_URL + id + Urls.PARAM_API + getString(R.string.API_KEY);
        final StringBuilder builder = new StringBuilder();
        StringRequest request = new StringRequest(Request.Method.GET, durationURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String runtime = object.getString("runtime");
                    boolean notAvailable = runtime.equals("null") || runtime.equals("0");
                    if (notAvailable){
                        builder.append("Not Available.");
                    }else{
                        builder.append(runtime).append("Min");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDetailDur.setText(builder);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.time_out) , Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                else if (error instanceof ServerError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }
    private void loadTrailer() {
        String trailerURL = Urls.BASE_URL + id + Urls.PARAM_TRAIL + Urls.PARAM_API + getString(R.string.API_KEY);
        StringRequest request = new StringRequest(Request.Method.GET, trailerURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray results = object.getJSONArray("results");
                    for (int i = 0 ; i <results.length() ; i++){
                        JSONObject currentObject = results.getJSONObject(i);
                        Trailer trailer = new Trailer();
                        trailer.setKey(currentObject.getString("key"));
                        trailer.setName(currentObject.getString("name"));
                        trailers.add(trailer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDetail_trailers.setAdapter(trailerAdapter);
                trailerAdapter.notifyDataSetChanged();
                fix(mDetail_trailers);
                if (trailers.isEmpty()){
                    mNoTrails.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.time_out) , Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                else if (error instanceof ServerError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }

    private void loadReview() {
        String reviewURL = Urls.BASE_URL + id + Urls.PARAM_REVS + Urls.PARAM_API + getString(R.string.API_KEY);
        StringRequest request = new StringRequest(Request.Method.GET, reviewURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray results = object.getJSONArray("results");
                    for (int i = 0 ; i< results.length() ; i++){
                        Review review = new Review();
                        JSONObject reviewJsonObject = results.getJSONObject(i);
                        review.setAuthor(reviewJsonObject.getString("author"));
                        review.setContent(reviewJsonObject.getString("content"));
                        reviews.add(review);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0 ; i < reviewAdapter.getCount() ; i++){
                    View view = reviewAdapter.getView(i, null, null);
                    mRevsList.addView(view);
                }
                reviewAdapter.notifyDataSetChanged();
                if (reviews.isEmpty()) {
                    mNoRevs.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.time_out) , Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                else if (error instanceof ServerError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public static void fix(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        desiredWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
