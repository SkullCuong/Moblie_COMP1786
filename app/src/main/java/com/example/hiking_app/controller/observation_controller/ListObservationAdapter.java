package com.example.hiking_app.controller.observation_controller;

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
import com.example.hiking_app.model.Observations;

import java.util.List;

public class ListObservationAdapter extends RecyclerView.Adapter<ListObservationAdapter.ObervationViewholder> {
    TextView nameTextView, dateTextView;
    ImageView image;

    Button update,delete;
    private List<Observations> observations;
    private LayoutInflater inflater;
    private Context context;
    LinearLayout ObsCardObj;
    public ListObservationAdapter(List<Observations> observations, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.observations = observations;
    }

    @NonNull
    @Override
    public ObervationViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_view_observation, null);
        return new ListObservationAdapter.ObervationViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObervationViewholder holder, int position) {
        holder.bindData(observations.get(position));
    }

    @Override
    public int getItemCount() {
        return observations.size();
    }


    public class ObervationViewholder extends RecyclerView.ViewHolder{

        public ObervationViewholder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.observationName);
            dateTextView = itemView.findViewById(R.id.observationDate);
            image = itemView.findViewById(R.id.observationImage);
            ObsCardObj = itemView.findViewById(R.id.obsCardObj);
            //update = itemView.findViewById(R.id.updateObservation);
            //delete = itemView.findViewById(R.id.deleteObservation);
        }
        public void bindData(Observations observation) {
                nameTextView.setText(observation.getName());
                dateTextView.setText(observation.getDateTime());
                image.setImageBitmap(getUserImage(observation.getPhoto()));
                // Set the ID for the current item
                final int itemId = observation.getId();
                ObsCardObj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ObservationDetails.class);
                        intent.putExtra("observationId", itemId);
                        context.startActivity(intent);
                    }
                });


            /*update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateObservation.class);
                    intent.putExtra("observationId", itemId);
                    context.startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Observations observation= DbContext.getInstance(this).appDao().getObservationById(itemId);
                    DbContext.getInstance(this).appDao().deleteObservation(observation);
                }
            });*/
        }
    }
    private Bitmap getUserImage(String image) {
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
