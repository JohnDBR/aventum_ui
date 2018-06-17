package com.projects.juan.journeys.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.juan.journeys.R;
import com.projects.juan.journeys.models.Journey;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class DetailsFragment extends Fragment {

    Journey journey;
    public DetailsFragment(Journey journey) {this.journey = journey;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView driver_profile_picture = view.findViewById(R.id.details_driver_profile_picture);
        final TextView driver_name = view.findViewById(R.id.details_driver_name);
//        final TextView my_account_name = view.findViewById(R.id.my_account_name);
//        final TextView my_account_email = view.findViewById(R.id.my_account_email);
//        final TextView my_account_bio = view.findViewById(R.id.my_account_bio);
//
//        my_account_name.setText(user.getString("first_name") + " " + user.getString("last_name"));
//        my_account_email.setText("Email: " + user.getString("email"));
//        my_account_bio.setText("Coins: " + user.getString("coins") + "\n" + "CC: " + user.getString("cc") + "\n" + "Phone number: " + user.getString("phone"));
        Log.i("DRIVER_PROFILE_PICTURE", "Profile picture: " +journey.getDriver().getProfile_picture());
        Glide.with(view).load(journey.getDriver().getProfile_picture()).apply(RequestOptions.circleCropTransform()).into(driver_profile_picture);
        driver_name.setText(journey.getDriver().getFirst_name() + " " + journey.getDriver().getLast_name());
    }
}
