package com.projects.juan.journeys.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.projects.juan.journeys.R;
import com.projects.juan.journeys.models.Journey;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class MapsFragment extends Fragment {

    private SupportMapFragment mSupportMapFragment;
    private PolylineOptions polylineOptions;
    private Journey journey;

    public MapsFragment(Journey journey) {
        this.journey = journey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapView, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setAllGesturesEnabled(true);
                        for(int i = 0; i < journey.getStops().length(); i++){
                            try {
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(journey.getStops().getJSONObject(i).getDouble("latitude"), journey.getStops().getJSONObject(i).getDouble("longitude"))).title("Origin").snippet("Time: test"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        LatLng origin = new LatLng(10.978499, -74.817864);
                        LatLng dest = new LatLng(11.020743, -74.850721);
                        googleMap.addMarker(new MarkerOptions().position(dest).title("Dest").snippet("Time: test"));

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(centerPoints(10.978499, -74.817864, dest.latitude, dest.longitude)).zoom(13).bearing(angleBteweenPoints(10.978499, -74.817864, dest.latitude, dest.longitude)).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);
                    }

                }
            });
        }
    }

    private LatLng centerPoints(double lat1, double long1, double lat2,double long2) {
        return new LatLng((lat1+lat2)/2, (long1+long2)/2);
    }

    private float angleBteweenPoints(double lat1, double long1, double lat2, double long2) {
        double dLon = (long2 - long1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng;
        return Float.parseFloat(brng + "");
    }

}
