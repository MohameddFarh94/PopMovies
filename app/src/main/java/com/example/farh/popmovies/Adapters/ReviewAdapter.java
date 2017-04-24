package com.example.farh.popmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.farh.popmovies.Models.Review;
import com.example.farh.popmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farh on 06/11/2016.
 */

public class ReviewAdapter extends BaseAdapter{
    Context context;
    List<Review> reviewList = new ArrayList<>();

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_row , parent , false);
            holder = new ReviewViewHolder();
            holder.revName = (TextView) convertView.findViewById(R.id.revName);
            holder.revContent = (TextView) convertView.findViewById(R.id.revContent);
            convertView.setTag(holder);
        }else{
            holder = (ReviewViewHolder) convertView.getTag();
        }
        Review review = reviewList.get(position);
        holder.revName.setText(review.getAuthor());
        holder.revContent.setText(review.getContent());
        return convertView;
    }
    static class ReviewViewHolder{
        TextView revName;
        TextView revContent;
    }
}
