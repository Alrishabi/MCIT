package com.example.musab.mcit.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.musab.mcit.Notifications.NotificationService;
import com.example.musab.mcit.R;
import com.example.musab.mcit.mFragment.AboutApp;
import com.example.musab.mcit.mFragment.ContactUsFrag;
import com.example.musab.mcit.mFragment.LoginFragment;
import com.example.musab.mcit.mFragment.Webside;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
         Toolbar toolbar;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_Login);

        ///////////////start service of notification///////////////////
        if (NotificationService.serviceIsRun==false){
            NotificationService.serviceIsRun=true;
          Intent  intent=new Intent(getApplication(),NotificationService.class);
            startService(intent);
          //  Toast.makeText(getApplicationContext(),"go to s",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.logout)
                    .setTitle(R.string.dialog_quit_title)
                    .setMessage(R.string.dialog_quit_message)
                    .setPositiveButton(R.string.dialog_quit_positive_button, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.dialog_quit_negative_button, null)
                    .show();
           // super.onBackPressed();
        }
    }
    private void displaySelectedScreen(int id){
        android.support.v4.app.Fragment fragment=null;
        switch (id){

            case R.id.nav_Login:
                fragment = new LoginFragment();
                break;
            case R.id.nav_webside:
                fragment = new Webside();
                break;
            case R.id.nav_about_app:
                fragment = new AboutApp();
                break;
            case R.id.nav_contact_us:
                fragment = new ContactUsFrag();
                break;
        }
        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main,fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }
}

