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
import android.widget.LinearLayout;
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

    LinearLayout imgFrame1;
    LinearLayout imgFrame2;
    LinearLayout imgFrame3;

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
        ImageView HikeImg, imgFrame1img1, imgFrame2img1, imgFrame2img2, imgFrame3img1, imgFrame3img2, imgFrame3img3;
        TextView name, location, date, length, difficulty;
        Button buttonUpdate;
        CardView cardView;


        public HikesViewHolder(@NonNull View itemView) {
            super(itemView);
            HikeImg = itemView.findViewById(R.id.hikeImage);
            name = itemView.findViewById(R.id.hikeName);
            location = itemView.findViewById(R.id.hikeLocation);
            date = itemView.findViewById(R.id.hikeDate);
            difficulty = itemView.findViewById(R.id.hikeDifficulty1);
            length = itemView.findViewById(R.id.hikeLength);

            imgFrame1img1 = itemView.findViewById(R.id.imgFrame1img1);

            imgFrame2img1 = itemView.findViewById(R.id.imgFrame2img1);
            imgFrame2img2 = itemView.findViewById(R.id.imgFrame2img2);

            imgFrame3img1 = itemView.findViewById(R.id.imgFrame3img1);
            imgFrame3img2 = itemView.findViewById(R.id.imgFrame3img2);
            imgFrame3img3 = itemView.findViewById(R.id.imgFrame3img3);

            imgFrame1 = itemView.findViewById(R.id.imgFrame1);
            imgFrame2 = itemView.findViewById(R.id.imgFrame2);
            imgFrame3 = itemView.findViewById(R.id.imgFrame3);
            //buttonUpdate = itemView.findViewById(R.id.HikeUpdate);
            cardView = itemView.findViewById(R.id.cardHike);
        }

        void bindData(final Hikes hike) {

            Users user = DbContext.getInstance(context).appDao().findUserById(hike.getUserId());

            try {
                Bitmap imageBitmap = getImage(user.getProfile_Picture());
                if (imageBitmap != null) {
                    HikeImg.setImageBitmap(imageBitmap);
                } else {
                    Log.e("ImageError", "Failed to convert image");
                }
            } catch (IllegalArgumentException e) {
                Log.e("ImageError", "IllegalArgumentException when loading the image: " + e.getMessage());
            } catch (OutOfMemoryError e) {
                Log.e("ImageError", "OutOfMemoryError when loading the image: " + e.getMessage());
            } catch (Exception e) {
                Log.e("ImageError", "Error when loading the image: " + e.getMessage());
            }

            name.setText(hike.getName());
            date.setText(hike.getDate());
            location.setText(hike.getLocation());
            length.setText(String.valueOf(hike.getLength()));
            imgFrame2.setVisibility(View.INVISIBLE);
            imgFrame1.setVisibility(View.INVISIBLE);
            imgFrame3.setVisibility(View.INVISIBLE);
            List<Observations> observations = DbContext.getInstance(context).appDao().getObservationsByHikeId(hike.getId());
            if (!observations.isEmpty()) {
                if (observations.size() < 2) {
                    imgFrame1.setVisibility(View.VISIBLE);
                    imgFrame1img1.setImageBitmap(getImage(observations.get(0).getPhoto()));
                }

                if (observations.size() == 2) {
                    imgFrame2.setVisibility(View.VISIBLE);
                    imgFrame2img1.setImageBitmap(getImage(observations.get(0).getPhoto()));
                    imgFrame2img2.setImageBitmap(getImage(observations.get(1).getPhoto()));
                }

                if (observations.size() > 2) {
                    imgFrame3.setVisibility(View.VISIBLE);

                    imgFrame3img1.setImageBitmap(getImage(observations.get(0).getPhoto()));
                    imgFrame3img2.setImageBitmap(getImage(observations.get(1).getPhoto()));
                    imgFrame3img3.setImageBitmap(getImage(observations.get(2).getPhoto()));
                }

            }

            if (hike.getDifficulty() == 1) {
                difficulty.setText("Easy");
            } else if (hike.getDifficulty() == 2) {
                difficulty.setText("Moderate");
            } else if (hike.getDifficulty() == 3) {
                difficulty.setText("Difficult");
            } else if (hike.getDifficulty() == 4) {
                difficulty.setText("Very Difficult");
            }
            // Set the ID for the current item
            final int itemId = hike.getId();
            /*buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateHike.class);
                    intent.putExtra("hike_id", itemId);
                    context.startActivity(intent);
                }
            });*/
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
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("ImageError", "IllegalArgumentException when converting the image (at getImage): " + e.getMessage());
        } catch (OutOfMemoryError e) {
            Log.e("ImageError", "OutOfMemoryError when converting the image (at getImage): " + e.getMessage());
        } catch (Exception e) {
            Log.e("ImageError", "Error when converting the image (at getImage): " + e.getMessage());
        }
        return null;
    }
}
