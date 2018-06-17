package com.projects.juan.journeys.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.juan.journeys.BuildConfig;
import com.projects.juan.journeys.R;
import com.projects.juan.journeys.adapters.JourneysAdapter;
import com.projects.juan.journeys.models.Journey;
import com.projects.juan.journeys.modules.HttpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyJourneysFragment extends Fragment {

    private static ArrayList<Journey> journeys = new ArrayList<>();
    private JourneysAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressDialog progressDialog;

    public MyJourneysFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        if(journeys.isEmpty()) getJourneys();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_journeys, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext(), R.style.dialog_light);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_journeys);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJourneys();
            }
        });
        //Instance and recycler
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_journeys);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        Instance and config adapter
        adapter = new JourneysAdapter(journeys, R.layout.recycler_view_journey_item, new JourneysAdapter.OnClickListener() {
            @Override
            public void onClick(Journey journey) {

            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void getJourneys (){
        progressDialog.show();
        HttpRequests.getRequest(getContext(), getArguments().getString("token"), BuildConfig.JOURNEYS, "Network error, try again", new HttpRequests.CallBack(){
            @Override
            public void sendResponse(String response) {
            try {
                JSONArray content = new JSONArray(response);
                journeys.clear();
                for(int i = 0; i < content.length(); i++){
                    JSONObject cr = content.getJSONObject(i);
                    journeys.add(new Journey(cr, null, null, null));
                }
                Collections.reverse(journeys);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                progressDialog.cancel();
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
