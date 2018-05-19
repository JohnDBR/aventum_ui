package com.projects.juan.journeys.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.juan.journeys.BuildConfig;
import com.projects.juan.journeys.R;
import com.projects.juan.journeys.activities.AuthActivity;
import com.projects.juan.journeys.activities.MainActivity;
import com.projects.juan.journeys.models.Transaction;
import com.projects.juan.journeys.modules.HttpRequests;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class PofileFragment extends Fragment {

    private ProgressDialog progressDialog;

    public PofileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pofile, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext(), R.style.dialog_light);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");

        final ImageView my_account_profile_pic = view.findViewById(R.id.my_account_profile_pic);
        final TextView my_account_name = view.findViewById(R.id.my_account_name);
        final TextView my_account_email = view.findViewById(R.id.my_account_email);
        final TextView my_account_bio = view.findViewById(R.id.my_account_bio);

        view.findViewById(R.id.pay_example_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                JSONObject transaction_params = new JSONObject();
                try {
                    transaction_params.put("coins", "50");
                    transaction_params.put("transaction_code", "135G12L3K5JBM32146H3K.J1H35L1KJ3H5L123J5H13251K2J35H1L235");
                    transaction_params.put("kind", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpRequests.postRequest(getContext(), getArguments().getString("token"), BuildConfig.CREATE_TRANSACTION, transaction_params, "Error processing transaction", new HttpRequests.CallBack(){
                    @Override
                    public void sendResponse(String response) {
                        Toast.makeText(getContext(), "Transaction complete successfully", Toast.LENGTH_SHORT).show();
                        getInfo(view, my_account_profile_pic, my_account_name, my_account_email, my_account_bio);
                    }

                    @Override
                    public void sendFailure(String response) {

                    }
                });
            }
        });

        view.findViewById(R.id.logout_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthActivity.logout();
                Intent authSuccessful = new Intent(getContext(), AuthActivity.class);
                startActivity(authSuccessful);
                getActivity().finish();
            }
        });

        getInfo(view, my_account_profile_pic, my_account_name, my_account_email, my_account_bio);
    }

    private void getInfo(final View view, final ImageView my_account_profile_pic, final TextView my_account_name, final TextView my_account_email, final TextView my_account_bio){
        progressDialog.show();
        HttpRequests.getRequest(getContext(), getArguments().getString("token"), BuildConfig.GET_STUDENTS, "Network error, try again", new HttpRequests.CallBack(){
            @Override
            public void sendResponse(String response) {
                try {
                    JSONObject user = new JSONObject(response);
                    my_account_name.setText(user.getString("first_name") + " " + user.getString("last_name"));
                    my_account_email.setText("Email: " + user.getString("email"));
                    my_account_bio.setText("Coins: " + user.getString("coins") + "\n" + "CC: " + user.getString("cc") + "\n" + "Phone number: " + user.getString("phone"));
                    Glide.with(view).load(user.getString("profile_picture")).apply(RequestOptions.circleCropTransform()).into(my_account_profile_pic);
                    progressDialog.cancel();
                    //                    :id, :first_name, :last_name, :cc, :email, :phone, :location, :profile_picture
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void sendFailure(String response) {

            }
        });
    }

}
