package com.example.hiking_app.controller.hike_controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;
import com.example.hiking_app.model.Users;

import java.util.List;

public class ListHikeAdapter extends RecyclerView.Adapter<ListHikeAdapter.HikesViewHolder> {

    private List<Hikes> hikes;
    private LayoutInflater inflater;
    private Context context;
    private List<Hikes> filteredList;
    public ListHikeAdapter(List<Hikes> hikes, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.hikes = hikes;
    }
    @NonNull
    @Override
    public HikesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_view_hike, null);
        return new ListHikeAdapter.HikesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HikesViewHolder holder, int position) {
        holder.bindData(hikes.get(position));
    }
    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public class HikesViewHolder extends RecyclerView.ViewHolder {
        ImageView HikeImg, hikeImg1;
        TextView name, location, date, length;
        Button buttonUpdate;
        CardView cardView;


        public HikesViewHolder(@NonNull View itemView) {
            super(itemView);
            HikeImg = itemView.findViewById(R.id.hikeImage);
            name = itemView.findViewById(R.id.hikeName);
            location = itemView.findViewById(R.id.hikeLocation);
            date = itemView.findViewById(R.id.hikeDate);
            length = itemView.findViewById(R.id.hikeLength);
            hikeImg1 = itemView.findViewById(R.id.img1);
            buttonUpdate = itemView.findViewById(R.id.HikeUpdate);
            cardView = itemView.findViewById(R.id.cardHike);
        }
        public void setFilteredList(List<Hikes> filteredList){

        }
        void bindData(final Hikes hike) {

            Users user = DbContext.getInstance(context).appDao().findUserById(hike.getUserId());
            List<Observations> observations = DbContext.getInstance(context).appDao().getObservationsByHikeId(hike.getId());
            //personImage.setImageBitmap(getUserImage(hike.getImage()));
            //HikeImg.setImageBitmap(getImage(user.getProfile_Picture()));
            try {
                Bitmap imageBitmap = getImage(user.getProfile_Picture());
                if (imageBitmap != null) {
                    HikeImg.setImageBitmap(imageBitmap);
                } else {
                    Log.e("ImageError", "Can't convert image");
                }
            } catch (Exception e) {
                Log.e("ImageError", "Error when load the image: " + e.getMessage());
            }

            SessionManager sessionManager = new SessionManager(context);
            int username1 = sessionManager.getKeyUserid();
            name.setText(hike.getName() + username1);
            date.setText(hike.getDate());
            location.setText(hike.getLocation());
            length.setText(String.valueOf(hike.getLength()));
            if (!observations.isEmpty()) {
                hikeImg1.setImageBitmap(getImage(observations.get(0).getPhoto()));
            }
            //hikeImg1.setImageBitmap(getImage(observations[0].getPhoto()));
            // Set the ID for the current item
            final int itemId = hike.getId();
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateHike.class);
                    intent.putExtra("hike_id", itemId);
                    context.startActivity(intent);
                }
            });
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, HikeDetails.class);
                    intent.putExtra("hike_id", itemId);
                    context.startActivity(intent);
                }
            });
        }
    }
    public void setFilteredList(List<Hikes> filteredList) {
        this.hikes.clear();
        this.hikes.addAll(filteredList);
        notifyDataSetChanged();
    }

private Bitmap getImage(String image) {
    try {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    } catch (Exception e) {
        Log.e("ImageError", "Error when convert the image (at getImage): " + e.getMessage());
        return null;
    }
}
}
