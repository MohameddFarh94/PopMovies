package com.example.farh.popmovies.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.farh.popmovies.Activities.MainActivity;
import com.example.farh.popmovies.Adapters.MoreListener;
import com.example.farh.popmovies.Adapters.MovieAdapter;
import com.example.farh.popmovies.Models.Movie;
import com.example.farh.popmovies.R;
import com.example.farh.popmovies.Utils.DatabaseHandler;
import com.example.farh.popmovies.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Pack200;

/**
 * Created by farh on 17/10/2016.
 */

public class MovieFragment extends Fragment{
    public static MenuItem up , pop , top , fav;
    public static RecyclerView mGridMovies;
    List<Movie> movieList ;
    public static MovieAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ImageView mNoConnection;
    private TextView mNoConnText;
    private String mUrL;
    private String mApiKey;
    private DatabaseHandler handler;
    private Configuration conf;
    private Animation anim;


    public MovieFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        setHasOptionsMenu(true);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler = new DatabaseHandler(getActivity());
        if (isNetworkAvailable()){
            fetchData();
            if (conf.smallestScreenWidthDp >= 600) {
                MainActivity.cornStamp.setVisibility(View.VISIBLE);
                MainActivity.cornStamp.setAnimation(anim);
            }
        } else {
                mNoConnection.setVisibility(View.VISIBLE);
                mNoConnText.setVisibility(View.VISIBLE);
        }
        anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        anim.setDuration(200);
    }

    private void init(View view) {
        mGridMovies = (RecyclerView) view.findViewById(R.id.mainGrid);
        mNoConnection = (ImageView) view.findViewById(R.id.noConnection);
        mNoConnText = (TextView) view.findViewById(R.id.noConnectionTxt);
        mApiKey = getString(R.string.API_KEY);
        mUrL = Urls.BASE_URL + Urls.POPULAR + Urls.PARAM_API + mApiKey;
        conf = getResources().getConfiguration();
        int orientation = getResources().getConfiguration().orientation;

        LinearLayoutManager mobPort = new GridLayoutManager(getActivity() , 2);
        LinearLayoutManager mobLand = new GridLayoutManager(getActivity(), 3);
        LinearLayoutManager tabPort = new GridLayoutManager(getActivity(), 1);
        LinearLayoutManager tabLand = new GridLayoutManager(getActivity(), 2);
        if (conf.smallestScreenWidthDp >= 600){
            switch (orientation){
                case Configuration.ORIENTATION_PORTRAIT :
                    mGridMovies.setLayoutManager(tabPort);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE :
                    mGridMovies.setLayoutManager(tabLand);
                    break;
            }
        }else{
            switch (orientation){
                case Configuration.ORIENTATION_PORTRAIT :
                    mGridMovies.setLayoutManager(mobPort);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE :
                    mGridMovies.setLayoutManager(mobLand);
            }
        }
        mGridMovies.setItemAnimator(new DefaultItemAnimator());
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getActivity() , movieList , getActivity());
        mGridMovies.setAdapter(adapter);

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu , menu);
//        up = menu.findItem(R.id.action_up);
//        up.setVisible(false);
        pop = menu.findItem(R.id.action_pop);
        top = menu.findItem(R.id.action_top);
        fav = menu.findItem(R.id.action_fav);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_pop :
                if (pop.isChecked()){
                    return false;
                }else{
                    pop.setChecked(true);
                    movieList.clear();
                    adapter.clear();
                    mUrL = Urls.BASE_URL + Urls.POPULAR + Urls.PARAM_API + mApiKey;
                    if (isNetworkAvailable()){
                        fetchData();
                    }else{
                        mNoConnection.setVisibility(View.VISIBLE);
                        mNoConnText.setVisibility(View.VISIBLE);
                    }

                }
                break;

            case R.id.action_top :
                if (top.isChecked()){
                    return false;
                }else{
                    top.setChecked(true);
                    movieList.clear();
                    adapter.clear();
                    mUrL = Urls.BASE_URL + Urls.TOP_RATED + Urls.PARAM_API + mApiKey;
                    if (isNetworkAvailable()){
                        fetchData();
                    }else{
                        mNoConnection.setVisibility(View.VISIBLE);
                        mNoConnText.setVisibility(View.VISIBLE);
                    }

                }
                break;

            case R.id.action_fav :
                if (fav.isChecked()){
                    return false;
                }else{
                    fav.setChecked(true);
                        movieList = handler.getFavMovies();
                        adapter = new MovieAdapter(getActivity() , movieList , getActivity());
                        mGridMovies.setAdapter(adapter);
                        if (!isNetworkAvailable()){
                            mNoConnection.setVisibility(View.GONE);
                            mNoConnText.setVisibility(View.GONE);
                        }
                }
                break;
        }
        return false;
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager mConnectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivity.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }



    private void fetchData() {

        StringRequest request = new StringRequest(Request.Method.POST, mUrL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray results = object.getJSONArray("results");
                    for (int i =0 ; i <results.length() ; i++){
                        Movie movie = new Movie();
                        JSONObject currentObject = results.getJSONObject(i);
                        movie.setPosterPath(currentObject.getString("poster_path"));
                        movie.setDate(currentObject.getString("release_date"));
                        movie.setTitle(currentObject.getString("original_title"));
                        movie.setBackdrop(currentObject.getString("backdrop_path"));
                        movie.setOverview(currentObject.getString("overview"));
                        movie.setVoteAverage(currentObject.getString("vote_average"));
                        movie.setId(currentObject.getString("id"));
                        movieList.add(movie);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
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
        }) ;
        Volley.newRequestQueue(getActivity()).add(request);
    }
}
