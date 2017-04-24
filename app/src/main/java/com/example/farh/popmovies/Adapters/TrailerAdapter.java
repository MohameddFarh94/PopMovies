package com.example.farh.popmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farh.popmovies.Fragment.DetailsFragment;
import com.example.farh.popmovies.Models.Trailer;
import com.example.farh.popmovies.R;
import com.example.farh.popmovies.Utils.Urls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farh on 05/11/2016.
 */

public class TrailerAdapter extends BaseAdapter{
    Context context;
    List<Trailer> trailers = new ArrayList<>();

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderTrailer holderTrailer;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trailer_row, parent, false);
            holderTrailer = new ViewHolderTrailer();
            holderTrailer.tubeThumb = (ImageView) convertView.findViewById(R.id.tubeThumb);
            holderTrailer.trailerTitle = (TextView) convertView.findViewById(R.id.trailerTitle);
            convertView.setTag(holderTrailer);
        }else{
            holderTrailer = (ViewHolderTrailer) convertView.getTag();
        }
        Trailer trailer = trailers.get(position);
        String thumbUrl = Urls.THUMB_URL + trailer.getKey()  + Urls.PARAM_THUMB;
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false)
                .displayer(new FadeInBitmapDisplayer(2000)).showImageForEmptyUri(R.mipmap.no_poster)
                .showImageOnFail(R.mipmap.no_poster)
                .resetViewBeforeLoading(true).build();
        MovieAdapter.imageLoader.displayImage(thumbUrl , holderTrailer.tubeThumb , imageOptions);
        holderTrailer.tubeThumb.setImageResource(R.mipmap.vid_hover);
        holderTrailer.trailerTitle.setText(trailer.getName());



        DetailsFragment.mDetail_trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String vidUrl = Urls.VID_URL + trailers.get(position).getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(vidUrl));
                context.startActivity(intent);
            }
        });

        return convertView;
    }
    static class ViewHolderTrailer{
        ImageView tubeThumb;
        TextView trailerTitle;
    }
}
