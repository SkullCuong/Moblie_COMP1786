package com.example.hiking_app.controller.observation_controller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.HikeDetails;
import com.example.hiking_app.controller.hike_controller.ListHikeAdapter;
import com.example.hiking_app.controller.observation_controller.ViewListObservations;
import com.example.hiking_app.databinding.ActivityUpdateObservationBinding;
import com.example.hiking_app.model.Observations;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateObservation extends AppCompatActivity {
    private ActivityUpdateObservationBinding binding;
    private  int observationId;
    private String encodedImage;
    String img;
    private Observations observation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);
        binding = ActivityUpdateObservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        observationId = getIntent().getIntExtra("observationId", -1);
        setData(observationId);
        setListener();
    }

    private void setListener() {
        binding.BtnUpdateObservation.setOnClickListener( v ->{
            updateObservation(observationId);
        });
        binding.arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateObservation.this, HikeDetails.class);
                intent.putExtra("hike_id", observation.getHikeId());
                startActivity(intent);
            }
        });
        binding.ObPicture.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);

        });
        binding.btnCaptureImage.setOnClickListener(v -> {
            // Open camera to capture image
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImage.launch(cameraIntent);
        });
    }
    private final ActivityResultLauncher<Intent> captureImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    if (imageBitmap != null) {
                        binding.capturedImageView.setImageBitmap(imageBitmap);
                        binding.capturedImageView.setVisibility(View.VISIBLE);
                        encodedImage = encodedImage(imageBitmap);
                    }
                }
            }
    );
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            // here I used it to execute an action and the return result
            new ActivityResultContracts.StartActivityForResult(),
            // it it Lambda ()
            result -> {
                //check if success or not (RESULT_OK is used to point that this action run without error)
                if(result.getResultCode() == RESULT_OK){
                    // check value
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.ObsPhoto.setImageBitmap(bitmap);
                            encodedImage = encodedImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodedImage(Bitmap bitmap){
        // First we determine the size
        // we fixed width
        int previewWidth = 150;
        // Calculate the height to the image not changed too much
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        // then we can review the image with these size but in small size then it is easy to store and transfer
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        //ByteArrayOutputStream, here I used it to store image after compressing
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // using preview image to compress with the quality 50 and finally it will store in byteArrayOutputStream
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        // then transfer to byte arrays
        byte[] bytes = byteArrayOutputStream.toByteArray();
        // use Base64 encrypt bytes array to string based64
        return Base64.encodeToString(bytes, Base64.DEFAULT);
        // the result we will be used to transfer or store under the string
    }

    private void updateObservation(int observationId) {
        Observations observationTemp = DbContext.getInstance(this.getApplicationContext()).appDao().getObservationById(observation.getId());
        String name = binding.ObsName.getText().toString();
        String date = binding.ObsDate.getText().toString();
        String comment = binding.ObsComment.getText().toString();
        String photo = encodedImage;
        if(observationTemp.getPhoto() != null){
            photo = observationTemp.getPhoto();
        }
        observation.setPhoto(photo);
        observation.setName(name);
        observation.setComment(comment);
        observation.setDateTime(date);
        DbContext.getInstance(this.getApplicationContext()).appDao().updateObservation(observation);
        Intent intent = new Intent(this, HikeDetails.class);
        intent.putExtra("hike_id", observation.getHikeId());

        startActivity(intent);
    }

    private void setData(int observationId) {
        observation = DbContext.getInstance(this.getApplicationContext()).appDao().getObservationById(observationId);
        binding.ObsPhoto.setImageBitmap(getImage(observation.getPhoto()));
        binding.ObsName.setText(observation.getName().toString());
        binding.ObsComment.setText(observation.getComment().toString());
        binding.ObsDate.setText(observation.getDateTime().toString());
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