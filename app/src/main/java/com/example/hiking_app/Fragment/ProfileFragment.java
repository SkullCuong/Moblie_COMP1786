package com.example.hiking_app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.databinding.ActivityInsertHikeBinding;
import com.example.hiking_app.databinding.ActivityProfileBinding;
import com.example.hiking_app.databinding.FragmentProfileBinding;
import com.example.hiking_app.model.Users;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private ImageView profileImageView; // Declare profileImageView
    private FragmentProfileBinding binding;
    private DbContext dbContext;
    private SessionManager sessionManager;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dbContext = DbContext.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        profileImageView = view.findViewById(R.id.profileImageView);

        // Retrieve user ID from the session
        int userId = sessionManager.getKeyUserid();

        if (userId != -1) {
            Users user = dbContext.appDao().findUserById(userId);
            if (user != null) {
                populateUI(user);
            } else {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                // Redirect to LoginActivity or handle the situation accordingly
                getActivity().finish(); // This assumes you're in an Activity that hosts this Fragment
            }
        } else {
            // User is not logged in, redirect to LoginActivity
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            // Redirect to LoginActivity or handle the situation accordingly
            getActivity().finish(); // This assumes you're in an Activity that hosts this Fragment
        }
        Button changeImageButton = view.findViewById(R.id.changeImageButton);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the image gallery to select a profile image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        Button updateButton = view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve updated information from EditText fields
                String updatedUsername = usernameEditText.getText().toString().trim();
                String updatedEmail = emailEditText.getText().toString().trim();
                String updatedAddress = addressEditText.getText().toString().trim();

                if (updatedUsername.isEmpty()) {
                    showMessage("Please fill in name.");
                    return;
                }
                if (updatedEmail.isEmpty()) {
                    showMessage("Please fill in email.");
                    return;
                }
                if (updatedAddress.isEmpty()) {
                    showMessage("Please fill in adress.");
                    return;
                }

                // Validate the input if necessary (e.g., check if email is valid)

                // Retrieve user ID from the session
                int userId = sessionManager.getKeyUserid();

                if (userId != -1) {
                    Users user = dbContext.appDao().findUserById(userId);
                    if (user != null) {
                        // Update user object with new information
                        user.setUserName(updatedUsername);
                        user.setEmail(updatedEmail);
                        user.setAddress(updatedAddress);

                        // Update the user in the database
                        dbContext.appDao().updateUser(user);

                        // Show a toast message to indicate successful update
                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        // Optionally, you can reload the UI with the updated information
                        populateUI(user);
                    } else {
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                        // Handle the situation accordingly
                    }
                } else {
                    Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                    // Handle the situation accordingly
                }
            }
        });

    }
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Users user) {
        usernameEditText.setText(user.getUserName());
        emailEditText.setText(user.getEmail());
        addressEditText.setText(user.getAddress());

        if (user.getProfile_Picture() != null && !user.getProfile_Picture().isEmpty()) {
            byte[] decodedString = Base64.decode(user.getProfile_Picture(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageView.setImageBitmap(decodedBitmap);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Load the selected image into the ImageView using Glide
            Glide.with(this)
                    .load(imageUri)
                    .into(profileImageView);

            // Convert the image to Base64 and update the user object
            String encodedImage = convertImageToBase64(imageUri);
            int userId = sessionManager.getKeyUserid();
            if (userId != -1) {
                Users user = dbContext.appDao().findUserById(userId);
                if (user != null) {
                    user.setProfile_Picture(encodedImage);
                    // Update the user in the database
                    dbContext.appDao().updateUser(user);
                }
            }
        }
    }

    // Helper method to convert image Uri to Base64
    private String convertImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}