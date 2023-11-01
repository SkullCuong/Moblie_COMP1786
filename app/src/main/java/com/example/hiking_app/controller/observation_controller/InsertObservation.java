package com.example.hiking_app.controller.observation_controller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.databinding.ActivityInsertObservationBinding;
import com.example.hiking_app.model.Observations;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class InsertObservation extends AppCompatActivity {
    private ActivityInsertObservationBinding binding;
    private DatePickerDialog datePickerDialog;
    private String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_observation);
        binding = ActivityInsertObservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.ObAdd.setOnClickListener(v ->{
            insertOb();
        });
        binding.ObDate.setOnClickListener(v ->{
            getCalendar();
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
    private void insertOb() {
        String name = binding.ObName.getText().toString().trim();
        String date = binding.ObDate.getText().toString().trim();
        String comment = binding.ObComment.getText().toString().trim();


        int hikeId = getIntent().getIntExtra("hike_id", -1);

        if (name.isEmpty() || date.isEmpty() || comment.isEmpty() || encodedImage.isEmpty()) {
            showMessage("Please fill in all fields and select/capture an image.");
            return;
        }

        Observations observation = new Observations(name, date, comment, encodedImage, hikeId);

        DbContext.getInstance(this.getApplicationContext()).appDao().insertObservations(observation);
        System.out.println(observation);
        showMessage("Add observation successful");
    }


    private void getCalendar() {
        Calendar calendar = Calendar.getInstance();
        int currentDay  = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth  = calendar.get(Calendar.MONTH);
        datePickerDialog = new DatePickerDialog(InsertObservation.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.ObDate.setText(day + "/" + (month + 1) + "/" +year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            // here I used it to execute an action and the return result
            new ActivityResultContracts.StartActivityForResult(),
            // it it Lambda ()
            result -> {
                //check if success or not (RESULT_OK is used to point that this action run without error)
                if(result.getResultCode() == RESULT_OK){
                    // check value
                    if(result.getData() != null){
                        // get date of image choosed
                        Uri imageUri = result.getData().getData();
                        try {
                            // Input is Image from URL
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            // use it to convert Uri from string to bitmap
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            // diplay image choosed
                            binding.imageView.setImageBitmap(bitmap);
//                            binding.textAddImage.setVisibility(View.GONE);
                            // encrypt this image into string base64
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
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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

    private final ActivityResultLauncher<Intent> pickImageLauncher  = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageView.setImageBitmap(bitmap);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}
