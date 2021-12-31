package com.example.natour21;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;
    private ArrayList<ReviewItem> mReviewList;

    public ReviewAdapter(Context context, ArrayList<ReviewItem> reviewList){
        mContext=context;
        mReviewList=reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.review_template,parent,false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewItem currentItem = mReviewList.get(position);

        String description = currentItem.getDescription();
        float rating = currentItem.getRating();

        holder.mDescription.setText(description);
        holder.mRatingBar.setRating(rating);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView mDescription;
        public RatingBar mRatingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mDescription = itemView.findViewById(R.id.reviewDescription_textView);
            mRatingBar = itemView.findViewById(R.id.ratingBar2);
        }
    }
}
