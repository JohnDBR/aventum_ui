package com.projects.juan.journeys.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.projects.juan.journeys.BuildConfig;
import com.projects.juan.journeys.R;
import com.projects.juan.journeys.adapters.JourneyDetailAdapter;
import com.projects.juan.journeys.fragments.DetailsFragment;
import com.projects.juan.journeys.fragments.MapsFragment;
import com.projects.juan.journeys.fragments.UsersFragment;
import com.projects.juan.journeys.models.Journey;
import com.projects.juan.journeys.models.User;
import com.projects.juan.journeys.modules.HttpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JourneyDetailsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private JourneyDetailAdapter journeyDetailAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);

        toolbar = findViewById(R.id.journey_details_toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this, R.style.dialog_light);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        journeyDetailAdapter = new JourneyDetailAdapter(getSupportFragmentManager());

        getInfo(new CallBack() {
            @Override
            public void onGetInfo(final Journey journey) {
                toolbar.setTitle(journey.getStart() + " \u2192 " + journey.getEnd());
                journeyDetailAdapter.addFragment(new DetailsFragment(journey), getResources().getString(R.string.details));
                journeyDetailAdapter.addFragment(new MapsFragment(journey), getResources().getString(R.string.title_activity_maps));
                journeyDetailAdapter.addFragment(new UsersFragment(journey), getResources().getString(R.string.users));
                viewPager.setAdapter(journeyDetailAdapter);
                tabLayout.setupWithViewPager(viewPager);

                findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JourneyDetailsActivity.this);
                        builder.setTitle(getResources().getString(R.string.join_to) + journey.getCode());
                        builder.setMessage(getResources().getString(R.string.price) + journey.getPrice());

                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                JSONObject join_params = new JSONObject();
                                try {
                                    join_params.put("code", journey.getCode());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                HttpRequests.postRequest(getApplicationContext(), getIntent().getStringExtra("token"),
                                        BuildConfig.JOURNEYS + "/" + getIntent().getIntExtra("id", 0) + "/join", join_params, getResources().getString(R.string.network_error), new HttpRequests.CallBack() {
                                            @Override
                                            public void sendResponse(String response) {
                                                try {
                                                    JSONObject jr = new JSONObject(response);
                                                    if(response.contains("err")){
                                                        Log.d("err", response);
                                                        Toast.makeText(getApplicationContext(), jr.getString("err"), Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.join_to) + getIntent().getIntExtra("id", 0) + " successfully", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void sendFailure(String response) {

                                            }
                                        });
                            }
                        });
                        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getIcon().setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getIcon().setColorFilter(getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//        tabLayout.getTabAt(0).getIcon().setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    private void getInfo(final CallBack callBack){
        progressDialog.show();
        HttpRequests.getRequest(getApplicationContext(), getIntent().getStringExtra("token"), BuildConfig.JOURNEYS + "/" + getIntent().getIntExtra("id", 0), getResources().getString(R.string.journeys_not_found), new HttpRequests.CallBack() {
            @Override
            public void sendResponse(String response) {
                try {
                    Journey journey = new Journey(new JSONObject(response).getJSONObject("journey"), new JSONObject(response).getJSONArray("stops"), new JSONObject(response).getJSONObject("driver"), new JSONObject(response).getJSONArray("students"));
                    Log.d("JOURNEY_OBJECT", journey.getDriver().getProfile_picture());
                    callBack.onGetInfo(journey);
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

    public interface CallBack {
        void onGetInfo(Journey journey);
    }
}
