package com.example.hiking_app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.databinding.FragmentProfileBinding;
import com.example.hiking_app.databinding.FragmentSettingBinding;
import com.example.hiking_app.model.Users;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private ImageView profileImageView; // Declare profileImageView
    private FragmentSettingBinding binding;
    private DbContext dbContext;
    private SessionManager sessionManager;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dbContext = DbContext.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());
        setOnListener();
        return view;
    }
    private void setOnListener(){
        binding.profileLink.setOnClickListener(v ->{
            showMessage("ProfileAccount");
        });
    }
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*usernameEditText = view.findViewById(R.id.usernameEditText);
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
    }

    private void populateUI(Users user) {
        usernameEditText.setText(user.getUserName());
        emailEditText.setText(user.getEmail());
        addressEditText.setText(user.getAddress());

        if (user.getProfile_Picture() != null && !user.getProfile_Picture().isEmpty()) {
            byte[] decodedString = Base64.decode(user.getProfile_Picture(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageView.setImageBitmap(decodedBitmap);
        }*/
    }
}