package com.washedup.anagnosti.ergo.eventPerspective;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.washedup.anagnosti.ergo.R;

public class EventPerspectiveActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_perspective);

        Toolbar toolbar = findViewById(R.id.activity_event_perspective_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.activity_event_perspective_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_event_perspective_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                    new MyObligationsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_my_obligations);
        }


    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_about_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new AboutEventFragment()).commit();
                break;

            case R.id.nav_assign_obligations:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new AssignObligationsFragment()).commit();
                break;

            case R.id.nav_break_centre:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new BreakCentreFragment()).commit();
                break;
                
            case R.id.nav_event_schedule:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new EventScheduleFragment()).commit();
                break;

            case R.id.nav_list_of_people:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new ListOfPeopleFragment()).commit();
                break;

            case R.id.nav_my_obligations:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_event_perspective_frame_layout,
                        new MyObligationsFragment()).commit();
                break;

            case R.id.nav_contact_superior:
                Toast.makeText(this, "Calling Zlatko...", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
