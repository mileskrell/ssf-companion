package org.cpsscifair.ssfcompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_send_feedback:
                Intent sendFeedbackIntent = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:" + getString(R.string.my_email_address)));
                sendFeedbackIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                if (sendFeedbackIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendFeedbackIntent);
                }
                break;
            case R.id.nav_view_the_code:
                Intent viewTheCodeIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.app_github_url)));
                if (viewTheCodeIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(viewTheCodeIntent);
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void viewSsfIncSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://cpsstudentsciencefair.squarespace.com/")));
    }

    public void viewCpsSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://cps.edu/")));
    }

    public void viewMsiSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.msichicago.org/")));
    }

    public void viewTakedaSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.takeda.com/")));
    }

    public void viewBpSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bp.com/")));
    }

    public void viewComedSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.comed.com/")));
    }

    public void viewMotorolaSolutionsFoundationSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.motorolasolutions.com/en_us/about/company-overview/corporate-responsibility/motorola-solutions-foundation.html")));
    }

    public void viewPeoplesGasSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.peoplesgasdelivery.com/")));
    }
}
