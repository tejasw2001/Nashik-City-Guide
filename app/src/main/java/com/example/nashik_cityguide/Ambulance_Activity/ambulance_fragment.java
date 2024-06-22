package com.example.nashik_cityguide.Ambulance_Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.nashik_cityguide.Bus_Activity.busTerminal;
import com.example.nashik_cityguide.Bus_Activity.busTerminal_adapter;
import com.example.nashik_cityguide.R;
import com.example.nashik_cityguide.emergency_category;
import com.example.nashik_cityguide.transport_category;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class ambulance_fragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    RecyclerView ambulanceview;
    ambulance_adapter ambulance_Adapter;
    private ImageView back_img;
    SearchView ambulance_searchBar;
    ShimmerFrameLayout shimmer_ambulance;

    public ambulance_fragment() {
        // Required empty public constructor
    }


    public static ambulance_fragment newInstance(String param1, String param2) {
        ambulance_fragment fragment = new ambulance_fragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ambulance_fragment, container, false);

        ambulanceview=(RecyclerView)view.findViewById(R.id.ambulanceview);
        ambulanceview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<ambulance_> options =
                new FirebaseRecyclerOptions.Builder<ambulance_>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Ambulance"), ambulance_.class)
                        .build();

        ambulance_Adapter = new ambulance_adapter(options);
        ambulanceview.setAdapter(ambulance_Adapter);

        back_img = (ImageView)view.findViewById(R.id.back_arrow);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), emergency_category.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        ambulance_searchBar = view.findViewById(R.id.ambulance_searchBar);
        ambulance_searchBar.clearFocus();
        ambulance_searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                String search = query.toUpperCase();

                txtSearch(search);
                return false;
            }
        });

        shimmer_ambulance = view.findViewById(R.id.shimmer_ambulance);
        shimmer_ambulance.startShimmer();

        Handler handler = new Handler();
        handler.postDelayed(() ->{
            ambulanceview.setVisibility(View.VISIBLE);
            shimmer_ambulance.stopShimmer();
            shimmer_ambulance.setVisibility(View.GONE);
        },2000);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        ambulance_Adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ambulance_Adapter.stopListening();
    }

    private void txtSearch(String str){

        FirebaseRecyclerOptions<ambulance_> options =
                new FirebaseRecyclerOptions.Builder<ambulance_>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Ambulance").orderByChild("name").startAt(str).endAt(str+"~"), ambulance_.class)
                        .build();

        ambulance_Adapter = new ambulance_adapter(options);
        ambulance_Adapter.startListening();
        ambulanceview.setAdapter(ambulance_Adapter);
    }
}