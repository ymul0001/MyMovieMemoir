package com.example.mymoviememoir;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_dashboard:
                replaceFragment(new DashboardFragment());
                break;
            case R.id.menu_search:
                replaceFragment(new SearchFragment());
                break;
            case R.id.menu_memoir:
                replaceFragment(new MemoirFragment());
                break;
            case R.id.menu_watchlist:
                replaceFragment(new WatchListFragment());
                break;
            case R.id.menu_reports:
                replaceFragment(new ReportsFragment());
                break;
            case R.id.menu_maps:
                replaceFragment(new MapsFragment());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment dashBoardFragment = new DashboardFragment();
        Intent fromLogin = getIntent();
        Bundle bundle = fromLogin.getExtras();
        saveData(bundle);
        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.app_drawer_layout);
        navigationView = findViewById(R.id.nv);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        dashBoardFragment.setArguments(bundle);
        replaceFragment(dashBoardFragment);
    }

    protected void saveData(Bundle bundle){
        SharedPreferences sf = getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("firstName", bundle.getString("firstName"));
        editor.putInt("personId", bundle.getInt("personId"));
        editor.apply();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }



}
