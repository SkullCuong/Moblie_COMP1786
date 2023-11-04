package com.example.hiking_app.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.ListHikeAdapter;
import com.example.hiking_app.controller.hike_controller.ViewHike;
import com.example.hiking_app.databinding.FragmentProfileBinding;
import com.example.hiking_app.model.Hikes;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsHikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsHikeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Hikes> hikes;
    private SearchView searchView;
    private Spinner spinner;
    private String[] choices;

    private String mParam1;
    private String mParam2;

    public DetailsHikeFragment() {
        // Required empty public constructor
    }

    public static DetailsHikeFragment newInstance(String param1, String param2) {
        DetailsHikeFragment fragment = new DetailsHikeFragment();
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
        View view = inflater.inflate(R.layout.activity_hikes, container, false);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        hikes = DbContext.getInstance(requireContext()).appDao().getListHike();
        ListHikeAdapter listAdapter = new ListHikeAdapter(hikes, requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.listHikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(listAdapter);


        spinner = view.findViewById(R.id.spinner);
        choices = new String[]{"Newest", "Oldest"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        setListener(listAdapter);

        return view;
    }

    private void setListener(ListHikeAdapter listAdapter) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                hikes = DbContext.getInstance(requireActivity()).appDao().getListHikeByName(newQuery);
                listAdapter.setFilteredList(hikes);
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedChoice = choices[position];

                if (selectedChoice.equals("Newest")) {
                    hikes = DbContext.getInstance(requireContext()).appDao().getListHike();
                    listAdapter.setFilteredList(hikes);
                } else if (selectedChoice.equals("Oldest")) {
                    hikes = DbContext.getInstance(requireContext()).appDao().getListHikeOldest();
                    listAdapter.setFilteredList(hikes);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }
}