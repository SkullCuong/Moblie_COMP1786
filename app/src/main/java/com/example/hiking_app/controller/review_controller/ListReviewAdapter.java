package com.example.hiking_app.controller.review_controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.observation_controller.UpdateObservation;
import com.example.hiking_app.controller.review_controller.ListReviewAdapter;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

import java.util.List;

public class ListReviewAdapter extends RecyclerView.Adapter<ListReviewAdapter.ReviewsViewHolder>{
    private List<Reviews> reviews;
    private LayoutInflater inflater;
    private Context context;
    private List<Reviews> filteredList;
    public ListReviewAdapter(List<Reviews> reviews, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.reviews = reviews;
    }
    @NonNull
    @Override
    public ListReviewAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_view_review, null);
        return new ListReviewAdapter.ReviewsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ListReviewAdapter.ReviewsViewHolder holder, int position) {
        holder.bindData(reviews.get(position));
    }
    @Override
    public int getItemCount() {
        return reviews.size();
    }
    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userName, rate, comment;
        Button buttonEdit;
        Button buttonDelete;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            //userImg = itemView.findViewById(R.id);
            userName = itemView.findViewById(R.id.userName);
            //rate = itemView.findViewById(R.id.hikeRate);
            comment = itemView.findViewById(R.id.comment);
            buttonEdit = itemView.findViewById(R.id.editBtn);
            buttonDelete = itemView.findViewById(R.id.deleteBtn);
        }

        void bindData(final Reviews review) {

            //personImage.setImageBitmap(getUserImage(hike.getImage()));
//            HikeImg.setImageBitmap(getUserImage());
            Users user = DbContext.getInstance(context).appDao().findUserById(review.getUserId());
            userName.setText(user.getUserName());
            //rate.setText(String.valueOf(review.getRating()));
            comment.setText(review.getComment());
            // Set the ID for the current item
            final int itemId = review.getId();


            buttonDelete.setOnClickListener(v -> {
                int userAction = 1;
                int reviewOwner = user.getId();
                if(userAction == reviewOwner) {
                    DbContext.getInstance(context).appDao().deleteReviewById(review.getId());
                    showMessage("Delete success!");
                }else{
                    showMessage("You are not auth to do this action!");
                }
            });
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateReview.class);
                    intent.putExtra("review_id", itemId);
                    context.startActivity(intent);
                }
            });

        }
    }
//    public void setFilteredList(List<Hikes> filteredList) {
//        this.hikes.clear();
//        this.hikes.addAll(filteredList);
//        notifyDataSetChanged();
//    }
    private Bitmap getUserImage(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
    private void showMessage(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
