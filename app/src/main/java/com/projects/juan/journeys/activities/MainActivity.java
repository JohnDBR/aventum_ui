package com.projects.juan.journeys.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.projects.juan.journeys.R;
import com.projects.juan.journeys.fragments.MyJourneysFragment;
import com.projects.juan.journeys.fragments.PofileFragment;
import com.projects.juan.journeys.fragments.JourneysFragment;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Bundle bundle;
    private Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_journeys:
                    toolbar.setTitle(getResources().getString(R.string.title_search));
                    setFragment(transaction, new JourneysFragment());
                    return true;
                case R.id.navigation_my_journeys:
                    toolbar.setTitle(getResources().getString(R.string.title_journeys));
                    setFragment(transaction, new MyJourneysFragment());
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle(getResources().getString(R.string.title_profile));
                    setFragment(transaction, new PofileFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.journey_toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_my_journeys);

    }

    private void setFragment(android.support.v4.app.FragmentTransaction transaction, Fragment fragment){
        sharedPreferences = getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE);
        bundle = new Bundle();
        bundle.putString("token", sharedPreferences.getString("token", null));
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, fragment).commit();
    }

}
